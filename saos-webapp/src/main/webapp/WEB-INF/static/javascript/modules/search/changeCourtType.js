/*
 * Module for switching between different parts of search form, based on court type.
 * E.g. switch from common court to supreme court.
 * 
 * @author Łukasz Pawełczak
 */
var ChangeCourtType = (function() {
	
	var space = {},
		EVENT_SHOW_CONTAINER = "showContainer",
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
	
	/* Bind fields related to court type with function, that on click event
	 * displays container with specified court type fields.
	 */
	assignShowContainerMethod = function() {
		var length = fields.length;
	
		
		/* All fields specified by name {radioName} are bound to a function,
		 * that reacts on click event. This method hides all containers
		 * and triggers event to show one container related to courtType.
		 */
		$("input[name='" + radioName + "']").each(function() {
			$(this).click(function() {
				hideAll();
				$(this).trigger(EVENT_SHOW_CONTAINER); //triggers event to display related container
			});
		});
		
		/* Fields specified in object {fields.button} are bound to a function,
		 * that is triggered by event {EVENT_SHOW_CONTAINER}. When this event
		 * occurs, related container is displayed.
		 */
		for (var i = 0; i < length; i += 1) {
			$(parentContainer + " " + fields[i].button).each(function() {
				var $fieldContainer = $(fields[i].fields),
					onChangeCallback = fields[i].onChangeCallback;

				$(this).on(EVENT_SHOW_CONTAINER, function() {
					$fieldContainer.removeClass("display-none").addClass("display-block");
					onChangeCallback();
				});
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
		var length = fields.length;
		
		for (var i = 0; i < length; i += 1) {
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
	
	//------------------------ PUBLIC --------------------------
	
	space.run = function(source) {
		init(source);
		assignSubmitEvent();
		assignShowContainerMethod();
		hideAll();
		showSelectedFields();
	};
	
	return space;
}());
