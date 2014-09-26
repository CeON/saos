/*
 * @author Łukasz Pawełczak
 */
var SearchForm = {

	buttonMore: "#search-more-fields",
	buttonLess: "#search-less-fields",
	form: "#advance-form",

	init: function() {
		SearchForm.assign();
	},

	assign: function() {
		SearchForm.getButtonMore().on("click", function() {
			SearchForm.show();
	    });
		
		SearchForm.getButtonLess().on("click", function() {
			SearchForm.hide();
	    });
	},

	show: function() {		
		SearchForm.hideButton(SearchForm.getButtonMore());
		
		SearchForm.getForm().slideDown(function() {
			SearchForm.showButton(SearchForm.getButtonLess());
		});
	},
	
	hide: function() {
		SearchForm.hideButton(SearchForm.getButtonLess());

		SearchForm.getForm().slideUp(function() {
			SearchForm.showButton(SearchForm.getButtonMore());
		});
	},
	
	getButtonMore: function() {
		return $(SearchForm.buttonMore);
	},
	
	getButtonLess: function() {
		return $(SearchForm.buttonLess);
	},
	
	getForm: function() {
		return $(SearchForm.form);
	},
	
	showButton: function($button) {
		$button.parent().parent().css("display", "block");
	},
	
	hideButton: function($button) {
		$button.parent().parent().css("display", "none");
	},
};
