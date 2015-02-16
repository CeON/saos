/**
 * Modules used in judgment details view.
 * 
 * @author Łukasz Pawełczak
 */
var jsInitInJudgmentDetails = function(options) {
	
	
	if (options.pageTitle !== undefined) {
		$(document).attr("title", options.pageTitle + " " + $(document).attr("title"));
	}
	
	$("#show-correction-box").showCorrectionBox({
		boxId: "corrections"
	});
}

