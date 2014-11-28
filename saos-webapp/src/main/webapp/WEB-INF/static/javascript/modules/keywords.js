


var Keywords = {
		
		
		
		
		
		get: function(url) {
			
			
			
			
			console.log("ASDASD");
			
			
			
			$.ajax(url)
			 .done(function(data) {
				 var j = 0,
				 	 dataLength = data.length;
				 
				 console.log(dataLength);
				 
				 for(j; j < dataLength; j += 1) {
					 console.log(data[j].phrase);
					 //options += prepareOption(data[j].id, data[j].phrase);
				 }
				 
				 //$divisionId.empty().removeAttr("disabled").prepend(options);
			 })
			 .fail(function() {});
			
		},
		
	
		
};



/* This module requires jquery */


var Suggester = (function() {
    
    var space = {},
    	selItem = [],
    	searchField = [];
    
    init = function(item, source) {
        var $item = item,
        	fieldId = $item.attr("id"),
        	searchName = "";
        
        if (source.searchName !== undefined && source.searchName.length > 0) {
        	searchName = source.searchName;
        } else {
        	console.log("Error: field searchName is required");
        	return;
        }
        
        if (source.searchField !== undefined && source.searchField.length > 0) {
        	searchField[searchName] = source.searchField; 
        }
        
        //selItem[searchName] = 0;
        
        createContainer($item, searchName);
        assignEvents($item, fieldId, searchName);
        
    },
    
    createContainer = function($item, searchName) {
		var $container = $("<div></div>"),
			$list = $("<ul></ul>");
    	
		$list.addClass("suggestions").attr("id", "suggestions-" + searchName);		
    	$container.addClass("suggestions-container").prepend($list);
        $item.after($container);
    },
    
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
					//event.preventDefault();
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
        
    loadSuggestions = function(fieldId, searchName, showData){
        var $field = $("#" + fieldId),
        	$suggestions = $("#suggestions-" + searchName),
        	inputVal = $field.attr('value'),
            html = "",
            $data = "";
        
        //$.get("api/suggest?q=" + inputVal + "&field=" + searchField[searchName], function(data) {})
        $.ajax("keywords/")
        .done(function(data) {
        	$data = $(data);
    		if($data.find('field').text() === searchField[searchName] && $data.find('q').text() === $field.attr('value')) {
    			
    			if($data.find("suggest").length > 0) {
        			var maxItems = 5,
        				i = 0;
    				
    				$suggestions.empty();
        			
        			$data.find("suggest").each(function(index) {
        				if (i < maxItems) {
        					html += "<li>" + $(this).text() + "</li>";
    						i += 1;
        				}
        			});
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
        	} 
        });
    },
    
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
    	//$suggestions.find("li").eq(selItem[searchName]).addClass("selected");
    	$suggestions.find("li:nth-child(" + ((selItem[searchName]) + 1) + ")").addClass("selected");
    	$suggestions.show();
    },
    
    populateSearchField = function($field, searchName) {
    	var $suggestions = $("#suggestions-" + searchName);
    	
    	if ((selItem[searchName] >= 0 && selItem[searchName] !== null  && selItem[searchName] !== undefined)) {
    		$field.attr("value", $suggestions.find("li").eq(selItem[searchName]).text());
    	}
    };
    

    //PUBLIC
    space.run = function(item, source) {
        init(item, source);
    };
    
    return space;
}());

$.fn.inputSuggester = function(source) {
    Suggester.run(this, source);
}



$("#input-search-keywords").inputSuggester({searchField: "all", searchName: "all"});





