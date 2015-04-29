/*
 * Module with operations on courtCriteriaForm, see: courtCriteriaFormSection.jsp
 * 
 * @author Łukasz Pawełczak
 * 
 */
var CourtCriteriaForm = (function() {
    
    var scope = {},
    
    ATTR_COURT_TYPE = "data-court-type",
    
    
    //------------------------ FUNCTIONS --------------------------
    
    getSelectedCourtType = function() {
        return $("[id^=courtType_]:checked").val();
    },
    
    showSelectedFields = function() {
        refreshForm(getSelectedCourtType());
    },
    
    assignCourtTypeChangeButtons = function() {
        
        $("[id^=courtType_]").each(function() {
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
    
    
    function initCourtSelects() {
        
        $("#courtType_COMMON").one("click", function() {
            
            //inits common court select with options
            addOptionsToSelect({
                selectId: "#select-common-court",
                url: contextPath + "/cc/courts/list"
            });
            
        });
            
        $("#courtType_SUPREME").one("click", function() {
        
            //inits supreme chamber select with options
            addOptionsToSelect({
                selectId: "#select-supreme-chamber",
                url: contextPath + "/sc/chambers/list"
            });
        
        
        });
        
        //Search form - init select court & division
        CourtDivisionSelect.run({
            fields: [{  court: "#select-common-court",
                        divisionId: "#select-common-division",
                        getDivisionUrl: function(id) {return contextPath + "/cc/courts/{id}/courtDivisions/list".replace("{id}", id); }
                     },
                     {  court: "#select-supreme-chamber",
                        divisionId: "#select-supreme-chamber-division",
                        getDivisionUrl: function(id) {return contextPath + "/sc/chambers/{id}/chamberDivisions/list".replace("{id}", id); }
                    }]
        });

    }
    
    
    
    scope.init = function() {
        
        showSelectedFields();
        assignCourtTypeChangeButtons();
        initCourtSelects();
    };
    
    return scope;
    
})();


