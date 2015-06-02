/**
 * Functions for extracting and formating info from inputs & selects inside form section.
 */


/****************** JUDGMENT SEARCH EXTRACTION *******/

function addPhrase(text, fieldDescription, separator) {
     var html = ""
     
     if (text !== "") {
         
         if (separator) {
             html += ", ";
         }
         html += fieldDescription;
         html += "<b>" + text.trim() + "</b>";
     }
     
     return html;
}

/**
 * Extracts info from court section
 * 
 * formSectionId the id of the form section preceded by #
 * 
 */
function extractInfoFromCourtSection(formSectionId) {
    
    var $formSection = $(formSectionId),
    html = "",
    comma = false;


    $formSection.find("input:text, input:hidden, select").each(function() {
        var $this = $(this),
            fieldDescription = $this.attr("data-field-desc") || "";
    

        if ($this.attr("id") === "ccIncludeDependentCourtJudgments") {
            return;
        }
        

        if ($this.is("input:radio")) {
            
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
            
              
        } else if ($this.is("select")) {
            
            var $optionSelected = $this.find("option:selected"); 
            
            if ($optionSelected.index() > 0) {
                
                var optionSelectedText = $optionSelected.text();
                
                if ($this.attr("id") === "select-common-court") {
                    if ($("#ccIncludeDependentCourtJudgments").is(":checked")) {
                        optionSelectedText = optionSelectedText + " " + $("#ccIncludeDependentCourtJudgments").attr("data-info-section-custom-text");
                    }
                }
                
                
                html += addPhrase(optionSelectedText, fieldDescription, comma);
                comma = true;
                
            }
        }
    });
    
    return html;
}

/**
 * Extracts info from date section in judgment search form.
 */
function extractInfoFromDateSectionInSearch() {
    
    var html = "",
        dateFrom = $("#datepicker_from").val(),
        dateTo = $("#datepicker_to").val();

    
    if (dateFrom !== "" && dateTo !== "") {
        html += "<b>" + parseDate(dateFrom) + "</b> - <b>" + parseDate(dateTo) + "</b>";
    } else if (dateFrom !== "" && dateTo === "") { 
        html += springMessage.from + ": <b>" + parseDate(dateFrom) + "</b>";
    } else if (dateFrom === "" && dateTo !== "") { 
        html += springMessage.to + ": <b>" + parseDate(dateTo) + "</b>";
    }

    return html;
    
    
    function parseDate(date) {
        return moment(date, 'DD-MM-YYYY', true).locale('pl').format("LL");
    }
}


/**
 * Extracts info from section judgment extra search fields.
 * 
 * formSectionId the id of the form section preceded by #
 * 
 */
 function extractInfoFromJudgmentFormSectionInSearch(formSectionId) {
     
     var $formSection = $(formSectionId),
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
        
                
             if (value === "" || $this.attr("name") === undefined) {
                 return;
             }
             
             //omits referencedCourtCaseId field
             if ($this.attr("id") === "referencedCourtCaseId") {
                 return;
             }

             if ($this.attr("id") === "lawJournalEntryCode") {
                 var lawJournalText = $("#law-journal-navigation").find("> div > span").text().replace(/\s+|\s+/g, ' ');
                 
                 if (lawJournalText.length > 50) {
                     lawJournalText = lawJournalText.substr(0, 50) + " ...";
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
 }
 

/****************** ANALYSIS EXTRACTION *******/

/**
 * Extracts information from judgmentDateRangeSelectDiv inputs.
 */
function extractJudgmentDateRangeFromAnalysisFormSection() {
    return '<b>' + $('#judgmentDateStartMonth option:selected').text() + ' ' +
                   $('#judgmentDateStartYear').val() + '</b> - <b>' +
                   $('#judgmentDateEndMonth option:selected').text() + ' ' +
                   $('#judgmentDateEndYear').val() + '</b>';
    
}


