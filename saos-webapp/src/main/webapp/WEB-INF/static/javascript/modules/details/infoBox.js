/*
 * Info box module.
 * 
 * @author Łukasz Pawełczak
 */
var InfoBox = (function() {
	
	var space = {},
		offsetTop = 0,
	    containerId = "",
	
		elements = [{buttonId: "", sectionId: "", buttonHideId: ""}],
		
		animationInProgress = false,
		
		openedSectionId = "",
		
	/* Assigns on click event to buttons specified in elements.
	 * Clicking on button should make corresponding section visible.
	 * If another section is already displayed, it will be hidden.
	 */
	assignToggleOnButtonClick = function() {
		
		for(var i = 0, length = elements.length; i < length; i += 1) {
			
			//Closure for storing elements[i].sectionId 
			(function() {
				
				//variable created for better performance
				var sectionId = elements[i].sectionId;
			
				$(elements[i].buttonId).click(function(event) {
					
					event.preventDefault();
					
					if (openedSectionId === sectionId) {
						hideSection(sectionId);
					} else {
						
						if (openedSectionId !== "") {
							hideSection(openedSectionId, function () {
								showSection(sectionId);
							});
						} else {
							showSection(sectionId);
						}
	
					}
					
				});
				
			})();
			
			$(elements[i].buttonHideId).click(function() {
				hideSection(openedSectionId);
			});
		
		}
	},
	
	/* Runs animation that makes section visible.
	 * 
	 * @param sectionId - section
	 */
	showSection = function(sectionId) {

		if (animationInProgress === false) {
			animationInProgress = true;
			
			var $sectionId = $(sectionId),
			    $containerId = $(containerId),
			    containerHeight = $containerId.outerHeight(),
			    sectionHeight = 0;
			
			$sectionId.css("display", "block");
			
			sectionHeight = $sectionId.outerHeight()
			
			if (containerHeight < sectionHeight + offsetTop) {
			    $containerId.animate({height: sectionHeight + offsetTop + 20 + "px"}, 400);
			}
			
			$(sectionId).animate({opacity: 1, right: "0px"}, 400, function() {
					openedSectionId = sectionId;
					animationInProgress = false;
				});
		}
	},
	
	/* Runs animation that hides section.
	 * 
	 * @param sectionId - section
	 * @param callback - function that is invoked when animation ends
	 */
	hideSection = function(sectionId, callback) {
		
		if (animationInProgress === false) {
			animationInProgress = true;
					
			$(sectionId)
				.animate({opacity: 0, right: "-999px"}, 400, function() {
					$(this).css("display", "none");
					openedSectionId = "";
					
					animationInProgress = false;
					
					if (callback) {
						callback();
					}
				});
		}	
	};
	
	//------------------------ PUBLIC --------------------------
	
	space.init = function(options) {
		
	    offsetTop = options.offsetTop;
	    
	    containerId = options.containerId;
	    
		elements = options.elements;
		
		assignToggleOnButtonClick();
	}
	
	return space;
	
})();


