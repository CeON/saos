
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
    $("#select-court").courtDivisionSelect({
		divisionId: "select-division",
		divisionUrl: contextPath + "/search/division/"
	});
    
    $("#search-settings").searchSettingsToolTip({
    	box: "settings-box"
    });
    
    
    SearchFilters.run({
    	formId: "#search-form",
    	removeAll: "#clearAllFilters",
    	parentContainer: ".judgment-list",
    	
    	filters: [{button: ".judge", searchfield: "#input-search-judge", filterfield: "filter-judge"},
		           {button: ".keyword", searchfield: "#input-search-keywords", filterfield: "filter-keyword"},
		           {button: ".type", searchfield: "[name='judgmentType']", filterfield: "filter-judgment-type"},
		           {button: ".date", searchfield: "#datepicker_from, #datepicker_to", filterfield: "filter-judgment-date-from"},
				   {button: ".court", searchfield: "#select-court", filterfield: "filter-court"}],
				   
		
		advanceFilter: {button: ".division", searchfield: "#select-division", filterfield: "filter-division",
						url: contextPath + "/search/division/",
						parent : {button: ".court", searchfield: "#select-court", filterfield: "filter-court"}},
    });
    
    
});
