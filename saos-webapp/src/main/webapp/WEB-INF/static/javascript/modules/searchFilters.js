/*
 * Module that enables usage of search filters like: page size and sorting method.
 * 
 * @author Łukasz Pawełczak
 */
var SearchFilters = {
		
		pageSize: "#searchPageSize",
		sorting: "#searchSorting",
		direction: "#searchSortingDirection",
		form: "#searchForm",
		
		init: function() {
			SearchFilters.assigPageSize();
			SearchFilters.assignSort();
		},
		
		/* Page size change automatically submits search form. */
		assigPageSize: function() {
			$(SearchFilters.pageSize).change(function() {
				SearchFilters.submitForm();
			});
		},
		
		/* Sorting method change automatically submits search form with new criteria. */
		assignSort: function() {
			$(SearchFilters.sorting + ", " + SearchFilters.direction).change(function() {
				var $sort = $(SearchFilters.sorting),
					value = $sort.find(":selected").attr("value"),
					checked = $(SearchFilters.direction + ":checked").length;
				
				$sort.find(":selected").attr("value", value + "," + (checked > 0 ? "asc" : "desc"));
				SearchFilters.submitForm();
			});
		},
		
		submitForm: function() {
			$(SearchFilters.form).submit();
		},
};
