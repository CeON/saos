/*
 * Module for switching between different parts of search form, based on court type.
 * 
 * @author Łukasz Pawełczak
 */
var ChangeCourtType = (function() {
	
	var space = {},
		fields = [{fields: "#all-fields", button: "#radio-all"},
		          {fields: "#common-court-fields", button: "#radio-common"},
		          {fields: "#supreme-court-fields", button: "#radio-supreme"}],
		          
		fieldsContainer = ".fields-container",
		radioName = "courtType",
		parentContainer = "#search-form",
	
	init = function(source) {
		
	},
	
	showSelectedFields = function() {
		$("input[name='" + radioName + "']:checked").trigger("click");
	},
	
	/* Show fields assigned to specified court type */
	assignButton = function() {
		var i = 0,
			length = fields.length;
		
		for (i; i < length; i+= 1) {
			$(parentContainer + " " + fields[i].button).each(function() {
				var $fieldContainer = $(fields[i].fields);

				$(this).click(
					(function() {
						return function() {
							hideAll();
							clearFields($fieldContainer);
							$fieldContainer.css("display", "block");
						};
					})()
				);
			});
		}
	},
	
	hideAll = function() {
		$(fieldsContainer).each(function() {
			$(this).css("display", "none");
		});
	},
	
	/* Selecting target court type clears form fields specified for others court types */ 
	clearFields = function($fieldsContainer) {
		$fieldsContainer.find(":input").each(function() {
			$(this)
			  .not(':button, :submit, :reset, :hidden')
			  .val('')
			  .removeAttr('checked')
			  .removeAttr('selected');
		});
	};
	
	/***** PUBLIC *****/
	
	space.run = function(source) {
		init(source);
		assignButton();
		hideAll();
		showSelectedFields();
	};
	
	return space;
}());
