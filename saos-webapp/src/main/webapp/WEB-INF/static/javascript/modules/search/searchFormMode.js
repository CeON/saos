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
			SearchFormMode.showButton(SearchFormMode.getButtonLess(), true);
			SearchFormMode.hideButton(SearchFormMode.getButtonMore(), false);
			callback();
		} });
	},
	
	/* Hide advanced mode of form.
	 * @param callback - function invoked when hide animation ends
	 */
	hide: function(callback) {
		SearchFormMode.hideButton(SearchFormMode.getButtonLess(), true);
		SearchFormMode.showButton(SearchFormMode.getButtonMore(), false);
		
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
	
	/* Show button
	 * 
	 * @param $button - (jquery object) button
	 * @param removeDisplayNone - (boolean) on true remove css class "display-none" 
	 */
	showButton: function($button, removeDisplayNone) {
		var $parent = $button.parent().parent();
		
		$parent.css("visibility", "visible");
		
		if (removeDisplayNone) {
			$parent.removeClass("display-none");
		}
	},
	
	/* Hide button
	 * 
	 * @param $button - (jquery object) button
	 * @param addDisplayNone - (boolean) on true add css class "display-none"
	 */
	hideButton: function($button, addDisplayNone) {
		var $parent = $button.parent().parent();
		
		$parent.css("visibility", "hidden");
		
		if (addDisplayNone) {
			$parent.addClass("display-none");
		}
	}
};
