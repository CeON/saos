/**
 * @author Łukasz Pawełczak
 * 
 * Modules used in judgment search view.
 */

var jsInitInJudgmentSearch = function() {

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
		
		fieldGroups: [{filterField: "radio-court-common", container: "#common-court-fields"}],
		
		filters: [{button: ".judge", searchfield: "#input-search-judge", filterfield: "filter-judge"},
		           {button: ".keyword", searchfield: "#input-search-keywords-cc", filterfield: "filter-keyword", selectFormType: "#radio-court-common"},
		           {button: ".judgment-type", searchfield: "[name='judgmentTypes']", filterfield: "filter-judgment-type"},
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
		fields: [{fields: "#all-fields", button: "#radio-all", onChangeCallback: function() {}},
		          {fields: "#common-court-fields", button: "#radio-court-common", onChangeCallback: function() {$("#input-search-keywords-cc").suggesterRefresh();}},
		          {fields: "#supreme-court-fields", button: "#radio-court-supreme", onChangeCallback: function() {}}],
		          
		fieldsContainer: ".fields-container",
		radioName: "courtType",
		parentContainer: "#search-form"
	});
	
	$("#filter-box").filterBox({
		removeAllButton: "#clearAllFilters",
		noFiltersMessage: "#no-filters",
		filterField: ".filter-item",
		resultList: ".judgment-list > div:first-child",
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
    			enabled: true,
    			placeholder: springMessage.judgmentSearchFormFieldKeywordsPlaceholder
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
}
 
