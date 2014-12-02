/*
 * Suggester - module that enables autocompletion.  
 * 
 * Module requires:
 * - jquery
 * 
 * @author Łukasz Pawełczak
 */

var Suggester = (function() {
    
    var space = {},
    	selItem = [],
    	
    	url = "keywords/",
    	maxElementsInList = 8,
    	cssClass = "suggestions";
    
    init = function(item, source) {
        var $item = item,
        	fieldId = $item.attr("id"),
        	searchName = "";
        
        
        if (source.searchName !== undefined) {
        	searchName = source.searchName;
        }
        
        if (source.url !== undefined) {
        	url = source.url;
        }
        
        if (source.maxSuggestions !== undefined) {
        	maxElementsInList = source.maxSuggestions;
        }
        
        if (source.cssClass !== undefined) {
        	cssClass = source.cssClass;
        }
        
                
        createContainer($item, searchName);
        assignEvents($item, fieldId, searchName);
        
    },
    
    /* Create container for suggestions. */
    createContainer = function($item, searchName) {
		var $container = $("<div></div>"),
			$list = $("<ul></ul>");
    		
		$list.attr("id", "suggestions-" + searchName);
    	$container.addClass(cssClass).prepend($list);
        $item.after($container);
    },
    
    
    /* Assign event methods to search field:
     * - turn off default autocompletion,
     * - turn off default events keyup & keypress,
     * - on event keyup load suggestions.
     */
    assignEvents = function($item, fieldId, searchName) {
    	var $field = $("#" + fieldId),
     		$suggestions = $("#suggestions-" + searchName);
  
    	
        $item.attr("autocomplete","off")
        	.off("keyup")
        	.on("keydown", function(evt) {
        		    evt = evt || window.event;
        		    var keyCode = evt.keyCode;
        		    if (keyCode == 38) {
        		        suppressKeypress = true; 
        		        return false;
        		    }
        	})
        	.off("keypress")
            .keyup(function(event) {
				if(event.keyCode > 40 || event.keyCode == 8){
					// special characters and Esc
					loadSuggestions(fieldId, searchName, true);
				}
				else if(event.keyCode == 38 && selItem[searchName] !== null ) {
					//arrow up
					setSelectedItem(selItem[searchName] - 1, searchName);
					populateSearchField($field, searchName);
					event.preventDefault();
				}
				else if(event.keyCode == 40 && selItem[searchName] !== null ) {
					//arrow down
					setSelectedItem(selItem[searchName] + 1, searchName);
					populateSearchField($field, searchName);
					event.preventDefault();
				}
				else if(event.keyCode == 27 && selItem[searchName] !== null ) {
					//Esc
					setSelectedItem(null, searchName);
					$field.trigger("blur");
				}
            })
            .keydown(function(event){
				if(event.keyCode == 13 && selItem[searchName] !== null ){
					populateSearchField($field, searchName);
					setSelectedItem(null, searchName);
				}
            })
            .focus(function() {
            	if ($field.attr("value") !== "" && $suggestions.find("li").length > 0) {
            		setSelectedItem(-1, searchName);
            	}
            })
            .blur(function(){
        		setTimeout(function(){
        			setSelectedItem(null, searchName);
    			}, 250);
            });
    },
        
    
    /* Send request to server for list of suggestions.
     * When response comes add them to html code.
     */
    loadSuggestions = function(fieldId, searchName, showData){
        var $field = $("#" + fieldId),
        	$suggestions = $("#suggestions-" + searchName),
        	inputVal = $field.val().trim(),
            html = "";
        
        if (inputVal === "") { 
        	$suggestions.empty();
        	$suggestions.hide();
        	return;
        }
        
        $.ajax(url + inputVal)
        .done(function(data) {
        	
        	var dataLength = data.length;
        	 
        	if(dataLength > 0) {
				
				$suggestions.empty();
				
				if (maxElementsInList < dataLength) {
					dataLength = maxElementsInList;
				}
				
				for(var i = 0; i < dataLength; i += 1) {
					html += "<li>";
					html += "<span>" + data[i].phrase.substring(0, inputVal.length) + "</span>";
					html += data[i].phrase.substring(inputVal.length, data[i].phrase.length);
					html += "</li>";
				}

			
				setSelectedItem(0, searchName);
				$suggestions.prepend(html);
				
				$suggestions.find("li").each(function(index) {
				$(this).on("click", function() {
						populateSearchField($field, searchName);
						setSelectedItem(null, searchName);
						loadSuggestions(fieldId, searchName, false);
					}).on("mouseover", function() {
							setSelectedItem(index, searchName);
						});        				
					});
					
				if (showData){
					$suggestions.show();
				} else {
					$suggestions.hide();
				}
				
			} else {
				$suggestions.empty();
				setSelectedItem(null, searchName);
			}
			 
        });
    },
    
    /* Select suggestion located on list */
    setSelectedItem = function(item, searchName) {
    	var $suggestions = $("#suggestions-" + searchName); 
    	
    	selItem[searchName] = item;
    	
    	if (selItem[searchName] === null) {
    		$suggestions.find("li").removeClass("selected");
    		$suggestions.hide();
    		return;
    	}
    	
    	if (selItem[searchName] < 0) {
    		selItem[searchName] = 0;
    	}
    	
    	if (selItem[searchName] >= $suggestions.find("li").length) {
    		selItem[searchName] = $suggestions.find("li").length - 1;
    	}
    	
    	$suggestions.find("li").removeClass("selected");
    	$suggestions.find("li:nth-child(" + ((selItem[searchName]) + 1) + ")").addClass("selected");
    	$suggestions.show();
    },
    
    /* Change value of search field to match value of selected suggestion */
    populateSearchField = function($field, searchName) {
    	var $suggestions = $("#suggestions-" + searchName);
    	
    	if ((selItem[searchName] >= 0 && selItem[searchName] !== null  && selItem[searchName] !== undefined)) {
    		$field.val($suggestions.find("li").eq(selItem[searchName]).text());
    	}
    };
    
    
	//------------------------ PUBLIC --------------------------
    
    space.run = function(item, source) {
        init(item, source);
    };
    
    return space;
}());

$.fn.autoComplitionSuggester = function(source) {
    Suggester.run(this, source);
}



