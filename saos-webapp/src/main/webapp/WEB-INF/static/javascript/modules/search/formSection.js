/*
 * Form sections
 * 
 * @author Łukasz Pawełczak
 * 
 */

var FormSection = (function() {
    
    var scope = {},
    
        options = {
            formSectionId: "",
            openSectionButtonId: "",
            selectedValuesSectionId: "",
            infoSectionId: "",
            infoSectionValueId: ""
        },
    
    
    /* 
     * Assigns buttons to open form section on event click.
     */ 
    assignOpenSection = function() {
    
        var $formSection = $(options.formSectionId);
        
        $(options.openSectionButtonId).click(function(event) {    
            
            event.preventDefault();
            
            $formSection.slideDown(400, function() {});
        });
        
        $(options.selectedValuesSectionId).click(function(event) {

            event.preventDefault();
            
            $(options.selectedValuesSectionId).fadeOut();
            
            $formSection.slideDown(400, function() {});
            
        });
        
    },
    
    
    
    /* 
     * Assigns buttons to close form section on event click.
     */ 
    assignCloseSection = function() {
        
        var $formSection = $(options.formSectionId),
            $setSection = $(options.openSectionButtonId);
        
        $(document).mouseup(function(e) {
            
            if ($formSection.css("display") !== "none" // if the target of the click isn't the container...
                    && $formSection.has(e.target).length === 0 && $setSection.has(e.target).length === 0// ... nor a descendant of the container
                    && !$setSection.is(e.target)) {
            
                closeSection();
            }
            
        });
    },
    
    
    
    /* 
     * Publishes form information
     * 
     * @param publishDefault - boolean - publish default values for radio input
     *      When true function considers default values of radio group.
     *      
     * @return string with inputs & selects values   
     */
    publishInfoFromFormSection = function(publishDefault) {
        
        var $formSection = $(options.formSectionId),
            html = "",
            comma = false;
        
        $formSection.find("input:text, input:hidden, select").each(function() {
            var $this = $(this);
            
            if ($this.is("input:radio")) {
                
                var value = $this.val(),
                    id = $this.attr("id");
            
                if (!$this.is(":checked")) {
                    return;
                }
                
                if ($this.attr("id") !== "" && ($this.val() !== "" || publishDefault)) {
                    
                    if (comma) {
                        html += ", ";
                    }

                    html += boldPhrase($("label[for=" + id + "]").text());
                    comma = true;
                }
                
            } else if ($this.is("input")) {
                
                html += addPhrase($this.val(), comma);
                    
            } else if ($this.is("select")) {
                
                var $optionSelected = $this.find("option:selected"); 
                
                if ($optionSelected.index() > 0) {
                    
                    html += addPhrase($optionSelected.text(), comma);
                }
            }
            
        });
        
        
        return html;
        
        
        function addPhrase(text, comma) {
            var html = ""
            
            if (text !== "") {

                if (comma) {
                    html += ", ";
                }
                
                html += boldPhrase(text);
                comma = true;
            }
            
            return html;
        }
        
        function boldPhrase(phrase) {
            return "<b>" + phrase + "</b>";
        }
    },
    
    
    
    /*
     * Closes form section
     */
    closeSection = function() {
        var $formSection = $(options.formSectionId);

        $formSection.find("> div").each(function() {
            var $this = $(this);
            
            if ($this.css("display") === "none") {
                //Remove values from text inputs
                $this
                    .find('input:text, input:hidden, input:password, input:file, select, textarea')
                    .val('')
                    .trigger('clear');
                
                //Select first element on select list
                $this
                    .find('select')
                    .selectedIndex=0;
            }
            
        });
        
        //Hides button "open section" & shows selected value section
        $(options.openSectionButtonId).fadeOut(400, function() {
            $(options.selectedValuesSectionId).fadeIn();
            $(options.infoSectionId).fadeIn();
        });
        
        //Hide form section
        $formSection.slideUp(400, function() {
            var html = publishInfoFromFormSection(true); 
            
            if (html !== "") {
                $(options.infoSectionValueId).html("<p>" + html + "</p>");
            }
            
        });
        
    },
    
    
    
    showPublishedValues = function() {
        var valuesToPublish = publishInfoFromFormSection(false); 
        
        if (valuesToPublish !== "") {
            
            $(options.infoSectionValueId).html("<p>" + valuesToPublish + "</p>");
            
            $(options.openSectionButtonId).hide();
            
            $(options.infoSectionId).show();
        }
        
    };
    
    
    
    //------------------------ PUBLIC --------------------------
    
    /*
     * Init 
     * 
     * @param userOptions - object with fields:
     * 
     *      formSectionId - id of form section
     *      openSectionButtonId -id of button that opens section
     *      infoSectionId - id of info section
     *      infoSectionValueId - id of info section value
     *      selectedValuesSectionId - id of element that holds value of selected fields 
     */
    scope.init = function(userOptions) {
        
        options = userOptions || {};
        
        showPublishedValues();
        
        assignOpenSection();
        assignCloseSection();
    };
    
    
    return scope;
    
})();


