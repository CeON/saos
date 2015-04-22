/*
 * This module connects two fields (court & division) i search form. When user selects
 * court, fields in division select are generated.
 * 
 * @author Łukasz Pawełczak
 */
var CourtDivisionSelect = (function() {
	
	var space = {},
		fields = [{court: "", divisionId: "", divisionUrl: ""}],
		
	
	init = function(source) {
		
		if (source.fields !== "" && source.fields !== undefined ) {
			fields = source.fields;
		}
		
	}, 
	
	assignChangeEvent = function() {
		
		for(var i = 0, length = fields.length; i < length; i += 1) {
			$(fields[i].court).each(function() {
				var $court = $(this),
					$divisionId = $(fields[i].divisionId),
					getDivisionUrl = fields[i].getDivisionUrl;
				
				$court.change((function() {
					/* Get divisions by court id and fill element 'select' divisions with received items */
					return function() {
						var selectedCourtId = $court.find("option:selected").attr("value");
						
						if (selectedCourtId !== "") {
							$.ajax(getDivisionUrl(selectedCourtId))
							 .done(function(data) {
								 var options = "";
								 
								 for(var j = 0, dataLength = data.length; j < dataLength; j += 1) {
									 options += prepareOption(data[j].id, data[j].name);
								 }
								 
								 $divisionId.removeAttr("disabled").append(options);
							 })
							 .fail(function() {});
						} else {
							$divisionId.empty().attr("disabled", "disabled");
						}
					};
					
				}()));
			});
		}
	},
	
	prepareOption = function(id, name) { 
		return "<option data-content='" + name + "' value='" + id + "' >" + name + "</option>";
	};
	
	space.run = function(source) {
		init(source);
		assignChangeEvent();
	};
	
	return space;
}());
