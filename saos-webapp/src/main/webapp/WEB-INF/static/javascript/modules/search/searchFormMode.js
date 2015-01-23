/*
 * Module created for search form. It enables switching between
 * simple and advance form mode. 
 * 
 * @author Łukasz Pawełczak
 */
var SearchFormMode = {

	buttonMore: "#search-more-fields",
	buttonLess: "#search-less-fields",
	form: "#advance-form",
	
	easing: "linear",

	/* Initialize parameters
	 * @param source configuration parameters(json):
	 * 	- callback - function that is executed after showing/hiding form
	 * 		- onShow - function that is executed when show animation is finished
	 * 		- onHide - function that is executed when hide animation is finished 
	 */
	init: function(source) {
		SearchFormMode.assign(source);
	},

	
	/* Assign function show/hide to button
	 * @param source configuration parameters
	 */
	assign: function(source) {
		SearchFormMode.getButtonMore().on("click", function() {
			SearchFormMode.switchToAdvanceMode(source.callback.onShow);
	    });
		
		SearchFormMode.getButtonLess().on("click", function() {
			SearchFormMode.switchToSimpleMode(source.callback.onHide);
	    });
	},

	/* Switch to advance form mode.
	 * @param callback - function invoked when show animation ends
	 */
	switchToAdvanceMode: function(callback) {	
		
		SearchFormMode.getForm().slideDown({easing: SearchFormMode.easing, complete: function() {
			var $buttonLessParent = SearchFormMode.getButtonLess().parent().parent(),
				$buttonMoreParent = SearchFormMode.getButtonMore().parent().parent();
			
			$buttonMoreParent.css("visibility", "hidden");
			
			$buttonLessParent.css("visibility", "visible")
							 .removeClass("display-none");
			
			callback();
		} });
	},
	
	/* Switch to simple mode form.
	 * @param callback - function invoked when hide animation ends
	 */
	switchToSimpleMode: function(callback) {		
		var $buttonLessParent = SearchFormMode.getButtonLess().parent().parent(),
			$buttonMoreParent = SearchFormMode.getButtonMore().parent().parent();
	
		$buttonMoreParent.css("visibility", "visible");
		
		$buttonLessParent.css("visibility", "hidden")
					 .addClass("display-none");
		
		
		SearchFormMode.getForm().slideUp({easing: SearchFormMode.easing, complete: function() {
			callback();
		} });
	},
	
	getButtonMore: function() {
		return $(SearchFormMode.buttonMore);
	},
	
	getButtonLess: function() {
		return $(SearchFormMode.buttonLess);
	},
	
	getForm: function() {
		return $(SearchFormMode.form);
	},
};
