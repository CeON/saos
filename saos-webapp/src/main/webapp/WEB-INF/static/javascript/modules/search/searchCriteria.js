/*
 * Module that enables usage of search criteria like: page size and sorting method.
 * 
 * @author Łukasz Pawełczak
 */
var SearchCriteria = {
		
		pageSize: "#searchPageSize",
		sorting: "#searchSorting",
		direction: "#searchSortingDirection",
		form: "#search-form",
		
		init: function() {
			SearchCriteria.assigPageSize();
			SearchCriteria.assignSort();
		},
		
		/* Page size change automatically submits search form. */
		assigPageSize: function() {
			$(SearchCriteria.pageSize).change(function() {
				SearchCriteria.submitForm();
			});
		},
		
		/* Sorting method change automatically submits search form with new criteria. */
		assignSort: function() {
			$(SearchCriteria.sorting + ", " + SearchCriteria.direction).change(function() {
				var $sort = $(SearchCriteria.sorting),
					value = $sort.find(":selected").attr("value").split(",")[0],
					checked = $(SearchCriteria.direction + ":checked").length;
				
				$sort.find(":selected").attr("value", value + "," + (checked > 0 ? "asc" : "desc"));
				SearchCriteria.submitForm();
			});
		},
		
		submitForm: function() {
			$(SearchCriteria.form).submit();
		},
};
