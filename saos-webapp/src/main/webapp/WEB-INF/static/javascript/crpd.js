
/** 
 * Connects the university and basicOrgUnit select elements so they be compatible, i.e. 
 * the possible basicOrgUnits options should depend on the selected university
 * @param findUniversityBasicOrgUnitsUrl an url of a service returning basicOrgUnits for the selected university. Must contain
 * {universityId} path variable, for example theses/search/universities/{universityId}/basicOrgUnits. The {universityId} path variable will be replaced by 
 * the id of the selected university
 * */
function connectUniversityBasicOrgUnitSelects(universitySelectId, basicOrgUnitSelectId, findUniversityBasicOrgUnitsUrl) {


	$('#' + universitySelectId).change(function() {
    		var basicOrgUnitsSelect = $('#' + basicOrgUnitSelectId);
            basicOrgUnitsSelect.empty();
            basicOrgUnitsSelect.attr('disabled','true');
            basicOrgUnitsSelect.after('<input id="basicOrgUnitHidden" type="hidden" name="basicOrgUnit"/>');
            
            $('#basicOrgUnitHidden').val("");
            var universityId = $(this).val();
	    	$.ajax({url: contextPath+'/' + findUniversityBasicOrgUnitsUrl.replace("{universityId}",universityId), 
	            
	    		success: function(basicOrgUnits) {
	            	basicOrgUnitsSelect.empty().append($('<option/>').val('').html(''));
	            	basicOrgUnitsSelect.removeAttr('disabled');
	            	$('#basicOrgUnitHidden').remove();
	                for (var i=0; i<basicOrgUnits.length; i++) {
	                	basicOrgUnitsSelect.append($('<option/>').val(basicOrgUnits[i].id).html(basicOrgUnits[i].name));
	                }
	            }
	    	
	         });
    	});

}

function jAlert(messageText) {
	$("<span class='col-md-7'>" + messageText +" </span>").dialog({ modal: true, width: "500px", buttons: [ { text: "Ok", click: function() { $( this ).dialog( "close" ); } } ] });
}