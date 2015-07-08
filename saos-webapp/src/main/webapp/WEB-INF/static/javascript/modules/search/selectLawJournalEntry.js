/*
 * Module for selecting lawJournalEntry. 
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
		
		
	/* Prepare fields used in lawJournalEntry searching. */
	prepareFields = function() {
		
		if($("." + defaults.SELECTED_CLASS).length > 0) {
		
			$("." + defaults.SELECTED_CLASS + " a.remove").click(function() {
				$(this).tooltip("hide");
				$(options.buttonsContainer).trigger("removeSelected");
			});
			
			$(options.setButton).trigger("hide");
		}
		
		for(var property in options.fields) {
			$(options.fields[property]).attr("autocomplete", "off");
		}
	}
	
	/* Buttons container events:
	 * - setSelected @param(text) - adds selected law journal with text
	 * - removeSelected - hides selected law journal
	 * - hideContainer - hides buttons 
	 * - showContainer - shows buttons
	 */
	assignButtonsContainerEventHandlers = function() {
		
		$(options.buttonsContainer)
			.on("setSelected", function(event, selectedText, fullTitle) {
				var $selectedElement = $("<div></div>"),
					$removeButton = $("<a><span></span></a>");
				
				$removeButton
					.attr("href", "")
					.addClass("remove")
					.attr("title", springMessage.judgmentSearchFormFieldLawJournalRemove)
					.attr("data-toggle", "tooltip")
					.attr("data-placement", "right")
					.tooltip({container: 'body'})
					.click(function(event) {
						event.preventDefault();
						
						$(this).tooltip("hide");
						
						$(options.buttonsContainer).trigger("removeSelected");
					});
				
				$selectedElementSpan = $("<span>" + selectedText + "</span>")
					.attr("title", fullTitle)
					.attr("data-toggle", "tooltip")
					.attr("data-placement", "right")
					.tooltip({container: 'body'});
				
				$selectedElement
					.addClass(defaults.SELECTED_CLASS)
					.append($selectedElementSpan)
					.append($removeButton);
				
				$(options.setButton)
					.before($selectedElement)
					.trigger("hide");
				
			})
			.on("removeSelected", function() {
				
				$(options.fieldLawJournalCode).val("");
				
				$(this).find("." + defaults.SELECTED_CLASS).remove();
				
				$(options.setButton).trigger("show")
			})
			.on("showContainer", function() {
				
				$(this).removeClass("display-none");
			})
			.on("hideContainer", function() {
				$(this).addClass("display-none");
			});
		
	},
		
	/* On form reset, reset lawJournalEntry field. */
	assignFieldLawJournalReset = function() {
		$(options.form)
			.on("reset", function() {
				$(options.buttonsContainer).trigger("removeSelected");
				$(options.fieldLawJournalCode).val("");
			});
	}
	
	/* Button for closing lawJournalEntry selecting area. */
	assignCloseContainerButtonEventHandler = function() {
		
		$(options.buttonCloseContainer).click(function() {
			$(options.fieldsContainer).trigger("hide");
		});
	},
	
	/* Area for selecting lawJournalEntry 
	 * Event handlers:
	 * - hide - make container not visible,
	 * - show - make container visible
	 */
	assignContainerEventHandlers = function() {
		
		$(options.fieldsContainer)
			.on("hide", function() {
				var $this = $(this);
				
				$this.slideUp(600, function() {
					$(options.buttonsContainer).trigger("showContainer");
					
					$this.find(options.list).empty();
					
					for(var property in options.fields) {
						
						$(options.fields[property]).val("");
					}	
				});
				
				$(options.buttonLoadMore).trigger("hide");
			})
			.on("show", function() {
				$(options.buttonsContainer).trigger("hideContainer");
				
				$(this).slideDown(600, function() {
					$(options.fieldsContainer).removeClass("display-none");
				});
			});
		
	},
	
	/* Assign events to button that opens/hides area for selecting lawJournalEntry. */
	assignSetButtonEventHandlers = function() {
		
		$(options.setButton)
			.click(function(event) {
			
				event.preventDefault();
				$(options.fieldsContainer).removeClass("display-none");
				$(options.fieldsContainer).trigger("show");
			})
			.on("show", function(event) {
				$(this).removeClass("display-none");
				event.stopPropagation();
			})
			.on("hide", function(event) {
				$(this).addClass("display-none");
				event.stopPropagation();
			});
	},
	
	/*  
	 * Search and show lawJournalEntry, when fields change their value.
	 */
	assignLawJournalSelectionFields = function() {

		for(var property in options.fields) {
			
			$(options.fields[property])
				.keyup(function() {
					findAndLoadLawJournalEntry(true);
				});
		}

	},
	
	/* Event handlers for button load more lawJournalEntries */
	assignButtonLoadMore = function() {
		
		$(options.buttonLoadMore)
			.click(function(event) {
				
				event.preventDefault();
				
				pageNumber += 1;
				findAndLoadLawJournalEntry(false);
			})
			.on("show", function(event) {
				event.stopPropagation();
				$(this).removeClass("display-none").addClass("display-inline-block");
			})
			.on("hide", function(event) {
				event.stopPropagation();
				$(this).addClass("display-none").removeClass("display-inline-block");
			});
		
	},
	
	
	/* Find lawJournalEntry and load it to container */
	findAndLoadLawJournalEntry = function findAndLoadLawJournalEntry(cleanContainer) {
		
		if (!findAndLoadLawJournalEntry.lastUrl) {
			findAndLoadLawJournalEntry.lastUrl = "";
		}
		
		if (!findAndLoadLawJournalEntry.lastPageNumber){
			findAndLoadLawJournalEntry.lastPageNumber = 0;
		}
		
		var url = options.url,
			year = $(options.fields.year).val(),
			journalNo = $(options.fields.journalNo).val(),
			entry = $(options.fields.entry).val(),
			text = $(options.fields.text).val(),
			$container = $(options.list);
		
		//Dont load new data if url is empty
		if (year == "" && journalNo == "" && entry == "" && text == "") {
			return;
		}
		
		url += "?year=" + year;
		url += "&journalNo=" + journalNo;
		url += "&entry=" + entry;
		url += "&text=" + text;
		url += "&pageSize=" + defaults.PAGE_SIZE;
	
		//Dont load new data if url has not changed
		if (findAndLoadLawJournalEntry.lastUrl === url
				&& findAndLoadLawJournalEntry.lastPageNumber === pageNumber) {
			return;			
		} else {
			pageNumber = 0;
		}
		
		findAndLoadLawJournalEntry.lastPageNumber = pageNumber;
		findAndLoadLawJournalEntry.lastUrl = url;
		
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
					$firstLi
						.addClass("no-items")
						.text(options.text.noItems);
				}
				
				if ($container.find("li").length === 0) {
					$container.append($firstLi);
				}
				

				for(var j = 0; j < dataLength; j += 1) {
					
					//Closure for storing law journal entry id & innerText
					(function () {
						var $li = $("<li><a></a></li>"),
							lawJournalEntryCode = data[j].code,
							title = data[j].title,
							innerText = "";
						
						innerText = prepareLawJournalEntryForDisplay(data[j], year, journalNo, entry, text);
						
						$li.find("a")
							.attr("href", "")
							.attr("data-toggle", "tooltip")
							.attr("data-placement", "right")
							.attr("title", title)
							.html(innerText)
							.tooltip({container: 'body'});
	
						innerText = innerText.replace(/(<([^>]+)>)/ig,"");
						
						$li.click(function(event) {
							
							event.preventDefault();
							
							$(options.fieldLawJournalCode).val(lawJournalEntryCode);
							$(options.fieldsContainer).trigger("hide");
							$(this).find("a").tooltip("hide");
							$(options.buttonsContainer).trigger("setSelected", [innerText, title]);
						});
						
						$container.append($li);
						
					})();
					
				}

				//Show/hide button load more jawJournalEntries
				if (defaults.PAGE_SIZE == dataLength) {
					$(options.buttonLoadMore).trigger("show");
				} else {
					$(options.buttonLoadMore).trigger("hide");
				}

			});

	},
	
	/* Prepares lawJournalEntry for display.
	 * Converts object data that represents lawJournal entry
	 * into string of format:
	 * year/journalNo/entry - title
	 * Function also highlights search phrases.
	 * 
	 * @param data - object that represents lawJournalEntry:
	 * 			data.year 
	 * 			data.journalNo
	 * 			data.entry
	 * 			data.title 
	 * @param year - year that should be highlighted
	 * @param journalNo - journalNo that should be highlighted
	 * @param entry - entry that should be highlighted
	 * @param title - title that should be highlighted
	 * 
	 * @return string text with highlighted phrases
	 * */
	prepareLawJournalEntryForDisplay = function(data, year, journalNo, entry, title) {
		
		return prepare();
		
		function prepare() {
			var text = "";
			
			text += highlightMatchingPhrase(data.year, year);
			text += "/" + highlightMatchingPhrase(data.journalNo, journalNo);
			text += "/" + highlightMatchingPhrase(data.entry, entry);
			text += " - " + highlightAndMovePhrase(data.title, title);
			
			return text;
		}
		
		function highlightMatchingPhrase(line, word ) {
			if (!word) {
				return line + "";
			}
			return (line + "").replace( new RegExp( '(' + word + ')', 'gi' ), "<b>$1</b>" );
		}
		
		function findPosition(line, word) {
			return (line + "").search(new RegExp( '(' + word + ')', 'gi' ));
		}
		
		function highlightAndMovePhrase(text, phrase) {
			var string = highlightMatchingPhrase(text, phrase),
				position = findPosition(string, phrase);

			if (position > 40) {
				string = string.substr(position - 20, string.length);
				string = "... " + string;
			}
			
			return string;
		}		
	},
	
	//------------------------ PUBLIC --------------------------
	
	space.init = function(source) {
		
		options = source;
		
		assignButtonsContainerEventHandlers();
		assignFieldLawJournalReset();
		assignContainerEventHandlers();
		assignCloseContainerButtonEventHandler();
		assignSetButtonEventHandlers();
		assignLawJournalSelectionFields();
		assignButtonLoadMore();
		
		prepareFields();
	};
	
	return space;
})();





