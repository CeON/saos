/* 
 * Add option of selecting search filter by clicking on property displayed in judgment results list. 
 * 
 * @author Łukasz Pawełczak
 */
var SearchFilters = (function(){
	
	var space = {},
		form = "",
		removeAllFiltersButton = "",
		
		filterItemClass = "filter-item",
		removeButtonClass = "remove-button",
		assignedField = "data-assigned-field",
		dataJudgmentType = "data-judgment-type",
		
		parentContainer = ".judgment-list",
		
		removeButtonBaseShape = "<span>X</span>",
		
		filters = [{button: "", searchfield: "", filterfield: ""}],
				   
		
		advanceFilter = {button: "", searchfield: "", filterfield: "", url : "",
						parent : {button: "", searchfield: "", filterfield: ""}},
		
		
	/***** PRIVATE METHODS *****/	
		
	init = function(source) {
		
		if (source.formId !== "") {
			form = source.formId;
		}
		
		if (source.divisionsUrl !== "") {
			advanceFilterUrl = source.advanceFilterUrl;
		}
		
		if (source.removeAll !== "") {
			removeAllFiltersButton = source.removeAll;
		}
		
		if (source.parentContainer !== "") {
			parentContainer = source.parentContainer;
		}
		
		if (source.filters !== "") {
			filters = source.filters;
		}
		
		if (source.advanceFilter !== "") {
			advanceFilter = source.advanceFilter;
		}
	},
	
	
	/* Add filters to basic fields that corresponds to input, select and checkbox */
	assignAddFilter = function() {
		var i = 0,
			length = filters.length;
		
		for (i; i < length; i += 1) {
			$(parentContainer + " " + filters[i].button).each(function() {
				var $thisButton = $(this),
					filterValue = $thisButton.text(),
					$searchFormField = $(filters[i].searchfield),
					$filterField = $("#" + filters[i].filterfield);
				 
				$thisButton.click(
						(function() {
							if ($searchFormField.is("input[type='checkbox']")) {
								return function() {
									$searchFormField.each(function() {
										var $this = $(this);
										
										filterValue = $thisButton.attr(dataJudgmentType);

										if($this.val() === filterValue) {
											$this.prop('checked', true);
											$this.trigger("change");
											
											submitForm(); //send form only if checkbox has changed value
										}
									});
								};
							} else if($searchFormField.is("input")) {
								return function() {
									$searchFormField.val(filterValue);
									//$filterField.text(filterValue);
									submitForm();
								};
							} else if($searchFormField.is("select")) {
								return function() {
									$searchFormField.find("option[content='" + filterValue + "']").attr('selected', 'selected');
									$searchFormField.trigger("change");
									//$filterField.text(filterValue);
									submitForm();
								};
							}
						}())
				);

			});
		}
	},
	
	/* Division select. Selecting division as filter start the process of first selecting corresponding court and then selecting division.
	 * Selecting divisions consist of getting list of all divisions that are under target court, finds selected division by name,
	 * and acquire selected division id. After that clear division select tag from all options and add one option with selected division. 
	 * Process ends with submitting form.*/
	assignAddFilterDivision = function() {
		
		$(parentContainer + " " + advanceFilter.button).each(function() {
			var $thisButton = $(this),
				divisionName = $thisButton.text();
			
			$thisButton.click(function() {
				var selectedCourt = $thisButton.parent().find(advanceFilter.parent.button).text(),
					selectedCourtId = "";
					
				
				clearField($(advanceFilter.parent.searchfield));
				selectedCourtId = $(advanceFilter.parent.searchfield).find("option[content='" + selectedCourt + "']")
													.attr('selected', 'selected')
													.val();
				$(advanceFilter.searchfield).removeAttr("disabled").removeAttr("selected");

				$.ajax(advanceFilter.url + selectedCourtId)
				 .done(function(data) {
					 var i = 0,
					 	 length = data.length,
					 	 id = 0;
					 
					 for(i; i < length; i += 1) {
						 if (divisionName === data[i].name) {
							 id = data[i].id;
							 break;
						 }
					 }
					 
					 if (id !== 0) {
						 $(advanceFilter.searchfield).prepend($("<option selected='selected' value='" + id + "' ></option>"));
					 }
					 
					 submitForm();
				 })
				 .fail(function() {});
				
			});
		});
	},

	
	/* Create buttons for target filter, clicking on it removes filter */
	createRemoveFilterButtons = function() {
		$("." + filterItemClass).each(function() {
			var $filterItem = $(this);
			
			if ($filterItem.text().trim() !== "") {
				var $removeFilterButton = $(removeButtonBaseShape),
					$assignedFieldId = $("#" + $filterItem.attr(assignedField));
				
				if ($assignedFieldId.is("select")) {
					var selectedItemValue = $assignedFieldId.find("[value='" + $filterItem.text().trim() + "']").text();
					
					$filterItem.text(selectedItemValue);
				}
				
				$removeFilterButton.click(function() {
					clearField($assignedFieldId);
					$filterItem.remove();
					submitForm();
				})
				.addClass(removeButtonClass);
				
				$filterItem.append($removeFilterButton);
			}
		});
	},
	
	clearField = function($field) {
		if ($field.is("input")) {
			$field.attr("value", "");
		} else if ($field.is("select")) {
			$field.removeAttr('selected')
				  .val("")
				  .trigger("change");
		}
	},
	
	
	createRemoveFilterButton = function() {
		
	},
	
	/* Button to remove all previous selected filters */
	assignButtonRemoveAllFilters = function() {
		var $removeAllButton = $(removeAllFiltersButton);
			
		$removeAllButton.click(function() {
			$(form).clearForm();
			submitForm();	
		});
	}
	
	changeMouseCursorToLoading = function() {
		$("body").css("cursor", "wait");
	},
	
	submitForm = function() {
		changeMouseCursorToLoading();
		$(form + "").submit();
	};
	
	/***** PUBLIC *****/
	
	space.run = function(source) {
		init(source);
		assignAddFilter();
		assignAddFilterDivision();
		assignButtonRemoveAllFilters();
		createRemoveFilterButtons();
	};
	
	return space;
}());
