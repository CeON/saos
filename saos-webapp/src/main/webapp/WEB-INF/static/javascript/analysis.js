
	

var initAnalysisJs = function() {	

	
	initButtonActions();
	
	
	function initButtonActions() {
		initAddNewSearchPhraseButton();
		initDeleteSearchPhraseButtons();
	}
	
	
	/****************** REMOVAL OF PHRASE DIV **/
	
	function initDeleteSearchPhraseButtons() {
	
		$('[id^=deletePhraseButton_]').click(function() {
			
			// TODO: delete tooltip
			
			deleteSearchPhrase($(this).attr('id').split('_')[1]);
	        
		});
	}
	
	
	function deleteSearchPhrase(phraseIndexToRemove) {
		
		ajaxAction('searchCriteriaIndexToRemove', phraseIndexToRemove);
	    
	}
	
	
	
	/****************** ADDING NEW PHRASE DIV **/
	
	function initAddNewSearchPhraseButton() {
		
		$('#addPhraseButton').click(function(){
		
			  addNewSearchPhrase();
		  
		});
	}
	
	
	function addNewSearchPhrase() {
		
		ajaxAction('addNewSearchCriteria', "");
	}
	
	
	/****************** COMMON **/
    
    function ajaxAction(actionParamName, actionParamValue) {
        
        $('#analysisForm').append($('<input>').attr('id', actionParamName).attr('type', 'hidden').attr('name', actionParamName).val(actionParamValue));
        $('#analysisForm').attr('method', 'post');
        
        $('#analysisForm').ajaxSubmit(function(view) {
            $('#analysisSearchCriteriaDiv').html(view);  
        
            $('#'+actionParamName).remove();
            $('#analysisForm').attr('method', 'get');
            
            initButtonActions();
            
        });
    }
	
	
	
	
	<!--$('input[id="daterange"]').daterangepicker();-->
	

}