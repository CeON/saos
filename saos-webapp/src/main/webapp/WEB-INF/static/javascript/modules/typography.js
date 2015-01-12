/* 
 * Typography provides text operations like:
 * 	- moving single letters from end of the line 
 * 	to next line e.g. "a", "i", "o".
 * 
 * 
 * Module requires:
 * - jquery
 * 
 * @author Łukasz Pawełczak
 */

var Typography = (function() {
	
	var space = {},
		SEPARATOR = "&nbsp",
		LETTERS_TO_MOVE = ["a", "z", "o", "i", "u", "w"],
	
	
		
	/* Initialize module parameters 
	 * 
	 * @param options - configuration json
	 * 		- letters - array with letters that can't be left at the end of the line
	 */
	init = function(options) {
		
		if (options.letters !== undefined && options.letters instanceof Array
				&& options.letters !== "") {
			LETTERS_TO_MOVE = options.letters;
		}
	},
		
	/* Method finds all the single letter words specified in LETTERS_TO_MOVE,
	 * and puts non-breaking space in front of them. When single letter word 
	 * happened to stay at the end of the line, it will be moved to next lines.
	 * 
	 * @param text - base text
	 * @return text with inserted non-breaking space signs 
	 */
	moveLettersFromEndOfLine = function(text){
        var length = text.length,
            newText = "",
            match = false;
       
        for (var i = 0; i < length; i += 1) {
            match = false;
            if(i > 1 && text[i] === " " && text[i - 2] === " ") {
                for (var j in LETTERS_TO_MOVE) {
                    if (LETTERS_TO_MOVE[j] === text[i - 1] || LETTERS_TO_MOVE[j].toUpperCase() === text[i - 1]) {
                        match = true;
                        break;
                    }
                }
               
                if (match) {
                    newText += SEPARATOR;
                } else {
                    newText += text[i];
                }
               
            } else {
                newText += text[i];
            }
        }
       
        return newText;
    };
	
	
	//------------------------ PUBLIC --------------------------

    
    space.run = function(text, options) {
    	init(options);
    	return moveLettersFromEndOfLine(text);
    };
    
	
    return space;
})();

$.fn.moveLettersFromEndOfLine = function(options) {
	var $this = $(this);
	   
	if($this.length > 0) {
	    var html = $this.html();
	    html = Typography.run(html, options)
	    $this.empty().html(html);
	}
}
