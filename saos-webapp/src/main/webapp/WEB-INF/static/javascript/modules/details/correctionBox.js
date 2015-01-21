/*
 * Correction box display
 * 
 * @author Łukasz Pawełczak
 */
var CorrectionBox = (function() {

	var space = {},
	
		ANIMATION_TIME = 600,
	
	/* Assign on click event to button.
	 * Clicking on $button should show correction box. 
	 * 
	 * @param $button 
	 * @param options - config json:
	 * 			- boxId - id of correction box
	 */
	assign = function($button, options) {
		
		$button.click(function() {
			show(options);
		});
	},
	
	
	/* Show correction box
	 * @param options.boxId - id of correction box
	 */
	show = function(options) {
		$("#" + options.boxId).slideDown(ANIMATION_TIME, function() {});
	};
	
	
	//------------------------ PUBLIC --------------------------
	
	space.assign = function($button, options) {
		assign($button, options);
	};
	
	return space;
})();


$.fn.showCorrectionBox = function(options) {
	CorrectionBox.assign($(this), options);
}

