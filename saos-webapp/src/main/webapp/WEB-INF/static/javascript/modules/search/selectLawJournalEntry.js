/*
 * 
 * 
 * @author Łukasz Pawełczak
 */

var SelectLawJournalEntry = (function() {
	
	
	var space = {},
		options = {},
		defaults = {
			SELECTED_CLASS : "selected-law"
		},
	
	
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
		
		
		$(options.fieldsContainer).on("hide", function() {
			var $this = $(this);
			
			$this.slideUp(600, function() {
				$(options.buttonsContainer).removeClass("display-none");
				$this.find(options.list).empty();
				
				for(var property in options.fields) {
					
					$(options.fields[property]).val("");
				}	
			});
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
				
					var url = options.url,
						year = $(options.fields.year).val(),
						journalNo = $(options.fields.journalNo).val(),
						entry = $(options.fields.entry).val(),
						text = $(options.fields.text).val();
				
					if (year == "" && journalNo == "" && entry == "" && text == "") {
						return;
					}
					
					url += "?year=" + year;
					url += "&journalNo=" + journalNo;
					url += "&entry=" + entry;
					url += "&text=" + text;

					
					$.ajax(url)
						.done(function(data) {
							var dataLength = data.length,
								$container = $(options.list);
							
							$container.empty();
							
							if (dataLength > 0) {
								$container.append("<li>" + options.text.choseItem + "</li>");
							} else {
								$container.append("<li>" + options.text.noItems + "</li>");
							}
							
							for(var j = 0; j < dataLength; j += 1) {
								
								 //Closure for storing law journal entry id & text
								(function () {
									var $html = "",
										lawJournalEntryId = data[j].id,
										text = "";
									
									//TODO highlighting
									text += data[j].year;
									text += "/" + data[j].journalNo;
									text += "/" + data[j].entry;
									text += " - " + data[j].title;
										
									$html += "<li><a href='' >";
									$html += text;
									$html += "</a></li>";
									
									$html = $($html);
									$html.click(function(event) {
										
										event.preventDefault();
										
										$(options.fieldLawJournalId).val(lawJournalEntryId);
										$(options.fieldsContainer).trigger("hide");									
										$(options.buttonsContainer).trigger("setSelected", [text]);
									});
									
									$container.append($html);
									
								})();
								
							}
			
						});
						
				});
		}

	}
	
	space.init = function(source) {
		
		options = source;
		assignButtonsContainerEventHandlers();
		assignContainerEventHandlers();
		assignCloseContainerButtonEventHandler();
		assignSetButton();
		assignLawJournalSelectionFields();
	};
	
	
	return space;
})();





