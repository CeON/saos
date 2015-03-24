function ClearSearchForm($form) {

	//Remove values from text inputs
	$form
		.find('input:text, input:password, input:file, select, textarea')
		.val('');
	
	//Uncheck all checkboxes
    $form
    	.find('input:checkbox')
        .removeAttr('checked')
        .removeAttr('selected');

    //Select first button in radio group
    $form
    	.find('input[type="radio"]:first')
    	.attr('checked', true)
    	.trigger('click');
    
    //Select first element on select list
    $form
    	.find('select')
    	.selectedIndex=0;
};

