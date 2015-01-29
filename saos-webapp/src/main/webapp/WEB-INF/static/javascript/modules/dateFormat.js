/*
 * DateFormat - converts string to string in format aa-aa-aaaa 
 * 
 * @author Łukasz Pawełczak
 */

var DateFormat = function() {

	var space = {},
	
	/* Insert one string in another  at position.
	 * insertAt("Helorld", 3 "lo W") -> "Hello World"
	 * 
	 * @param string - main string
	 * @param index - position at which string will be inserted
	 * @param value - inserted string
	 */
	insertAt = function(string, index, value) { 
	  return string.substr(0, index) + value + string.substr(index);
	};
	
	
	/* Converts string to match format aa-aa-aaaa. 
	 * Argument text can't have more than 8 letters,
	 * rest is subtracted.
	 * 
	 * @param text - string to convert
	 * @returns - converted string
	 */
	space.convert = function(text) {
		var length = 0;
		
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

