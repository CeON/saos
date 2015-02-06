/*
 * Module that cleans url from empty parameters.
 * It simply clears attribute name in all empty/not used
 * fields like: 
 * inputs and selects.
 * 
 * @author Łukasz Pawełczak
 */

var CleanUrlForm = {
		
	/* Clear attribute name of inputs and selects
	 * found in {@param form}
	 * 
	 * @param $form - jquery object of form
	 */  
	clear: function($form) {
		
		$form.find("input, select").each(function() {
			var $this = $(this);
			if ($this.val() === "") {
				$this.attr("name", "");
			}
		});
		
		$form.find("input:checkbox").each(function() {
			var $this = $(this);
			if (!$this.is(":checked")) {
				$this.attr("name", "");
				$this.next().attr("name", "");
			}
		});
	}
}

$.fn.cleanUrlForm = function() {
	$(this).submit(function(event) {
		CleanUrlForm.clear($(this));
	});
};

