
function jAlert(messageText) {
	$("<span class='col-md-7'>" + messageText +" </span>").dialog({ modal: true, width: "500px", buttons: [ { text: "Ok", click: function() { $( this ).dialog( "close" ); } } ] });
}

function clearSubmitMade() {
    __submitMade = false;
}


/**
 * Extracts and returns an index of the passed element. Assumes that the index is the last part
 * of the element id preceded by an underscore (_). <br/>
 * This function is useful in situation where one prints collection elements and then wants to deal with them individually
 * (for example delete from collection).
 */   
function extractIndex(element) {
    return element.attr('id').split('_')[1];
}






/****************** MONTH YEAR RANGE PICKER **/


/**
 * Ties the month-year range selects. Month-year range selects are four html selects 
 * corresponding to month start, year start, month end, year end. Options of these selects are whole numbers
 * representing months or years. </br>
 * This function binds the selects together so, for example, changes to one of them can influence the other ones.
 * E.g. changing the end year to the value lower than the start year decreases the start year.
 * 
 * @param monthStartId the id of the select representing the range start month
 * @param yearStartId the id of the select representing the range start year
 * @param monthEndId the id of the select representing the range end month
 * @param yearEndId the id of the select representing the range end year
 */
function tieMonthYearRangeSelects(monthStartId, yearStartId, monthEndId, yearEndId) {
    
    var monthStart = $('#' + monthStartId);
    var yearStart = $('#' + yearStartId);
    var monthEnd = $('#' + monthEndId);
    var yearEnd = $('#' + yearEndId);
    
    
    yearStart.change(function() {
        
        if (yearStart.val() >= yearEnd.val()) {
            
            yearEnd.val(yearStart.val());
            
            if (monthStart.val() > monthEnd.val()) {
                
                monthEnd.val(monthStart.val());
                
            }
        }
        
    });


    yearEnd.change(function() {
        
        if (yearStart.val() >= yearEnd.val()) {
            
            yearStart.val(yearEnd.val());
            
            if (monthStart.val() > monthEnd.val()) {
                
                monthStart.val(monthEnd.val());
                
            }
        }
        
    });
    
    
    monthStart.change(function() {
       
        if (yearStart.val() == yearEnd.val() && monthStart.val() > monthEnd.val()) {
            
            monthEnd.val(monthStart.val());
            
        }
            
    });
    

    monthEnd.change(function() {
        
        if (yearStart.val() == yearEnd.val() && monthStart.val() > monthEnd.val()) {
            
            monthStart.val(monthEnd.val());
            
        }
            
    });
    
    
}



/**
 * Adds space every three digits
 * 
 * @param nStr number in string format 
 * */
function addSpacesEvery3Digits(nStr) {
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ' ' + '$2');
	}
	return x1 + x2;
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
    
	
    /* Enable bootstrap tooltip */
    $('[data-toggle="tooltip"]').tooltip({container: 'body'});
	
    /* Enable bootstrap popover's */
    $('[data-toggle="popover"]').popover({container: 'body'});
    
    
    /* Hints */
    $(".hint").click(function(event) {
    	event.preventDefault();
    });
	
	/* Anchor with empty href, should not reload page*/
	$("a[href='']").click(function(event) {
		event.preventDefault();
	});
	
	
});
