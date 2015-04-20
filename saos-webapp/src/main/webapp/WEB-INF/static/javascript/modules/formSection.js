/*
 * Form section
 * 
 * @author Łukasz Pawełczak
 * 
 * 
 * @param userOptions - object with structure
 * 
 *  {
 *      formSectionId - id of form section 
 *      infoSectionId - id of info section
 *  }
 * 
 */
function FormSection(userOptions) {
   
    
    
    var defaultOptions = {
        formSectionId: "",
        infoSectionId: ""
    };
    
    var options = userOptions || defaultOptions;
    
    init();
    
    
    //------------------------ FUNCTIONS --------------------------
    
    function init() {
        showPublishedValues();
        initOpenFormSection();
        initCloseFormSection();
    }
    
    
    /* 
     * Assigns buttons to open form section on event click.
     */ 
     function initOpenFormSection() {
    
        var $formSection = $(options.formSectionId);
        
        $(options.infoSectionId).click(function(event) {    
            
            event.preventDefault();
            
            $formSection.slideDown(400, function() {});
        });
        
    }
     
     /* 
      * Assigns buttons to close form section on event click.
      */ 
     function initCloseFormSection() {
         
         var $formSection = $(options.formSectionId),
             $setSection = $(options.infoSectionId);
         
         $(document).mouseup(function(e) {
             
             if (!$formSection.is(e.target) && !$setSection.is(e.target) // if the target of the click isn't the container...
                     && $formSection.has(e.target).length === 0 && $setSection.has(e.target).length === 0) // ... nor a descendant of the container
             {
                 closeFormSection();
             }
             
         });
     }
     
     /*
      * Closes form section
      */
     function closeFormSection() {
         var $formSection = $(options.formSectionId);

         $formSection.find(".form-group").each(function() {
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
         
         
         //Hide form section
         $formSection.slideUp(400, function() {
             var html = publishInfoFromFormSection(); 
             
             if (html !== "") {
                 $(options.infoSectionId).html(html);
             }
         });
         
     }
     
     /* 
      * Publishes form information
      *      
      * @return string with inputs & selects values   
      */
     function publishInfoFromFormSection() {
         
         var $formSection = $(options.formSectionId),
             html = "";
         
         $formSection.find("input:text, input:hidden, select").each(function() {
             var $this = $(this),
                 fieldDescription = $this.attr("data-field-desc") || "";
             
             
             if ($this.is("input:radio")) {
                 
                 var value = "",
                     id = $this.attr("id");
             
                 if (!$this.is(":checked")) {
                     return;
                 }
                 
                 if (id !== "") {
                     value += $("label[for=" + id + "]").text();
                 }
                 
                 html += addPhrase(value, fieldDescription, false);
                 
             } else if ($this.is("input")) {
                 
                 html += addPhrase($this.val(), fieldDescription, true);
                     
             } else if ($this.is("select")) {
                 
                 var $optionSelected = $this.find("option:selected"); 
                 
                 if ($optionSelected.index() > 0) {
                     
                     html += addPhrase($optionSelected.text(), fieldDescription, true);
                 }
             }
             
         });
         
         
         return html;
         
         
         function addPhrase(text, fieldDescription, separator) {
             var html = ""
             
             if (text !== "") {

                 if (separator) {
                     html += ", ";
                 }
                 html += fieldDescription;
                 html += boldPhrase(text);
             }
             
             return html;
         }
         
         function boldPhrase(phrase) {
             return "<b>" + phrase + "</b>";
         }
     }
     
     function showPublishedValues() {
         var valuesToPublish = publishInfoFromFormSection(); 
         
         if (valuesToPublish !== "") {
             
             $(options.infoSectionId).html(valuesToPublish);
         }
         
     }
}


function formSection(options) {
    return new FormSection(options);
}




