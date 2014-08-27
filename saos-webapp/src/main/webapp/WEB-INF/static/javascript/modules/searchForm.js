var SearchForm = {


	init: function() {
	
		SearchForm.assign();
	},

	assign: function() {
		$("#search-more-fields").on("click", function() {
	    	$("#advance-form").slideToggle();
	    	
	    	//$("#advance-search").css("display", "block");
	    	
	    });
	},


	show: function() {
		
	},
};