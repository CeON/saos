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
                url: contextPath + "/cc/courts/list",
                createOption: createCcCourtOption
            });
            
        });
            
        $("#courtType_SUPREME").one("click", function() {
        
            //inits supreme chamber select with options
            addOptionsToSelect({
                selectId: "#select-supreme-chamber",
                url: contextPath + "/sc/chambers/list"
            });
        
        
        });
        
        var createCcCourtOption = function(court) {
            return $('<option/>').attr("value", court.id).attr("data-cc-court-type", court.type).text(court.name).prop('outerHTML');
            
        }
        
        $('#ccIncludeDependentCourtJudgments').change(function() {
            var $commonCourtDivisionSelect = $("#select-common-division");
            
            if ($(this).is(":checked")) {
                $commonCourtDivisionSelect.find(":selected").removeAttr("selected");
                $commonCourtDivisionSelect.attr("disabled", "disabled");
                
                hideCcDivisionSearchCriteriaLink();
            } else {
                $commonCourtDivisionSelect.removeAttr("disabled");
                
                showCcDivisionSearchCriteriaLink();
            }
        });
        
        if($("#ccIncludeDependentCourtJudgments").is(":checked")) {
            hideCcDivisionSearchCriteriaLink();
        } else {
            showCcDivisionSearchCriteriaLink();
        }
        
        
        $("#select-common-court").change(function() {
            
            var $selectedCourt = $(this).find("option:selected");
            var selectedCourtId = $selectedCourt.attr("value");
            var $commonCourtDivisionSelect = $("#select-common-division");
            var $ccIncludeDependentCourtJudgmentsCheckbox = $('#ccIncludeDependentCourtJudgments');

            if (selectedCourtId !== "") {
                $.ajax(contextPath + "/cc/courts/"+selectedCourtId+"/courtDivisions/list")
                     .done(function(data) {
                         
                         var options = "";
                         
                         for(var j = 0, dataLength = data.length; j < dataLength; j += 1) {
                             options += createDivisionOption(data[j].id, data[j].name);
                         }
                         
                         $commonCourtDivisionSelect.find("option:gt(0)").remove();
                         $commonCourtDivisionSelect.append(options);
                         
                         if ($selectedCourt.attr("data-cc-court-type") === 'DISTRICT') {
                             $commonCourtDivisionSelect.removeAttr("disabled");
                             $ccIncludeDependentCourtJudgmentsCheckbox.prop("checked", false).trigger("change");
                             $ccIncludeDependentCourtJudgmentsCheckbox.attr("disabled", "disabled");
                         } else {
                             $ccIncludeDependentCourtJudgmentsCheckbox.removeAttr("disabled");
                             $ccIncludeDependentCourtJudgmentsCheckbox.val("true");
                             if (!$("#select-common-court").is(':focus')) {
                                 $ccIncludeDependentCourtJudgmentsCheckbox.focus();
                             }
                             if (!$ccIncludeDependentCourtJudgmentsCheckbox.is(":checked")) {
                                 $commonCourtDivisionSelect.removeAttr("disabled");
                                 showCcDivisionSearchCriteriaLink();
                             } else {
                                 $commonCourtDivisionSelect.prop("disabled", "disabled");
                             }
                         }
                         
                     
                     })
                     .fail(function() {});
                } else {
                    $commonCourtDivisionSelect.prop("disabled", "disabled").find("option:gt(0)").remove();
                    $ccIncludeDependentCourtJudgmentsCheckbox.prop("checked", false).trigger("change");
                    $ccIncludeDependentCourtJudgmentsCheckbox.attr("disabled", "disabled");
                    
                }
            });
            
        
        var createDivisionOption = function(id, name) { 
            return "<option data-content='" + name + "' value='" + id + "' >" + name + "</option>";
        };
        
        
        
    
    //Search form - init select court & division
    CourtDivisionSelect.run({
        fields: [
                 {  court: "#select-supreme-chamber",
                    divisionId: "#select-supreme-chamber-division",
                    getDivisionUrl: function(id) {return contextPath + "/sc/chambers/{id}/chamberDivisions/list".replace("{id}", id); }
                 }]
        });

        function showCcDivisionSearchCriteriaLink() {
            $(".division").each(function() {
                $(this).css("display", "inline");
            });
            
            $(".not-link-division").each(function() {
                $(this).css("display", "none");
            });
        }
    
        function hideCcDivisionSearchCriteriaLink() {
            $(".division").each(function() {
                $(this).css("display", "none");
            });
            
            $(".not-link-division").each(function() {
                $(this).css("display", "inline");
            });
        }
    }
    
    
    
    scope.init = function() {
        
        showSelectedFields();
        assignCourtTypeChangeButtons();
        initCourtSelects();
    };
    
    return scope;
    
})();


