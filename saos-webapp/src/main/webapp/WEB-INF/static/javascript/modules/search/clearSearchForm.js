/*
 * Function clears fields in form.
 * 
 * @param $form - jquery object that contains search form
 */
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
	
	//Select first element on select list
	$form
		.find('select')
		.selectedIndex=0;
	

	//Select first button in radio group
	var radioGroups = findAllRadioGroups();

	for (group in radioGroups) {
		
		$form
			.find('input[name="' + group + '"]:first')	
			.attr('checked', true)
			.trigger('click');
	}
	
	
	//Finds all radio button groups
	function findAllRadioGroups() {
		
		var radioGroups = [];
		
		$form.find(':radio').each(function() {
			radioGroups[this.name] = true;
		});
		
		return radioGroups;
	}
};

