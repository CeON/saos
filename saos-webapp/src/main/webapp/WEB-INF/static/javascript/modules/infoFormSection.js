/*
 * Info Form section
 * 
 * @author Łukasz Pawełczak
 * 
 * 
 * @param userOptions - object with structure
 * 
 *  {
 *      formSectionId - id of form section 
 *      infoSectionId - id of info section
 *      defaultInfoSectionText - not required - text displayed in info section when there is no selected fields
 *      extractInfoFromFormCustom - function - if set then it is used for extracting info from form section
 *  }
 * 
 */
function InfoFormSection(options) {
   
    
    
    init();
    
    
    //------------------------ FUNCTIONS --------------------------
    
    function init() {
        updateInfoSection();
        bindOpenFormSection();
    }
    
    
    /* 
     * Assigns buttons to open form section on event click.
     */ 
     function bindOpenFormSection() {
    
        var $formSection = $(options.formSectionId);
        
        $(options.infoSectionId).on(createCustomEventName("click"), function(event) {    
                
            event.preventDefault();
            
            unBindOpenFormSection();
            bindCloseFormSectionEventHandler();
            
            $(options.infoSectionId).addClass("info-section-opened");
            
            $formSection.slideDown(400, function() {});
        });
        
     }
     
     function unBindOpenFormSection() {
         
         $(options.infoSectionId).off(createCustomEventName("click"));
         
     }
     
     /* 
      * Assigns buttons to close form section on event click.
      */ 
     function bindCloseFormSectionEventHandler() {
         
         $(document).on(createCustomEventName("mouseup"), null, [options], closeFormSectionIfClickedOutside);

     }
     
     function unBindCloseFormSectionEventHandler() {
         
         $(document).off(createCustomEventName("mouseup"));
         
     }
     
     /* Creates custom event name
      * 
      * @param eventName
      * @return string event name + optionsInfoSectionId
      */
     function createCustomEventName(eventName) {
         return eventName + "." + (options.infoSectionId).substr(1, options.infoSectionId.length);
     }
     
     /**
      * Closes form section if the user clicked outside it
      * e.data[0].formSectionId = the id of the form section prefixed with #
      * e.data[1].infoSectionId = the id of the info section prefixed with #
      */
     function closeFormSectionIfClickedOutside(e) {

         var $formSection = $(e.data[0].formSectionId),
         $setSection = $(e.data[0].infoSectionId),
         $datepicker = $("#ui-datepicker-div");
     
     
         if ($("#datepicker_from-error").css("display") === "block" || $("#datepicker_to-error").css("display") === "block") {
             return;
         }

         if (!$formSection.is(e.target)  // if the target of the click isn't the container...
                 && $formSection.has(e.target).length === 0 && $setSection.has(e.target).length === 0 && // ... nor a descendant of the container
                 !$datepicker.is(e.target) && $datepicker.has(e.target).length === 0) // ... nor a datepicker
         {
             closeFormSection();
             unBindCloseFormSectionEventHandler();
             
             //binds open form section handler after 200ms
             setTimeout(bindOpenFormSection, 200);
         }
         
     }
     
     /*
      * Closes form section.
      * 
      */
     function closeFormSection() {

         $(options.infoSectionId).removeClass("info-section-opened");
         
         //Hide form section
         $(options.formSectionId).slideUp(400, function() {
             updateInfoSection();
         });
         
     }
     
     /* 
      * Extracts and formats info from inputs & selects inside form section.
      *      
      * @return string with inputs & selects values   
      */
     function extractInfoFromFormSection() {
         
         var $formSection = $(options.formSectionId),
             html = "",
             comma = false;
         
         
         $formSection.find("input:text, input:hidden, select").each(function() {
             var $this = $(this),
                 fieldDescription = $this.attr("data-field-desc") || "";
             
             
             /*
              * Doeasn't check hidden input's that name starts with '_'.
              */
             if ($this.is("input:hidden") && $this.attr("name") !== undefined
                 && $this.attr("name")[0] === "_") {
                 return;
             }
             
             
             if ($this.is("input:radio") || $this.is("input:checkbox")) {
                 
                 var value = "",
                     id = $this.attr("id");
             
                 if (!$this.is(":checked")) {
                     return;
                 }
                 
                 if (id !== "") {
                     value += $("label[for=" + id + "]").text();
                 }
                 
                 html += addPhrase(value, fieldDescription, comma);
                 comma = true;
                 
                   
             } else if ($this.is("input")) {
                 var value = $this.val();
                 
       
                 /*
                  * Date fields should be handled differently.
                  */
                 if ($this.attr("id") === "datepicker_to") {
                     var dateFrom = $("#datepicker_from").val(),
                         dateTo = $("#datepicker_to").val();
                     
                     
                     if (dateFrom !== "" && dateTo !== "") {
                         html += "<b>" + parseDate(dateFrom) + "</b> - <b>" + parseDate(dateTo) + "</b>";
                     } else if (dateFrom !== "" && dateTo === "") { 
                         html += springMessage.from + ": <b>" + parseDate(dateFrom) + "</b>";
                     } else if (dateFrom === "" && dateTo !== "") { 
                         html += springMessage.to + ": <b>" + parseDate(dateTo) + "</b>";
                     }
                     
                     return;
                     
                     
                 } else if ($this.attr("id") === "datepicker_from") {
                     return;
                 }
                 
                 
                 if (value === "" || $this.attr("name") === undefined) {
                     return;
                 }
                 

                 if ($this.attr("id") === "lawJournalEntryId") {
                     var lawJournalText = $("#law-journal-navigation").find("> div > span").text();
                     
                     if (lawJournalText.length > 60) {
                         lawJournalText = lawJournalText.substr(0, 60) + " ...";
                     }
                     
                     html += addPhrase(lawJournalText, fieldDescription, comma);
                     comma = true;
                 } else {
                     html += addPhrase($this.val(), fieldDescription, comma);
                     comma = true;
                 }
                 
                 
                     
             } else if ($this.is("select")) {
                 
                 var $optionSelected = $this.find("option:selected"); 
                 
                 if ($optionSelected.index() > 0) {
                     
                     html += addPhrase($optionSelected.text(), fieldDescription, comma);
                     comma = true;
                     
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
             return "<b>" + phrase.trim() + "</b>";
         }
         
         function parseDate(date) {
             return moment(date, 'DD-MM-YYYY', true).locale('pl').format("LL");
         }
     }
     
     function updateInfoSection() {
         var html = "";
         
         if (options.extractInfoFromFormCustom !== undefined) {
             html = options.extractInfoFromFormCustom();
         } else {
             html = extractInfoFromFormSection();
         }
         
         if (html !== "") {
             $(options.infoSectionId).html(html);
         } else {
             
             if (options.defaultInfoSectionText !== undefined) {
                 $(options.infoSectionId).html(options.defaultInfoSectionText);
             }
             
         }
     }
}


function infoFormSection(options) {
    return new InfoFormSection(options);
}




