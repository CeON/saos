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
			SearchCriteria.assignPageSize();
			SearchCriteria.assignSort();
			SearchCriteria.assignTrackingFocus();
		},
		
		/* Page size change automatically submits search form. */
		assignPageSize: function() {
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
		
		/* Assign events for tracking focus on search criteria. */
		assignTrackingFocus: function() {
			$(SearchCriteria.sorting + ", " + SearchCriteria.direction + ", " + SearchCriteria.pageSize).focusin(function() {
				$(SearchCriteria.form).data("focusOn", $(this).attr("id"));
			});
			$(SearchCriteria.sorting + ", " + SearchCriteria.direction + ", " + SearchCriteria.pageSize).focusout(function() {
				$(SearchCriteria.form).data("focusOn", "");
			});
		},
		
		submitForm: function() {
			var $form = $(SearchCriteria.form),
				focusOn = $form.data("focusOn");
			
			if (focusOn) {
				trackFocusInput = $("<input></input>").attr("name", "trackFocusOn").val(focusOn);
				$form.append(trackFocusInput);
			}
			
			$form.submit();
		},
};
