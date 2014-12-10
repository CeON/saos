/* 
 * Add option of selecting search filter by clicking on property displayed in judgment results list. 
 * 
 * Module requires:
 * - bootstrap tooltip
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
		
		removeButtonBaseShape = "<span></span>",
		
		fieldGroups = [{filterField: "", container: ""}],
		
		filters = [{button: "", searchfield: "", filterfield: "", selectFormType: ""}],
		
		advanceFilter = [{button: "", searchfield: "", filterfield: "", selectFormType: "", url : "",
						parent : {button: "", searchfield: "", filterfield: ""}}],
		
		
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
		
		if (source.fieldGroups !== "") {
			fieldGroups = source.fieldGroups;
		}
		
		if (source.filters !== "") {
			filters = source.filters;
		}
		
		if (source.advanceFilter !== "") {
			advanceFilter = source.advanceFilter;
		}
	},
	
	/* Add filters to fields that correspond to input, select and checkbox */
	assignAddFilter = function() {
		var i = 0,
			length = filters.length;
		
		for (i; i < length; i += 1) {
			$(parentContainer + " " + filters[i].button).each(function() {
				var $thisButton = $(this),
					filterValue = $thisButton.text().trim(),
					$searchFormField = $(filters[i].searchfield),
					$selectFormType = $(filters[i].selectFormType), 
					$filterField = $("#" + filters[i].filterfield);
								 
				$thisButton.click(
						(function() {
							if ($searchFormField.is("input[type='checkbox']")) {
								return function() {
									$searchFormField.each(function() {
										var $this = $(this);
										
										filterValue = $thisButton.attr(dataJudgmentType);

										if($this.val() === filterValue) {
											selectFormType($selectFormType);
											$this.prop('checked', true);
											$this.trigger("change");
											submitForm(); //send form only if checkbox value has changed
										}
									});
								};
							} else if($searchFormField.is("input")) {
								return function() {
									selectFormType($selectFormType);
									$searchFormField.val(filterValue);
									submitForm();
								};
							} else if($searchFormField.is("select")) {
								return function() {
									selectFormType($selectFormType);
									
									$searchFormField.find("option").each(function() {
										var $this = $(this);
										$this.attr("data-content", ($this.attr("data-content")+"").trim());
									});
									$searchFormField.find("option[data-content='" + filterValue + "']").attr('selected', 'selected');
									$searchFormField.trigger("change");
									submitForm();
								};
							}
						}())
				);

			});
		}
	},
	
	/* If filter field is assigned to specified court type,
	 * selecting filter process must also select corresponding court type.
	 */
	selectFormType = function($selectFormType, triggerChange) {
		if ($selectFormType.length > 0) {
			
			if ($selectFormType.is(":checked") !== true) {
				$("." + $selectFormType.attr("name")).each(function() {
					$(this).removeAttr('checked');
				});
	
				$selectFormType
					.prop('checked', true)
					.trigger("click");
				
				if (!(triggerChange !== undefined && triggerChange === false)) {
					$selectFormType.trigger('change');
				}
			} 
		}
	},
	
	/* Division select. Selecting division as filter start the process of first selecting corresponding court and then selecting division.
	 * Selecting divisions consist of getting list of all divisions that are under target court, finds selected division by name,
	 * and acquire selected division id. After that clear division select tag from all options and add one option with selected division. 
	 * Process ends with submitting form.*/
	assignAddFilterDivision = function() {
		var j = 0,
			advFilterLength = advanceFilter.length;
		
		for (j; j < advFilterLength; j += 1) {
			$(parentContainer + " " + advanceFilter[j].button).each(function() {
				var $thisButton = $(this),
					$selectFormType = $(advanceFilter[j].selectFormType),
					parentButton = advanceFilter[j].parent.button,
					parentSearchField = advanceFilter[j].parent.searchfield,
					searchField = advanceFilter[j].searchfield,
					filterUrl = advanceFilter[j].url,
					getUrl = advanceFilter[j].getUrl,
					divisionName = $thisButton.text();
				
				$thisButton.click(
					(function() {
						return function() {
							var selectedCourt = $thisButton.parent().find(parentButton).text().trim(),
								selectedCourtId = "";
								
							selectFormType($selectFormType, false);
							selectedCourtId = $(parentSearchField)
												.find("option[data-content='" + selectedCourt + "']")
												.attr('selected', 'selected')
												.val();
							
							$(searchField)
								.removeAttr("disabled")
								.removeAttr("selected");
			
							$.ajax(getUrl(selectedCourtId))
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
									 $(searchField).prepend($("<option selected='selected' value='" + id + "' ></option>"));
								 }
								 
								 submitForm();
							 })
							 .fail(function() {});
						}
					})()
				);
			});
		}
	},
	
	/* Create buttons for target filter, clicking on it removes filter */
	createRemoveFilterButtons = function() {
		$("." + filterItemClass).each(function() {
			var $filterItem = $(this);
			
			if ($filterItem.text().trim() !== "") {
				var $removeFilterButton = $(removeButtonBaseShape),
					assignedFieldValue = $filterItem.attr(assignedField),
					$assignedFieldId = $("#" + assignedFieldValue);
				
				if ($assignedFieldId.is("select")) {
					var selectedItemValue = $assignedFieldId.find("[value='" + $filterItem.text().trim() + "']").text();
					
					$filterItem.find("div").text(selectedItemValue);
				}
				
				$removeFilterButton.click(function() {
					clearFilterGroup(assignedFieldValue);
					clearField($assignedFieldId);
					$filterItem.remove();
					submitForm();
				})
				.addClass(removeButtonClass)
				.attr("data-toggle", "tooltip")
				.attr("data-placement", "right")
				.attr("title", $filterItem.find(" > div").attr("data-tooltip-text"))
				.tooltip();
				
				$filterItem.append($removeFilterButton);
			}
		});
	},
	
	/* Clear group of fields e.g. common-court-fields */
	clearFilterGroup = function(fieldId) {
		var i = 0,
			length = fieldGroups.length;
		
		for (i; i < length; i += 1) {
			if (fieldId === fieldGroups[i].filterField) {
				clearFieldsInContainer($(fieldGroups[i].container));
			}
		}
	},
	
	/* Clear target field e.g. input select */
	clearField = function($field) {
		
		if ($field.is("input:checkbox")) {
			$field.prop("checked", false);
		} else if ($field.is("input")) {
			$field.attr("value", "");
		} else if ($field.is("select")) {
			$field.removeAttr('selected')
				  .val("")
				  .trigger("change");
		}
	},
	
	clearFieldsInContainer = function($container) {
		$container.find("input, select").each(function() {
			clearField($(this));
		});
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
