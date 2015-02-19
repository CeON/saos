
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
