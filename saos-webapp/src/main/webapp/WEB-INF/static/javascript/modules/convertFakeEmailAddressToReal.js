/*
 * Converts all dummy email addresses to real ones.
 * 
 * @author Łukasz Pawełczak
 * 
 */
function ConvertFakeEmailAddressToReal(options) {
	
	var defaultOptions = {
		addressTag: ".dummy-mail",
		
		itemsToReplace: [
             {
				fake: "_AT_",
				real: "@"
			},
			{
				fake: "_DOT_",
				real: "."
			}
		]
	};
	
	
	var options = options || defaultOptions;
	
	convert();
	
    //------------------------ FUNCTIONS --------------------------
	
	/* Search for all e-mail items on page(specified in options.addressTag).
	 * Replace all fake text to real one.
	 * 
	 * example:
	 * 
	 *    sierotka_AT_marysia_DOT_edu_DOT_pl --> sierotka@marysia.edu.pl
	 */ 
	function convert() {
		
		$(options.addressTag).each(function() {
			var $this = $(this),
				address = $this.text(),
				items = options.itemsToReplace;
			
			for(var i = 0, length = items.length; i < length; i += 1) {
				address = address.replace(new RegExp(items[i].fake, "g"), items[i].real);
			}
			
			$this.attr("href", "mailto:" + address);
			$this.text(address);
		});
		
	}
	
}


