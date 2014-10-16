/*
 * Module for managing cookies
 * 
 * @author Łukasz Pawełczak
 */
var Cookies = (function() {

	var space = {},

	create = function(name, value, minutes) {
		var expires = "";
		
		if (minutes) {
			var date = new Date();
			
			date.setTime(date.getTime() + (minutes * 60 * 1000));
			expires = "; expires=" + date.toGMTString();
		}
		
		document.cookie = name + "=" + value + expires + "; path=/";
	},
	
	read = function(name) {
		var nameEQ = name + "=",
			cookieArray = document.cookie.split(';'),
			i = 0,
			length = cookieArray.length;
		
		for(i; i < length; i += 1) {
			var cookie = cookieArray[i];
			
			while (cookie.charAt(0) === ' ') {
				cookie = cookie.substring(1, cookie.length);
			}
			
			if (cookie.indexOf(nameEQ) === 0) {
				return cookie.substring(nameEQ.length, cookie.length);
			}
		}
		return null;
	},
	
	remove = function(name) {
		create(name, "", -1);
	};

	
	/*** PUBLIC ***/
	
	space.create = function(name, value, minutes) {
		create(name, value, minutes);
	},

	space.read = function(name) {
		return read(name);
	},

	space.remove = function(name) {
		remove(name);
	};
	
	return space;
}());
