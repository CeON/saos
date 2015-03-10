
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
