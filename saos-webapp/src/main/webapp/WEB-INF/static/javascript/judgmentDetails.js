/**
 * Modules used in judgment details view.
 * 
 * @author Łukasz Pawełczak
 */
var jsInitInJudgmentDetails = function(options) {
	
	
	if (options.pageTitle !== undefined) {
		$(document).attr("title", options.pageTitle + " " + $(document).attr("title"));
	}
		
	InfoBox.init({
	    offsetTop: 61,
	    containerId: "#judgment",
		elements: [{buttonId: "#source-info-nav", sectionId: "#source-info-section", buttonHideId: "#source-info-hide"},
		            {buttonId: "#corrections-nav", sectionId: "#corrections-section", buttonHideId: "#corrections-hide"}]
	});

}

