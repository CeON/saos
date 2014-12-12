/*
 * Suggester - module that provides autocompletion.
 * 
 * Module requires:
 * - jquery
 * 
 * @author Łukasz Pawełczak
 */

var Suggester = (function() {
    
    var space = {},
    	selItem = [],
    	getUrl = [],
    	
    	maxElementsInList = 10,
    	cssClass = "suggestions";
    
    
    /* Initialize url parameter and runs main methods: create suggestion container
     * and assign load suggestions methods to input field.
     * 
     * @param $field input field connected to suggester
     * @param source configuration parameters(json):
     * 		- getUrl - function that takes one @param id and returns url location with list of suggestions.
     * 		  Module requires function instead of simple url, because this way it can handle url like e.g. "/keywords/COMMON/{phrase}/list"
     */
    
    init = function($field, source) {
        var fieldId = $field.attr("id");
        
        if (source.getUrl !== undefined) {
        	getUrl[fieldId] = source.getUrl;
        }
      
        createContainer($field);
        assignEvents($field);
    },
    
    /* Create container for suggestions. 
     * @param $field input field, suggestions container is added to html after $field.   
     */
    createContainer = function($field) {
		var $container = $("<div></div>"),
			$list = $("<ul></ul>");
    		
		$list.attr("id", "suggestions-" + $field.attr("id"));
    	$container.addClass(cssClass).prepend($list);
    	$field.after($container);
    },
    
    
    /* Changed some of default events assigned to input $field:
     * - turn off default html5 autocompletion,
     * - turn off default events keyup & keypress,
     * - on event keyup load suggestions, send ajax request and show
     *   response suggestions below input field,
     * - on event focus show suggestions list.
     *   
     *  @param $field input field
     */
    assignEvents = function($field) {
    	var fieldId = $field.attr("id"),
    		$suggestions = $("#suggestions-" + fieldId);

    	
        $field.attr("autocomplete","off")
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
					loadSuggestions($field, true);
				}
				else if(event.keyCode == 38 && selItem[fieldId] !== null ) {
					//arrow up
					setSelectedItem(selItem[fieldId] - 1, fieldId);
					populateSearchField($field, fieldId);
					event.preventDefault();
				}
				else if(event.keyCode == 40 && selItem[fieldId] !== null ) {
					//arrow down
					setSelectedItem(selItem[fieldId] + 1, fieldId);
					populateSearchField($field, fieldId);
					event.preventDefault();
				}
				else if(event.keyCode == 27 && selItem[fieldId] !== null ) {
					//Esc
					setSelectedItem(null, fieldId);
					$field.trigger("blur");
				}
            })
            .keydown(function(event){
				if(event.keyCode == 13 && selItem[fieldId] !== null ){
					populateSearchField($field, fieldId);
					setSelectedItem(null, fieldId);
				}
            })
            .focus(function() {
            	if ($field.attr("value") !== "" && $suggestions.find("li").length > 0) {
            		setSelectedItem(-1, fieldId);
            	}
            })
            .blur(function(){
        		setTimeout(function(){
        			setSelectedItem(null, fieldId);
    			}, 250);
            });
    },
        
    
    /* Send request to server for list of suggestions.
     * When response comes add them to html code.
     * @param $field input field
     * @param showSuggestions boolean that specifies if suggestion list should be visible
     */
    loadSuggestions = function($field, showSuggestions){
        var fieldId = $field.attr("id"),
        	$suggestions = $("#suggestions-" + fieldId),
        	inputVal = $field.val().trim(),
            html = "";
        
        if (inputVal === "") { 
        	$suggestions.empty();
        	$suggestions.hide();
        	return;
        }
        
        $.ajax(getUrl[fieldId](inputVal))
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
			
				setSelectedItem(0, fieldId);
				$suggestions.prepend(html);
				
				$suggestions.find("li").each(function(index) {
				$(this).on("click", function() {
						populateSearchField($field, fieldId);
						setSelectedItem(null, fieldId);
						loadSuggestions($field, false);
					}).on("mouseover", function() {
							setSelectedItem(index, fieldId);
						});        				
					});
					
				if (showSuggestions){
					$suggestions.show();
				} else {
					$suggestions.hide();
				}
				
			} else {
				$suggestions.empty();
				setSelectedItem(null, fieldId);
			}
			 
        });
    },
    
    /* Select suggestion located on list 
     * @param value selected suggestion number
     * @param fieldId input field id 
     */
    setSelectedItem = function(value, fieldId) {
    	var $suggestions = $("#suggestions-" + fieldId); 
    	
    	selItem[fieldId] = value;
    	
    	if (selItem[fieldId] === null) {
    		$suggestions.find("li").removeClass("selected");
    		$suggestions.hide();
    		return;
    	}
    	
    	if (selItem[fieldId] < 0) {
    		selItem[fieldId] = 0;
    	}
    	
    	if (selItem[fieldId] >= $suggestions.find("li").length) {
    		selItem[fieldId] = $suggestions.find("li").length - 1;
    	}
    	
    	$suggestions.find("li").removeClass("selected");
    	$suggestions.find("li:nth-child(" + ((selItem[fieldId]) + 1) + ")").addClass("selected");
    	$suggestions.show();
    },
    
    /* Change value of search field to match value of selected suggestion
     * @param $field input field
     */
    populateSearchField = function($field) {
    	var fieldId = $field.attr("id"), 
    		$suggestions = $("#suggestions-" + fieldId);
    	
    	if ((selItem[fieldId] >= 0 && selItem[fieldId] !== null  && selItem[fieldId] !== undefined)) {
    		$field.val($suggestions.find("li").eq(selItem[fieldId]).text());
    	}
    };
    
    
	//------------------------ PUBLIC --------------------------
     
    space.run = function(item, source) {
        init(item, source);
    };
    
    return space;
}());

$.fn.autoCompletionSuggester = function(source) {
    Suggester.run(this, source);
}



