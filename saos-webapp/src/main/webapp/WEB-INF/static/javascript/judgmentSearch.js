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
				return moment(value, 'DD-MM-YYYY', true).isValid();
			} else {
				return true;
			}
		}, 
		""
	);
    
    $('#search-form').validate({
        onkeyup: false,
        onclick: false,
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
            $parent.prev().addClass("has-error");
            
            $(id).css({display: "block"});
            
            $parent.parent().css({marginTop: "35px"});
        },
        unhighlight: function(element, errorClass, validClass) {
            var id = "#" + $(element).attr("id") + "-error",
            $parent = $(element).parent(); 
            
            $parent.removeClass("has-error");
            $parent.prev().removeClass("has-error");
            
            $(id).css({display: "none"});
            
            if ($parent.parent().find(".has-error").length === 0) {
            $parent.parent().css({marginTop: "0px"});
            }
            
        }
    });
    
	/* Format date */
    $("#datepicker_from, #datepicker_to")
    	.focusout(function() {
    		var $this = $(this),
    			text = $this.val();
    		
    		$this.val(DateFormat.convert(text));
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
	
	//Search form - init select court & division
	CourtDivisionSelect.run({
		fields: [{	court: "#select-common-court",
					divisionId: "#select-common-division",
					getDivisionUrl: function(id) {return contextPath + "/cc/courts/{id}/courtDivisions/list".replace("{id}", id); }
				 },
		         {	court: "#select-supreme-chamber",
					divisionId: "#select-supreme-chamber-division",
					getDivisionUrl: function(id) {return contextPath + "/sc/chambers/{id}/chamberDivisions/list".replace("{id}", id); }
				}]
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
		           {button: ".keyword", searchfield: "#input-search-keywords-cc", filterfield: "filter-keyword", selectFormType: "#radio-court-common"},
		           {button: ".judgment-type", searchfield: "[name='judgmentTypes']", filterfield: "filter-judgment-type"},
		           {button: ".court-type", searchfield: "[name='courtType']", filterfield: "filter-court-type"},
		           {button: ".date", searchfield: "#datepicker_from, #datepicker_to", filterfield: "filter-judgment-date-from"},
				   {button: ".court", searchfield: "#select-common-court", filterfield: "filter-court", selectFormType: "#radio-court-common"},
				   {button: ".chamber", searchfield: "#select-supreme-chamber", filterfield: "filter-supreme-chamber", selectFormType: "#radio-court-supreme"},
				   {button: ".judgment-form", searchfield: "#select-search-judgment-form", filterfield: "filter-supreme-judgment-form", selectFormType: "#radio-court-supreme"}],
		
		advanceFilter: [{button: ".division", searchfield: "#select-common-division", filterfield: "filter-division", selectFormType: "#radio-court-common",
						getUrl: function(id) {return contextPath + "/cc/courts/{id}/courtDivisions/list".replace("{id}", id)},
						parent : {button: ".court", searchfield: "#select-common-court", filterfield: "filter-court"}},
						{button: ".chamber-division", searchfield: "#select-supreme-chamber-division", filterfield: "filter-supreme-chamber-division", selectFormType: "#radio-court-supreme",
						getUrl: function(id) {return contextPath + "/sc/chambers/{id}/chamberDivisions/list".replace("{id}", id)},
						parent : {button: ".chamber", searchfield: "#select-supreme-chamber", filterfield: "filter-supreme-chamber"}}],
	});
	
	
	ChangeCourtType.run({
		fieldsContainer: ".fields-container",
		radioName: "courtType",
		parentContainer: "#search-form",
		defaultFieldsContainer: "#all-fields",
		
		fields: [{fields: "#all-fields", button: "#radio-all", onChangeCallback: function() {}},
		          {fields: "#common-court-fields", button: "#radio-court-common", onChangeCallback: function() {$("#input-search-keywords-cc").suggesterRefresh();}},
		          {fields: "#supreme-court-fields", button: "#radio-court-supreme", onChangeCallback: function() {}},
		          {fields: "#constitutional-tribunal-fields", button: "#radio-court-constitutional_tribunal", onChangeCallback: function() {}}]
	});
	
	
	$("#filter-box").filterBox({
		removeAllButton: "#clearAllFilters",
		noFiltersMessage: "#no-filters",
		filterField: ".filter-item",
		resultList: "#judgment-list",
		buttonHide: "#filter-hide",
		stickyOptions: {enabled: true, topSpacing: 10},
		settingsButton: {className: "filter-box-button"},
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
		fieldLawJournalId: "#lawJournalEntryId",
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
 
