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

	init: function(source) {
		SearchFormMode.assign(source);
	},

	assign: function(source) {
		SearchFormMode.getButtonMore().on("click", function() {
			SearchFormMode.show(source.callback.onShow);
	    });
		
		SearchFormMode.getButtonLess().on("click", function() {
			SearchFormMode.hide(source.callback.onHide);
	    });
	},

	show: function(callback) {	
		SearchFormMode.hideButton(SearchFormMode.getButtonMore());
		
		SearchFormMode.getForm().slideDown({easing: SearchFormMode.easing, complete: function() {
			SearchFormMode.showButton(SearchFormMode.getButtonLess());
			callback();
		} });
	},
	
	hide: function(callback) {
		SearchFormMode.hideButton(SearchFormMode.getButtonLess());

		SearchFormMode.getForm().slideUp({easing: SearchFormMode.easing, complete: function() {
			SearchFormMode.showButton(SearchFormMode.getButtonMore());
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
