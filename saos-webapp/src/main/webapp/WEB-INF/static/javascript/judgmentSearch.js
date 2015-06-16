/**
 * Modules used in judgment search view.
 * 
 * @author Łukasz Pawełczak
 */
var jsInitInJudgmentSearch = function() {

	
	/* Clear button clears form */ 
	$("#search-form button[type='reset']").click(function() {
		setTimeout(function() {
			ClearSearchForm($("#search-form"));
		}, 200);
	});
	
	/* Shorten long url.
	 * When submiting form, clean empty paramters*/
	$("#search-form").cleanUrlForm();
	
    /* Search form validation */
    $.validator.addMethod(
		"dateFormat",
		function(value, element) {
			if (value !== '') {
				return moment(value, 'D-M-YYYY', true).isValid();
			} else {
				return true;
			}
		}, 
		""
	);
    
    $('#search-form').validate({
        onkeyup: false,
        onclick: false,
        ignore: "",
        rules: {
            dateFrom: {
                dateFormat: true
            },
            dateTo: {
                dateFormat: true
            }
        },
        errorPlacement: function(error, element) {},
        highlight: function(element, errorClass, validClass) {
            var id = "#" + $(element).attr("id") + "-error",
            $parent = $(element).parent(); 
            
            $parent.addClass("has-error");

            $(id).css({display: "block"});
        },
        unhighlight: function(element, errorClass, validClass) {
            var id = "#" + $(element).attr("id") + "-error",
            $parent = $(element).parent(); 
            
            $parent.removeClass("has-error");
            
            $(id).css({display: "none"});
            
        }
    });
    
	/* Format date */
    $("#datepicker_from, #datepicker_to")
    	.focusout(function() {
    		var $this = $(this),
    			text = $this.val();
    		
    		$this.val(DateFormat.convert(text));
    	})
    	.change(function() {
    	    $(this).valid();
    	});
    
    
    
    /* Search form: selecting date with datepicker */
    
    $.datepicker.setDefaults($.datepicker.regional["pl"]);
    
    $('[id^="datepicker_"]').datepicker({changeYear: 'true', dateFormat: "dd-mm-yy",
		yearRange: 'c-50:' + ((new Date()).getFullYear()+1)
	});


	/*** Search form show more fields ***/
	SearchFormMode.init({
			callback: {
				onShow: function() {
					$("#input-search-keywords-cc").suggesterRefresh();
				},
				onHide: function() {}
			}
	});

	SearchCriteria.init();
	SearchContext.init();
	

	$("#courtType_SUPREME").one("click", function() {
    
        //init sc judgment form select with options
        addOptionsToSelect({
            selectId: "#select-search-judgment-form",
            url: contextPath + "/sc/judgmentForms/list",
            createOption: function(element) {
                return "<option value='" + element.id + "' >" + element.name + "</option>";
            }
        });
    });
	
	
	$("#search-settings").searchSettingsToolTip({
		box: "settings-box"
	});
	
	
	SearchFilters.run({
		formId: "#search-form",
		removeAll: "#clearAllFilters",
		parentContainer: ".judgment-list",
		
		fieldGroups: [{filterField: "radio-court-common", container: "#common-court-fields"},
		              {filterField: "radio-court-supreme", container: "#supreme-court-fields"},
		              {filterField: "radio-court-constitutional_tribunal", container: "#constitutional-tribunal-fields"}],
		
		filters: [{button: ".judge", searchfield: "#input-search-judge", filterfield: "filter-judge"},
		           {button: ".keyword", searchfield: "#input-search-keywords-cc", filterfield: "filter-keyword", selectFormType: "#courtType_COMMON"},
		           {button: ".judgment-type", searchfield: "[name='judgmentTypes']", filterfield: "filter-judgment-type"},
		           {button: ".court-type", searchfield: "[name='courtCriteria.courtType']", filterfield: "filter-court-type"},
		           {button: ".date", searchfield: "#datepicker_from, #datepicker_to", filterfield: "filter-judgment-date-from"},
				   {button: ".court", searchfield: "#select-common-court", filterfield: "filter-court", selectFormType: "#courtType_COMMON"},
				   {button: ".chamber", searchfield: "#select-supreme-chamber", filterfield: "filter-supreme-chamber", selectFormType: "#courtType_SUPREME"},
				   {button: ".judgment-form", searchfield: "#select-search-judgment-form", filterfield: "filter-supreme-judgment-form", selectFormType: "#courtType_SUPREME"}],
		
		advanceFilter: [{button: ".division", searchfield: "#select-common-division", filterfield: "filter-division", selectFormType: "#courtType_COMMON",
						getUrl: function(id) {return contextPath + "/cc/courts/{id}/courtDivisions/list".replace("{id}", id)},
						parent : {button: ".court", searchfield: "#select-common-court", filterfield: "filter-court"}},
						{button: ".chamber-division", searchfield: "#select-supreme-chamber-division", filterfield: "filter-supreme-chamber-division", selectFormType: "#courtType_SUPREME",
						getUrl: function(id) {return contextPath + "/sc/chambers/{id}/chamberDivisions/list".replace("{id}", id)},
						parent : {button: ".chamber", searchfield: "#select-supreme-chamber", filterfield: "filter-supreme-chamber"}}],
	});
	
	
	CourtCriteriaForm.init();
    	
	
    
    /* Search form section: date */
    infoFormSection({
        formSectionId: "#date-form-section",
        infoSectionId: "#date-info-section",
        clearFormButtonId: "#date-clear-form-button",
        extractInfoFromFormCustom: extractInfoFromDateSectionInSearch,
        defaultInfoSectionText: springMessage.contextDateAnyValue
    });
    
    
    /* Search form section: judgment extra fields */
    var extraFieldInfoFormSection = infoFormSection({
        formSectionId: "#judgment-form-section",
        infoSectionId: "#judgment-info",
        clearFormButtonId: "#judgment-clear-form-button",
        extractInfoFromFormCustom: extractInfoFromJudgmentFormSectionInSearch,
        defaultInfoSectionText: springMessage.judgmentSearchJudgmentSectionDefaultText,
        onAfterClearFormSection: function() {
            $("#law-journal-navigation").trigger("removeSelected");
            $("#input-search-keywords-cc").suggesterClear();
        }
    });

    this.updateExtraFieldInfoFormSection = function() {
        extraFieldInfoFormSection.updateInfoSection();
    }

    /* Search form section: court type */
    infoFormSection({
        formSectionId: "#court-form-section",
        infoSectionId: "#court-info-section",
        clearFormButtonId: "#court-clear-form-button",
        extractInfoFromFormCustom: extractInfoFromCourtSection,
        onFormSectionCloseAction: updateExtraFieldInfoFormSection,
        onAfterClearFormSection: updateExtraFieldInfoFormSection
    });

    
    
    //refresh keywords suggester width when form section opens
    $("#judgment-info").on("click", function() {
        setTimeout(function() {$("#input-search-keywords-cc").suggesterRefresh()}, 1000);
    });
	
	
	/* Search form view.
	 * Autocompletion added to search field CommonCourtKeywords.
	 */
    $("#input-search-keywords-cc")
    	.suggester({
    		boxyMode: {
    			enabled: true
    		},
    		
    		url: function(id) {
    			return contextPath + "/keywords/COMMON/{id}".replace("{id}", id);
    		},
    		getValue: function(element) {
    			return element.phrase;
    		}
    	});
    
    //Reset width of the common court keywords field
    $("#judgment-info").click(function() {
        $("#input-search-keywords-cc").suggesterRefresh();    
    });
    
    
    /* Judgment results list, extract text. 
     * Remove single letter words from end of the lines.
     */
    $(".judgment-list .extract").each(function() {
    	$(this).moveLettersFromEndOfLine({
        	letters: ["a", "i", "e", "o", "w", "z", "–", "k"]
        });
    });
    
    
    /* Law journal selection */
    SelectLawJournalEntry.init({
		url: contextPath + "/search/lawJournalEntries",
		
		form: "#search-form",
		fieldLawJournalCode: "#lawJournalEntryCode",
		fieldsContainer: "#law-journal-fields",
		buttonsContainer: "#law-journal-navigation",
		buttonCloseContainer: "#law-journal-close",
		buttonLoadMore: "#law-journal-more",
		setButton: "#law-journal-set",
		list: "#law-journal-list",
		
		fields: {
			year: "#law-journal-year",
			journalNo: "#law-journal-journalNo",
			entry: "#law-journal-entry",
			text: "#law-journal-text"
		},

		text: {
			choseItem: springMessage.judgmentSearchFormFieldLawJournalChoseItem,
			noItems: springMessage.judgmentSearchFormFieldLawJournalNoItems,
		}
	});

}
 
