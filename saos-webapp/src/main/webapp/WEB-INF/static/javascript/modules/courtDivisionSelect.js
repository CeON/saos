/*
 * This module connects two fields (court & division) i search form. When user selects
 * court, fields in division select are generated.
 * 
 * @author Łukasz Pawełczak
 */
var CourtDivisionSelect = (function() {
	
	var space = {},
		$court = "",
		divisionId = "",
		divisionUrl = "",
	
	init = function($this, source) {
		
		if ($this !== "") {
			$court = $this;
		}
		
		if (source.divisionId !== "") {
			divisionId = "#" + source.divisionId;
		}
		
		if (source.divisionsUrl !== "") {
			divisionUrl = source.divisionUrl;
		}
		
	}, 
	
	assignChangeEvent = function() {
		$court.change(function() {
			changeCourt();
		});
	},
	
	/* Get divisions by court id and fill element 'select' divisions with received items */
	changeCourt = function() {
		var selectedDivision = $court.find("option:selected").attr("value");
		
		if (selectedDivision !== "") {
			$.ajax(divisionUrl + selectedDivision)
			 .done(function(data) {
				 var options = prepareOption("", ""),
				 	i = 0,
				 	length = data.length;
				 
				 for(i; i < length; i += 1) {
					 options += prepareOption(data[i].id, data[i].name);
				 }
				 
				 $(divisionId).empty().removeAttr("disabled").prepend(options);
			 })
			 .fail(function() {});
		} else {
			$(divisionId).empty().attr("disabled", "disabled");
		}
	},
	
	prepareOption = function(id, name) {
		return "<option value='" + id + "' >" + name + "</option>";
	};
	
	space.run = function($this, source) {
		init($this, source);
		assignChangeEvent();
	};
	
	return space;
}());

$.fn.courtDivisionSelect = function(source) {
	CourtDivisionSelect.run($(this), source);
};
