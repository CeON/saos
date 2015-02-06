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
    	getValue = [],
    	
    	maxElementsInList = 10,
    	cssClass = "suggestions",
    
    	SELECTED_FIELD_CLASS = "suggestion",
    	CONTAINER_ID = "suggestions",
    	AREA_ID = "suggestion-area",
    	WRAPPER_ID = "suggestion-wrapper",
    	
		FIELD_FOR = "data-field-for",
    		
    		
    	OFFSET = 100;
    
    /* Initialize url parameter and runs main methods: create suggestion container
     * and assign load suggestions methods to input field.
     * 
     * @param $field input field connected to suggester
     * @param source configuration parameters(json):
     * 		- boxyMode - selected suggestions are presented as boxes and can be removed by clicking on them
     * 			- enabled - turn on boxy mode,
     * 			- placeholder - text that is used as placeholder in textarea  
     * 
     * 		- url - returns url location with list of suggestions.
     * 		   		it can be function or simple string url, function can handle url like e.g. "/keywords/COMMON/{phrase}/list"
     *		- getValue - returns value of suggestion taken from suggestion list
     *					 it must be specified only when suggestion is not a simple string e.g. 
     *				
     *				basic suggestion list:
     *				{"suggestion1", "suggestion2", "suggestion3}
     *				
     *				json suggestion list:
     *				{
     *					{ id: 1,
     *					  phrase: "suggestion1"
     *					},
     *					{ id: 2,
     *					  phrase: "suggestion2"
     *					}
     *				}
     *				
     *				for this suggestion list getValue function must specified as:
     *
     *				function(element) {
     *					return element.phrase;
	 *				}
	 *				
     */
    init = function($field, source) {
        var fieldId = $field.attr("id");
        
        if (source.url !== undefined) {
        	
        	if (typeof source.url === "function") {
        		getUrl[fieldId] = source.url;
        	} else {
        		getUrl[fieldId] = function() { return source.url; };
        	}
        }
        
        if (source.getValue !== undefined) {
        	
        	if (typeof source.getValue === "function") {
        		getValue[fieldId] = source.getValue;
        	} else {
        		getValue[fieldId] = function(value) { return value; };
        	}
        }
        
        if (source.boxyMode !== undefined && source.boxyMode.enabled !== undefined && source.boxyMode.enabled === true) {
        	var $newField;
        	
	        removeDuplicatedValues(fieldId);
	        createFieldAreaBoxyMode($field, source.boxyMode);
	        
	        $newField = $("#" + AREA_ID + "-" + fieldId);
	        
	        createContainer($newField);
	        assignEvents($newField, populateFieldBoxyMode);
    	} else {
	        createContainer($field);
	        assignEvents($field, populateFieldSimpleMode);
    	}
        
        $field.closest("form").on("reset", function() {
        	$("." + SELECTED_FIELD_CLASS).each(function() {
        		$(this).remove();
        		
        		$field.val("")
        			  .trigger("change");
        	});
        });
        
    },
    
    /* Create container for suggestions. 
     * @param $field input field, suggestions container is added to html after $field.   
     */
    createContainer = function($field) {
		var $container = $("<div></div>"),
			$list = $("<ul></ul>"),
			attr = $field.attr(FIELD_FOR),
			cssClasses = cssClass;
    		
		if ($field.attr(FIELD_FOR) === undefined) {
			$field.attr(FIELD_FOR, $field.attr("id"));
		} else {
			cssClasses += " suggestions-boxy";
		}
		
		$list.attr("id", CONTAINER_ID + "-" + $field.attr(FIELD_FOR));
    	$container.addClass(cssClasses).prepend($list);
    	$field.after($container);
    },
    
    
    /* Creates field area for suggester boxy mode. 
     * Input field {@param $field} is changed to input
     * type hidden, so it is not visible. Next to it
     * function creates textarea that should behave
     * as base input.
     * 
     * @param $field
     * @param source
     */
    createFieldAreaBoxyMode = function($field, source) {
    	var fieldId = $field.attr("id"),
    		fieldValue = $field.val(),
    		$textArea = $("<textarea></textarea>"),
    		$wrapper = $("<div></div>"),
    		newFieldId = AREA_ID + "-" + fieldId;
    	
    	$textArea
    		.attr("id", newFieldId)
    		.attr("rows", 1)
    		.attr(FIELD_FOR, fieldId)
    		.attr("placeholder", source.placeholder);
    		
    	$wrapper
    		.addClass(WRAPPER_ID)
    		.attr("id", WRAPPER_ID + "-" + fieldId);
    	
    	if (fieldValue !== "") {
    		var elements = fieldValue.split(",");
    		
    		for(var i = elements.length - 1; i >= 0; i -= 1) {        		
        		$wrapper.prepend(createBoxedSuggestion(elements[i], fieldId));
    		}
    	}

    	$wrapper.append($textArea);
    	
    	$field
    		.before($wrapper)
    		.attr("type", "hidden")
    		.change(function() {
    			var id = $(this).attr("id");
    			
    			removeDuplicatedValues(id);
				refreshSuggestionField(id);
			});

    	//change label for id
    	
    	$("label[for=" + fieldId + "]").attr("for", newFieldId);
    	
    	$("#" + AREA_ID + "-" + fieldId).val(""); //trigger update on field textarea 
    },
    
    /* Create boxed suggestion.
     * Boxed suggestion is inserted into field.
     * Inside boxed suggestion is added remove button,
     * that lets user remove selected suggestion.
     * 
     * @param value of suggestion
     * @param fieldId
     * @return created boxed suggestion
     */
    createBoxedSuggestion = function(value, fieldId) {
    	var $boxedSuggestion = $("<div></div>"),
			$removeButton = $("<span></span>");
		
    	$boxedSuggestion
    		.text(value)
    		.addClass(SELECTED_FIELD_CLASS);
    	
    	$removeButton
    		.addClass("remove-suggestion");
    	
		removeSuggestion($removeButton, fieldId);
		$boxedSuggestion.append($removeButton);
    	
		return $boxedSuggestion;
    },
    
    
    /* Changed some of default events assigned to input $field:
     * - turn off default html5 autocompletion,
     * - turn off default events keyup & keypress,
     * - on event keyup load suggestions, send ajax request and show
     *   response suggestions below input field,
     * - when field input is empty and users writes backspace,
     *   last added keyword will be removed,
     * - on event focus show suggestions list.
     *   
     *  @param $field input field
     */
    assignEvents = function($field, populateMethod) {
    	var fieldId = $field.attr(FIELD_FOR),
    		$suggestions = $("#" + CONTAINER_ID + "-" + fieldId),
			populate = populateMethod;

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
            	
            	if (event.keyCode > 40 || event.keyCode === 8){
					
            		// special characters and backspace
            		
					loadSuggestions($field, true, populate);					
				}
				else if (event.keyCode === 38 && selItem[fieldId] !== null ) {
					
					//arrow up
					
					setSelectedItem(selItem[fieldId] - 1, fieldId);
					
			    	if ($("#" + WRAPPER_ID + "-" + fieldId).length === 0) {
			    		populateFieldSimpleMode($field);
			    	}
					
					event.preventDefault();
				}
				else if (event.keyCode === 40 && selItem[fieldId] !== null ) {
					
					//arrow down
					
					setSelectedItem(selItem[fieldId] + 1, fieldId);
					
					if ($("#" + WRAPPER_ID + "-" + fieldId).length === 0) {
			    		populateFieldSimpleMode($field);
			    	}
					
					event.preventDefault();
				}
				else if (event.keyCode == 27 && selItem[fieldId] !== null ) {
					
					//Esc
					
					setSelectedItem(null, fieldId);
					$field.trigger("blur");
				}
            })
            .keydown(function(event){
            	if (event.keyCode === 8 && $field.val().length === 0) {
            		
            		//remove last added keyword
            		
            		$("#" + WRAPPER_ID + "-" + fieldId).find(".remove-suggestion").last().trigger("click");
            		
            	} else if(event.keyCode == 13 && selItem[fieldId] !== null ){
					populate($field);
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
    loadSuggestions = function($field, showSuggestions, populateMethod){
        var fieldId = $field.attr(FIELD_FOR),
        	$suggestions = $("#" + CONTAINER_ID + "-" + fieldId),
        	inputVal = $field.val().trim(),
			populate = populateMethod;
        
        if (inputVal === "") { 
        	$suggestions.empty();
        	$suggestions.hide();
        	return;
        }
        
        $.ajax(getUrl[fieldId](inputVal))
        .done(function(data) {        	
        	var dataLength = data.length,
        		html = "";
        	
        	if(dataLength > 0) {
				
				$suggestions.empty();
				
				if (maxElementsInList < dataLength) {
					dataLength = maxElementsInList;
				}
				
				for(var i = 0; i < dataLength; i += 1) {
					var suggestionValue = getValue[fieldId](data[i]);
					
					html += "<li>";
					html += "<span>" + suggestionValue.substring(0, inputVal.length) + "</span>";
					html += suggestionValue.substring(inputVal.length, suggestionValue.length);
					html += "</li>";
				}
			
				setSelectedItem(0, fieldId);
				$suggestions.prepend(html);
				
				$suggestions.find("li").each(function(index) {
				$(this).on("click", function() {
						populate($field, fieldId);
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
    	var $suggestions = $("#" + CONTAINER_ID + "-" + fieldId); 
    	
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
    populateFieldBoxyMode = function($field) {
    	var fieldId = $field.attr(FIELD_FOR), 
    		$suggestions = $("#" + CONTAINER_ID + "-" + fieldId),
    		$boxedSuggestion;
    	
    	if ((selItem[fieldId] >= 0 && selItem[fieldId] !== null  && selItem[fieldId] !== undefined)) {
    		var $wrapper = $("#" + WRAPPER_ID + "-"+fieldId),
    			$suggestion = $wrapper.find("." + SELECTED_FIELD_CLASS);
    		
    		$field.val("");
    		$boxedSuggestion = createBoxedSuggestion($suggestions.find("li").eq(selItem[fieldId]).text().trim(), fieldId);
    		
    		if ($suggestion.length > 0) {
    			$suggestion.last().after($boxedSuggestion);
    		} else {
    			$wrapper.prepend($boxedSuggestion);
    		}
    		
    		populateInputFieldInBoxyMode(fieldId);
    	}
    },
    
    /* Populate field with selected value in simple mode.
     * @param $field input field 
     */
    populateFieldSimpleMode = function($field) {
    	var fieldId = $field.attr(FIELD_FOR), 
			$suggestions = $("#" + CONTAINER_ID + "-" + fieldId);
    	
		if (selItem[fieldId] >= 0 && selItem[fieldId] !== null  && selItem[fieldId] !== undefined) {
			$field.val($suggestions.find("li").eq(selItem[fieldId]).text());
		}
	},
    
	/* Populate hidden input field in boxy mode.  
	 * All suggestions presented in boxy field are converted
	 * and added as string to hidden input field.
	 * 
	 * @param fieldId
	 */
    populateInputFieldInBoxyMode = function(fieldId) {
    	var	value = "",
    		first = true,
    		valueArray = [];
    	
    	$("." + SELECTED_FIELD_CLASS).each(function() {
    		var text = $(this).text().trim(),
    			length = valueArray.length,
    			exists = false;
    	
    		for(var i = 0; i < length; i += 1) {
	    		if (text === valueArray[i]) {
	    			exists = true;
	    		}
	    	}
    		
    		if (!exists) {
    			valueArray[length] = text;
    		}
    		
    	});
    	
    	valueArray = valueArray.sort();
    	
    	value = convertArrayToString(valueArray, ", ")
    	
    	$("#" + fieldId).val(value).change();
    },
    
    /* Remove boxed suggestion.
     * When users clicks on suggestion close mark,
     * that suggestion is removed from field.
     * 
     * @param $removeButton suggestion remove button
     * @param fieldId 
     */
    removeSuggestion = function($removeButton, fieldId) {
    	
    	$removeButton.each(function() {
    		$(this).click(function() {
        		$(this).parent().remove();
        		populateInputFieldInBoxyMode(fieldId);
        	});
    	});
    },
    
    
    /* Method that changes width of textarea when suggester is in boxy mode.
     * When boxy suggestion is added to field, textarea needs to change
     * its width, to fit width of the whole field.
     * 
     * @param fieldId
     */
    refreshSuggestionField = function(fieldId) {
    	var $field = $("#" + fieldId),
    		$parent = $("#" + WRAPPER_ID + "-" + fieldId),
    		width = 0,
    		parentWidth = $parent.width();
    	
    	$parent.find("." + SELECTED_FIELD_CLASS).each(function() {
    		var suggestionWidth = $(this).outerWidth(true);
    		
    		width += suggestionWidth;
    		
    		if (width > parentWidth) {
    			width = suggestionWidth;
    		}
    		
    		if (width + OFFSET > parentWidth) {
    			width = 0;
    		}
    	});
    	
    	$parent.find("textarea").outerWidth(parentWidth - width -5);
    },
    
    /* Removes duplicated values from target input.
     * Method gets value from input(by @param fieldId),
     * after that method converts string of attributes into array and
     * removes values that exists more than once in that array.
     * In next step elements from array are inserted once
     * again into input value.
     * 
     * @param fieldId id of input field
     */
    removeDuplicatedValues = function(fieldId) {
    	var $field = $("#" + fieldId) 
    		value = $field.val(),
    		newValue = "",
    		first = true,
    		array = value.split(", "),
    		elementsToRemove = [],
    		length = array.length;
    	
    	array = array.sort();
    	
    	for(var i = 1; i < length; i += 1) {
    		if (array[i-1] === array[i]) {
    			elementsToRemove[elementsToRemove.length] = i-1;
    		}
    	}
    	
    	length = elementsToRemove.length;
    	
    	for(var i = 0; i < length; i += 1) {
    		array.splice(elementsToRemove[i], 1);
    	}
    	
    	newValue = convertArrayToString(array, ", ");
    	
    	$field.val(newValue);
    },
    
    /* Converts array of strings to one string.
     * @param array 
     * @param separator   
     */
    convertArrayToString = function(array, separator) {
    	var	value = "",
			first = true,
			length = array.length;
	    	
	    	for(var i = 0; i < length; i += 1) {
	    		
	    		if (separator !== "" && !first) {
	    			value += separator;
	    		}
	    		
	    		value += array[i];
	    		first = false;
	    	}
    	
    	return value;
    };
    
    
	//------------------------ PUBLIC --------------------------
     
    space.run = function(item, source) {
        init(item, source);
    };
    
    space.refresh = function($field) {
    	refreshSuggestionField($field.attr("id"));
    }
    
    return space;
}());

$.fn.suggester = function(source) {
    Suggester.run(this, source);
}

$.fn.suggesterRefresh = function() {
    Suggester.refresh(this);
}



