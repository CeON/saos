/*
 * Module for switching between different parts of search form, based on court type.
 * E.g. switch from common court to supreme court.
 * 
 * @author Łukasz Pawełczak
 */
var ChangeCourtType = (function() {
	
	var space = {},
		fields = [{fields: "", button: ""}],
		          
		fieldsContainer = "",
		radioName = "",
		parentContainer = "",
	
	init = function(source) {
		
		if (source.fields !== "") {
			fields = source.fields;
		}
		
		if (source.fieldsContainer !== "") {
			fieldsContainer = source.fieldsContainer;
		}
		
		if (source.radioName !== "") {
			radioName = source.radioName;
		}
		
		if (source.parentContainer !== "") {
			parentContainer = source.parentContainer;
		}
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
				var $fieldContainer = $(fields[i].fields),
					onChangeCallback = fields[i].onChangeCallback;

				$(this).click(
					(function() {
						return function() {
							hideAll();
							$fieldContainer.removeClass("display-none").addClass("display-block");
							onChangeCallback();
						};
					})()
				);
			});
		}
	},
	
	/* On submit clear fields that not associated with selected court type */
	assignSubmitEvent = function() {
		$(parentContainer).on("submit", function() {
			$(fieldsContainer).each(function() {
				var $this = $(this);
				
				if ($this.hasClass("display-none")) {
					clearField($this);
				}
			});
		});
	},
	
	hideAll = function() {
		$(fieldsContainer).each(function() {
			$(this).removeClass("display-block").addClass("display-none");
		});
	},
	
	/* Selecting target court type clears form fields specified for others court types */ 
	clearFields = function($targetContainer) {
		var i = 0,
			length = fields.length;
		
		for (i; i < length; i += 1) {
			var $fieldContainer = $(fields[i].fields);
			
			$($fieldContainer).each(function() {
				if ($fieldContainer.attr("id") !== $targetContainer.attr("id")) {
					clearField($fieldContainer);
				}
			});
		}
		
	},
	
	
	clearField = function($fieldsContainer) {
		$fieldsContainer.find("input, select").each(function() {
			$(this)
				.val('')
				.removeAttr('checked')
				.removeAttr('selected')
				.trigger("change");
		});
	};
	
	/***** PUBLIC *****/
	
	space.run = function(source) {
		init(source);
		assignSubmitEvent();
		assignButton();
		hideAll();
		showSelectedFields();
	};
	
	return space;
}());
