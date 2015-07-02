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
			SearchCriteria.saveInitialValues();
			SearchCriteria.assignPageSize();
			SearchCriteria.assignSort();
			SearchCriteria.assignKeyPress();
			SearchCriteria.assignTrackingFocus();
		},
		
		/* Saves starting pageSize and sorting values in "initial-value" data attribute. */
		saveInitialValues: function() {
			$(SearchCriteria.pageSize).data("initial-value", $(SearchCriteria.pageSize).val());
			$(SearchCriteria.sorting).data("initial-value", $(SearchCriteria.sorting).val());
		},
		

		
		/* Page size change automatically submits search form. */
		assignPageSize: function() {
			$(SearchCriteria.pageSize).change(function(event) {
				
				SearchCriteria.submitForm();
			});
		},
		
		/* Sorting method change automatically submits search form with new criteria. */
		assignSort: function() {
			$(SearchCriteria.sorting + ", " + SearchCriteria.direction).change(function(event) {
				var $sort = $(SearchCriteria.sorting),
					value = $sort.find(":selected").attr("value").split(",")[0],
					checked = $(SearchCriteria.direction + ":checked").length;
				
				$sort.find(":selected").attr("value", value + "," + (checked > 0 ? "asc" : "desc"));
				
				SearchCriteria.submitForm();
			});
		},
		
		/* Assign event for detecting tab pressed when criteria changed (in that situation it should lock focus change) */
		assignKeyPress: function() {
			$(SearchCriteria.sorting + ", " + SearchCriteria.pageSize).keydown(function(event) {
				
				if (event.keyCode == 9) { // Tab
					
					if ($(this).data("initial-value") != $(this).val()) {
						
						if (event.shiftKey) {
							shouldFocusOn = $(":tabbable").eq($(":tabbable").index($(this)) - 1).attr("id");
						} else {
							shouldFocusOn = $(":tabbable").eq($(":tabbable").index($(this)) + 1).attr("id");
						}
						$(SearchCriteria.form).data("focusOn", shouldFocusOn);
						
						
						event.preventDefault();
						$(this).change();
					}
					
					
				}
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
