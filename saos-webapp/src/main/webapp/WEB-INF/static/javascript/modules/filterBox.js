/*
 * Filter box features
 * 
 * @author Łukasz Pawełczak
 */
var FilterBox = (function() {

	var space = {},
	
	cookieName = "saos-filterbox-show",
	cookieTime = 30, //minutes
	
	stickyOptions = {enabled: true, topSpacing: 0},
	
	MAXLETTERS = 30,
	dataFullText = "data-full-text",
	tooltipClass = "long-text",
	
	$parent = "",
	$removeAllButton = "",
	$noFiltersMessage= "",
	filterField = "",
	
	resultList = "",
	buttonHide = "",
	settingsButton = "filter-box-button",
	animationElementClass = "fbox-button-before",
	
	widthBase = "75%",
	widthMax = "100%",
	
	init = function($this, source) {
		
		
		if ($this !== "" && $this !== undefined) {
			$parent = $this;
		}
		
		if (source.stickyOptions !== "" && source.sticky !== undefined) {
			stickyOptions = source.stickyOptions;
		}
		
		if (source.removeAllButton !== "" && source.removeAllButton !== undefined) {
			$removeAllButton = $(source.removeAllButton);
		}
		
		if (source.noFiltersMessage !== "" && source.noFiltersMessage !== undefined) {
			$noFiltersMessage = $(source.noFiltersMessage);
		}
		
		if (source.buttonHide !== "" && source.buttonHide !== undefined) {
			buttonHide = (source.buttonHide);
		}
		
		if (source.resultList !== "" && source.resultList !== undefined) {
			resultList = source.resultList;
		}
		
		if (source.filterField !== "" && source.filterField !== undefined) {
			filterField = source.filterField;
		}
		
		if (source.maxLetters !== "" && source.maxLetters !== undefined) {
			MAXLETTERS = source.maxLetters;
		}

	},
	
	
	
	/* Make box sticky */
	stickyBox = function() {
		if (stickyOptions.enabled === true) {
			var top = 30;
			if (stickyOptions.topSpacing !== undefined) {
				top = stickyOptions.topSpacing;
			}
			$parent.sticky({ topSpacing: top });
		}
	},
	
	/* If filter box contains at least one filter,
	 * show button "remove all filters" and hide message "no filters". */
	showButtonAndMessage = function() {
		if ($parent.find(filterField).length !== 0) {
			$removeAllButton.css("display", "inline-block");
			$noFiltersMessage.css("display","none");
		} 
	},
	
	/* Shorten to long filter text */
	shortenFilterText = function() {
		$parent.find(filterField).each(function() {
			var $this = $(this),
				text = $this.find("div").text().trim();
			
			if (text.length > MAXLETTERS) {
				$this
					.attr(dataFullText, text)
					.mouseenter(function() {
						var $fullTextItem = $("<div></div>");
						
						$fullTextItem
							.addClass(tooltipClass)
							.text($this.attr(dataFullText));
						
						$this.append($fullTextItem);
					})
					.mouseleave(function() {
						$this
							.find("." + tooltipClass)
							.remove();
					})
					.find("div")
					.text(text.substr(0, MAXLETTERS) + (text.length > MAXLETTERS + 1 ? "..." : ""));
			}
		});
	},
	
	assignHideButton = function() {
		$parent.find(buttonHide).click(function() {
			hideBox();
		});
	},
	
	hideBox = function() {
		saveCookie("false");
		$parent.slideUp(function() {
			createFilterShowButton();
			$(resultList).animate({width: widthMax}, 400);
		});
	},
	
	showBox = function() {
		var fBoxSlideDown = function() {
			$parent.slideDown();
		}
		
		saveCookie("true");
		$(resultList).animate({width: widthBase}, 400, function() {});
		hideFilterButton(fBoxSlideDown);
	},
	
	
	/* Create button "show filter box" */
	createFilterShowButton = function() {
		if ($("#" + settingsButton).length === 0) {
			var $button = $("<div></div>"); 
			
			$button
				.attr("id", settingsButton)
				.addClass(settingsButton)
				.click(function() {
					showBox();
				});
			
			$("#search-settings").before($button);
		} 
		
		showFilterButton();
	},
	
	/* Animation show filter button */
	showFilterButton = function() {
		var $button = $("#" + settingsButton),
			$animationElement = $("<div ></div>");
		
		$animationElement.addClass(animationElementClass);
		
		$("#" + settingsButton)
			.prepend($animationElement)
			.css({opacity: 1});
		
		$button.find("." + animationElementClass).animate({opacity: 0}, 400, function() {
			$(this).remove();
		});
	},
	
	/* Animation hide filter button */
	hideFilterButton = function(callBack) {
		var $button = $("#" + settingsButton),
		$animationElement = $("<div ></div>");
	
		$animationElement
			.addClass(animationElementClass)
			.css({opacity: 0});
		
		$("#" + settingsButton).prepend($animationElement);
		$button.find("." + animationElementClass).animate({opacity: 1}, 400, function() {
			$("#" + settingsButton).css({opacity: 0});
			$(this).remove();
			callBack();
		});
	},
	
	dontShowBox = function() {
		if (readCookie() === "false") {
			$parent.css({display: "none"});
			$(resultList).css({width: widthMax});
			createFilterShowButton();
		}
	},
	
	saveCookie = function(value) {
		Cookies.create(cookieName, value, cookieTime);
	},
	
	readCookie = function(value) {
		return Cookies.read(cookieName);
	};
	
	
	/***** PUBLIC *****/
	
	space.run = function($this, source) {
		init($this, source);
		dontShowBox();
		stickyBox();
		showButtonAndMessage();
		assignHideButton();
		shortenFilterText();
	};
	
	return space;
}());

$.fn.filterBox = function(source) {
	FilterBox.run($(this), source);
};
