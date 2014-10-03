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

	init: function() {
		SearchFormMode.assign();
	},

	assign: function() {
		SearchFormMode.getButtonMore().on("click", function() {
			SearchFormMode.show();
	    });
		
		SearchFormMode.getButtonLess().on("click", function() {
			SearchFormMode.hide();
	    });
	},

	show: function() {		
		SearchFormMode.hideButton(SearchFormMode.getButtonMore());
		
		SearchFormMode.getForm().slideDown({easing: SearchFormMode.easing, complete: function() {
			SearchFormMode.showButton(SearchFormMode.getButtonLess());
		} });
	},
	
	hide: function() {
		SearchFormMode.hideButton(SearchFormMode.getButtonLess());

		SearchFormMode.getForm().slideUp({easing: SearchFormMode.easing, complete: function() {
			SearchFormMode.showButton(SearchFormMode.getButtonMore());
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
