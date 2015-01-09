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
	 * 	- callback - function that is exetued after showing/hiding form
	 * 		- onShow - function that is excuted when show animation is finished
	 * 		- onHide - function that is excuted when hide animation is finished 
	 */
	init: function(source) {
		SearchFormMode.assign(source);
	},

	
	/* Assign function show/hide to button
	 * @param source configuration parameters
	 */
	assign: function(source) {
		SearchFormMode.getButtonMore().on("click", function() {
			SearchFormMode.show(source.callback.onShow);
	    });
		
		SearchFormMode.getButtonLess().on("click", function() {
			SearchFormMode.hide(source.callback.onHide);
	    });
	},

	/* Show advanced mode of form.
	 * @param callback - function invoked when show animation ends
	 */
	show: function(callback) {	
		
		SearchFormMode.getForm().slideDown({easing: SearchFormMode.easing, complete: function() {
			SearchFormMode.showButton(SearchFormMode.getButtonLess());
			callback();
		} });
	},
	
	/* Hide advanced mode of form.
	 * @param callback - function invoked when hide animation ends
	 */
	hide: function(callback) {
		SearchFormMode.hideButton(SearchFormMode.getButtonLess());

		
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
	
	showButton: function($button) {
		$button.parent().parent().css("display", "block");
	},
	
	hideButton: function($button) {
		$button.parent().parent().css("display", "none");
	},
};
