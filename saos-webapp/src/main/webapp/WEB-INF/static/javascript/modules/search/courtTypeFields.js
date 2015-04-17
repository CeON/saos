/*
 * Module for showing/hiding form fields depending on court type.
 * 
 * @author Łukasz Pawełczak
 * 
 */
var CourtTypeFields = (function() {
    
    var scope = {},
    
    defaultOptions = {
        formId: "#search-form",
        FIELD_NAME: "courtType",
        ATTR_COURT_TYPE: "data-court-type"
    },
    options = {},
    
    
    getSelectedCourtType = function() {
        return $("[name=" + options.FIELD_NAME + "]:checked").val();
    },
    
    showSelectedFields = function() {
        updateForm(getSelectedCourtType());
    },
    
    assignChangeCourtTypeButtons = function() {
        
        $("[name=" + options.FIELD_NAME + "]").each(function() {
            var $item = $(this);
            
            //closure for storing courtType value
            (function() {
                var courtType = $item.val();
                
                $item.click(function() {
                    updateForm(courtType); 
                });
                
            })();
            
        });
        
    },
    
    /*
     * Show fields that have attribute @{ATTR_COURT_TYPE} value equal to @param courtType.
     * Hide fields that don't have attribute equal to courtType.
     * 
     * @param courtType - type of court
     */
    updateForm = function(courtType) {
        
        $("[" + options.ATTR_COURT_TYPE + "]").each(function() {
            var $this = $(this)
                attr = $this.attr(options.ATTR_COURT_TYPE);
            
            if (attr === "" || attr === courtType) {
                $this.show();
            } else {
                $this.hide();
            }
        });
        
    },
    
    /* 
     * Clears fields that are not related to selected court type.   
     * On submit clear fields that not associated with selected court type. 
     * */
    assignSubmitEvent = function() {
        $(options.formId)
        .on("submit", function(event) {
            clearNotRelatedFields(event);
        })
        .on("clearCourtType", function(event) {
            clearNotRelatedFields(event);
        });
        
        function clearNotRelatedFields(event) {
            $("[" + options.ATTR_COURT_TYPE + "]").each(function() {
                
                var $this = $(this),
                    attr = $this.attr(options.ATTR_COURT_TYPE);
                
                if (attr != getSelectedCourtType()) {
                    clearField($this);
                }

            });
        }
    },

    /*
     * Clears field in container
     */
    clearField = function($fieldsContainer) {
        $fieldsContainer.find("input, select").each(function() {
            $(this)
                .val('')
                .removeAttr('checked')
                .removeAttr('selected')
                .trigger("change");
        });
    };
    
    
    scope.init = function(userOptions) {
        
        options = userOptions || defaultOptions;
        
        showSelectedFields();
        assignSubmitEvent();
        assignChangeCourtTypeButtons();
    };
    
    return scope;
    
})();


