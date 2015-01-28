/*
 * DateFormat - converts string to string in format aa-aa-aaaa 
 * 
 * Module requires:
 * - jquery
 * 
 * @author Łukasz Pawełczak
 */

var DateFormat = function() {

	var space = {},
	
	/* Insert one string in another at position.
	 * 
	 */
	insertAt = function(string, index, value) { 
	  return string.substr(0, index) + value + string.substr(index);
	};
	
	
	space.convert = function(text) {
		var newText = "",
			length = 0;
		
		text = text.replace(/-/g, '');
		length = text.length;
		
		if (length > 2) {
			text = insertAt(text, 2, "-");
			length = text.length;
		}
		
		if (length > 5) {
			text = insertAt(text, 5, "-");
			length = text.length;
		}
		
		
		if (length > 10) {
			text = text.substr(0, 10);
		}
		
		return text;
	};
	
	return space;
}();

