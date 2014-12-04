
function jAlert(messageText) {
	$("<span class='col-md-7'>" + messageText +" </span>").dialog({ modal: true, width: "500px", buttons: [ { text: "Ok", click: function() { $( this ).dialog( "close" ); } } ] });
}

function clearSubmitMade() {
    __submitMade = false;
}

$(document).ready(function() {
	
	clearSubmitMade();
	
	$.blockUI.defaults.css = { border: 'none', 
            padding: '15px', 
            backgroundColor: '#000', 
            '-webkit-border-radius': '10px', 
            '-moz-border-radius': '10px', 
            opacity: .5, 
            color: '#fff' };
	$.blockUI.defaults.message = '<img src="${contextPath}/static/images/ajax-loader.gif">';
	
    $.datepicker.setDefaults($.datepicker.regional["pl"]);
    $('[id^="datepicker_"]').datepicker({changeYear: 'true', dateFormat: "dd-mm-yy", yearRange: 'c-50:' + ((new Date()).getFullYear()+1) });
    
    $('[title]').tooltip({container: 'body'});
    
    
    /*** Search form show more fields ***/
    SearchFormMode.init();
    SearchCriteria.init();
    
    //Search form - init select court & division
    CourtDivisionSelect.run({
		fields: [{court: "#select-common-court", divisionId: "#select-common-division", divisionUrl: contextPath + "/search/courtDivision/"},
		         {court: "#select-supreme-chamber", divisionId: "#select-supreme-chamber-division", divisionUrl: contextPath + "/search/chamberDivision/"}]
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
		           {button: ".keyword", searchfield: "#input-search-keywords", filterfield: "filter-keyword", selectFormType: "#radio-court-common"},
		           {button: ".type", searchfield: "[name='judgmentTypes']", filterfield: "filter-judgment-type"},
		           {button: ".date", searchfield: "#datepicker_from, #datepicker_to", filterfield: "filter-judgment-date-from"},
		           {button: ".personnel-type", searchfield: "#input-search-personnel-type", filterfield: "filter-supreme-personnel-type", selectFormType: "#radio-court-supreme"},
				   {button: ".court", searchfield: "#select-common-court", filterfield: "filter-court", selectFormType: "#radio-court-common"},
				   {button: ".chamber", searchfield: "#select-supreme-chamber", filterfield: "filter-supreme-chamber", selectFormType: "#radio-court-supreme"}],
		
		advanceFilter: [{button: ".division", searchfield: "#select-common-division", filterfield: "filter-division", selectFormType: "#radio-court-common",
						url: contextPath + "/search/courtDivision/",
						parent : {button: ".court", searchfield: "#select-common-court", filterfield: "filter-court"}},
						{button: ".chamber-division", searchfield: "#select-supreme-chamber-division", filterfield: "filter-supreme-chamber-division", selectFormType: "#radio-court-supreme",
						url: contextPath + "/search/chamberDivision/",
						parent : {button: ".chamber", searchfield: "#select-supreme-chamber", filterfield: "filter-supreme-chamber"}}],
    });
    
    
    ChangeCourtType.run({
    	fields: [{fields: "#all-fields", button: "#radio-all"},
		          {fields: "#common-court-fields", button: "#radio-court-common"},
		          {fields: "#supreme-court-fields", button: "#radio-court-supreme"}],
		          
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
    $("#input-search-keywords").autoCompletionSuggester({url: contextPath + "/ccKeywords/"});
    
    
    
});
