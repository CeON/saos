
var SearchContext = {
		
		form: "#search-form",
		contextPanelClass: "search-context",
		removeContextClass: "remove-search-context",
		contextFormFieldData: "context-form-field",
		
		init: function() {
			SearchContext.initRemoveContexts();
		},
		
		initRemoveContexts: function() {
			$("." + SearchContext.removeContextClass).click(function() {
				var contextPanel = $(this).closest("." + SearchContext.contextPanelClass);
				var formFieldId = contextPanel.data(SearchContext.contextFormFieldData);
				
				$("#" + formFieldId).val("");
				contextPanel.remove();
				SearchContext.submitForm();
			});
		},
		
		submitForm: function() {
			$(SearchContext.form).submit();
		},
}