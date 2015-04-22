/*
 * Module for showing/hiding form fields depending on court type.
 * 
 * @author Łukasz Pawełczak
 * 
 */
var CourtTypeFields = (function() {
    
    var scope = {},
    
    FIELD_NAME = "courtType",
    ATTR_COURT_TYPE = "data-court-type",
    
    
    //------------------------ FUNCTIONS --------------------------
    
    getSelectedCourtType = function() {
        return $("[name=" + FIELD_NAME + "]:checked").val();
    },
    
    showSelectedFields = function() {
        refreshForm(getSelectedCourtType());
    },
    
    assignChangeCourtTypeButtons = function() {
        
        $("[name=" + FIELD_NAME + "]").each(function() {
            var $item = $(this);
            
            //closure for storing courtType value
            (function() {
                var courtType = $item.val();
                
                $item.click(function() {
                    refreshForm(courtType); 
                });
                
            })();
            
        });
        
    },
    
    /*
     * Show fields that have attribute @{ATTR_COURT_TYPE} value equal to @param courtType.
     * Hide fields that don't have this attribute equal to courtType.
     * 
     * @param courtType - type of court
     */
    refreshForm = function(courtType) {
        
        $("[" + ATTR_COURT_TYPE + "]").each(function() {
            var $this = $(this)
                attr = $this.attr(ATTR_COURT_TYPE);
            
            if (attr === "" || attr === courtType) {
                $this.show();
                
                //Reset width of the common court keywords field
                $("#input-search-keywords-cc").suggesterRefresh();
            } else {
                $this.hide();
                clearField($this);
            }
        });
        
    },
    

    /*
     * Clears field in container
     */
    clearField = function($fieldsContainer) {
        $fieldsContainer.find("input, select").each(function() {
            var $field = $(this);
            
            $field
                .val('')
                .removeAttr('checked')
                .removeAttr('selected')
                .trigger("change");
            
            //Remove suggestion boxes (cc-keywords)
            $(".suggestion").remove();
        });
    };
    
    
    scope.init = function() {
        
        showSelectedFields();
        assignChangeCourtTypeButtons();
    };
    
    return scope;
    
})();


