/*
 * Module for displaying settings tooltip box. Clicking on $button show/hide $box.
 * 
 * @author Łukasz Pawełczak
 */
var SearchSettingsToolTip = (function() {
	
	/***** PRIVATE *****/
	
	var space = {},
		$button = "",
		$box = "",
	
	init = function($this, source) {
		
		if ($this !== "") {
			$button = $this;
		}
		
		if (source.box !== "") {
			$box = $("#" + source.box);
		}
		
	},
	
	assignButton = function() {
		$button.click(function() {
			toggleBox();
		});
	},
	
	toggleBox = function() {
		$box.toggle("slow", function() {});
	};
	
	/***** PUBLIC *****/
	
	space.run = function($this, source) {
		init($this, source);
		assignButton();
	};
	
	return space;
}());

$.fn.searchSettingsToolTip = function(source) {
	SearchSettingsToolTip.run($(this), source);
};
