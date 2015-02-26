/*
 * 
 * 
 * @author Łukasz Pawełczak
 */

var SelectLawJournalEntry = (function() {
	
	
	var space = {},
		options = {},
		defaults = {
			PAGE_SIZE: 10, 
			SELECTED_CLASS : "selected-law"
		},
		pageNumber = 0,
		lastPageNumber = 0,
		lastUrl = "",
	
	
	/*
	 * 
	 * Field container events:
	 * - hide - make container not visible,
	 * - show - make container visible
	 * 
	 * Buttons container events:
	 * - setSelected @param(text) - adds selected law journal with text
	 * - removeSelected - hides selected law journal
	 * 
	 * Field law journal id events:
	 * - setValue @param(value) - sets value of field options.fieldLawJournalId
	 */
		
	assignButtonsContainerEventHandlers = function() {
		
		$(options.buttonsContainer)
			.on("setSelected", function(event, selectedText) {
				var $selectedElement = $("<div></div>"),
					$removeButton = $("<a></a>");
				
				$removeButton
					.attr("href", "")
					.addClass("remove")
					.click(function(event) {
						event.preventDefault();
						
						$(options.buttonsContainer).trigger("removeSelected");
					});
				
				$selectedElement
					.text(selectedText)
					.addClass(defaults.SELECTED_CLASS)
					.append($removeButton);
				
				$(options.setButton)
					.addClass("display-none")
					.before($selectedElement);
				
			})
			.on("removeSelected", function() {
				
				$(options.fieldLawJournalId).val("");
				$(this).find("." + defaults.SELECTED_CLASS).remove();
				$(options.setButton).removeClass("display-none");
			});
		
	},
		
	
	assignCloseContainerButtonEventHandler = function() {
		
		$(options.buttonCloseContainer).click(function() {
			$(options.fieldsContainer).trigger("hide");
		});
		
		
	},
	
	assignContainerEventHandlers = function() {
		
		
		$(options.fieldsContainer)
			.on("hide", function() {
				var $this = $(this);
				
				$this.slideUp(600, function() {
					$(options.buttonsContainer).removeClass("display-none");
					s
					$this.find(options.list).empty();
					
					for(var property in options.fields) {
						
						$(options.fields[property]).val("");
					}	
				});
				
				$(options.buttonLoadMore).addClass("display-none");
			})
			.on("show", function() {
				$(options.buttonsContainer).addClass("display-none");
				
				$(this).slideDown(600, function() {
					$(options.fieldsContainer).removeClass("display-none");
				});
			});
		
	},
	
	assignSetButton = function() {
		
		$(options.setButton).click(function(event) {
		
			event.preventDefault();
			$(options.fieldsContainer).removeClass("display-none");
			$(options.fieldsContainer).trigger("show");
		});
	},
	
	assignLawJournalSelectionFields = function() {

		for(var property in options.fields) {
			
			$(options.fields[property])
				.keyup(function() {
				
					findAndLoadLawJournalEntry(true);
				});
		}

	},
	
	assignNextPage = function() {
		
		
		$(options.buttonLoadMore)
			.click(function(event) {
				
				event.preventDefault();
				
				pageNumber += 1;
				
				findAndLoadLawJournalEntry(false);
				
			});
		
	},
	
	findAndLoadLawJournalEntry = function(cleanContainer) {
		
		var url = options.url,
			year = $(options.fields.year).val(),
			journalNo = $(options.fields.journalNo).val(),
			entry = $(options.fields.entry).val(),
			text = $(options.fields.text).val(),
			$container = $(options.list);
		
		
		if (year == "" && journalNo == "" && entry == "" && title == "") {
			return;
		}
		
		url += "?year=" + year;
		url += "&journalNo=" + journalNo;
		url += "&entry=" + entry;
		url += "&text=" + text;
		url += "&pageSize=" + defaults.PAGE_SIZE;
	
		
		if (lastUrl == url) {
			
			if (pageNumber === lastPageNumber) {
				return;
			} 
			
			
		} else {
			lastUrl = url
		}

		lastPageNumber = pageNumber;
		
		
		$.ajax(url + "&pageNumber=" + pageNumber)
			.done(function(data) {
				var dataLength = data.length,
					$firstLi = $("<li></li>");
				
				
				if (cleanContainer) {
					$container.empty();
				}
				
				if (dataLength > 0) {
					$firstLi.text(options.text.choseItem);
				} else {
					$firstLi.text(options.text.noItems);
				}
				
				if ($container.find("li").length === 0) {
					$container.append($firstLi);
				}
				

				for(var j = 0; j < dataLength; j += 1) {
					

					//Closure for storing law journal entry id & text
					(function () {
						var $li = $("<li><a></a></li>"),
							lawJournalEntryId = data[j].id
							innerText = "";
						
						innerText = highlight(data[j], year, journalNo, entry, text);
						
						$li.find("a")
							.attr("href", "")
							.html(innerText);
	
						$li.click(function(event) {
							
							event.preventDefault();
							
							$(options.fieldLawJournalId).val(lawJournalEntryId);
							$(options.fieldsContainer).trigger("hide");									
							$(options.buttonsContainer).trigger("setSelected", [innerText.replace(/(<([^>]+)>)/ig,"")]);
						});
						
						$container.append($li);
						
					})();
					
				}
				
				
				if (defaults.PAGE_SIZE == dataLength) {
					$(options.buttonLoadMore).removeClass("display-none");
				} else {
					$(options.buttonLoadMore).addClass("display-none");
				}
				
	
			});
		
		/*
		function checkUrlChange(url) {
			if(!checkUrlChange.lastUrl) {
				checkUrlChange.lastUrl = url;
			}
			
			if(url === checkUrlChange.lastUrl) {}
			
		}*/
	},
	
	highlight = function(data, year, journalNo, entry, title) {
		var text = "";
		
		
		text += highlightMatchingPhrase(data.year, year);
		text += "/" + highlightMatchingPhrase(data.journalNo, journalNo);
		text += "/" + highlightMatchingPhrase(data.entry, entry);
		text += " - " + highlightMatchingPhrase(data.title, title);
		
		
		return text;
		
		function highlightMatchingPhrase(text, phrase) {
			var string = "" + text;
			
			return string.replace(phrase, "<b>" + phrase + "</b>");
		}
		
		function bold() {}
		
	}
	
	
	
	
	space.init = function(source) {
		
		options = source;
		assignButtonsContainerEventHandlers();
		assignContainerEventHandlers();
		assignCloseContainerButtonEventHandler();
		assignSetButton();
		assignLawJournalSelectionFields();
		assignNextPage();
	};
	
	
	return space;
})();





