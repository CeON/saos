/*
 * Filter box features
 * 
 * @author Łukasz Pawełczak
 */
var FilterBox = (function() {

	var space = {},
	
	MAXLETTERS = 30,
	dataFullText = "data-full-text",
	tooltipClass = "long-text",
	
	$parent = "",
	$removeAllButton = "",
	$noFiltersMessage= "",
	filterField = "",
	
	init = function($this, source) {
		
		
		if ($this !== "" && $this !== undefined) {
			$parent = $this;
		}
		
		if (source.removeAllButton !== "" && source.removeAllButton !== undefined) {
			$removeAllButton = $(source.removeAllButton);
		}
		
		if (source.noFiltersMessage !== "" && source.noFiltersMessage !== undefined) {
			$noFiltersMessage = $(source.noFiltersMessage);
		}
		
		
		if (source.filterField !== "" && source.filterField !== undefined) {
			filterField = source.filterField;
		}
		
		if (source.maxLetters !== "" && source.maxLetters !== undefined) {
			MAXLETTERS = source.maxLetters;
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
				text = $this.find("div").text();
			
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
					.text(text.substr(0, MAXLETTERS) + "...");
			}
		});
	};
	
	
	/***** PUBLIC *****/
	
	space.run = function($this, source) {
		init($this, source);
		showButtonAndMessage();
		shortenFilterText();
	};
	
	return space;
}());

$.fn.filterBox = function(source) {
	FilterBox.run($(this), source);
};
