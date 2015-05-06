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
 */
function extractInfoFromCourtSection(options) {
    
    var $formSection = $(options.formSectionId),
    html = "",
    comma = false;


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
            
            html += addPhrase(value, fieldDescription, comma);
            comma = true;
            
              
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
 */
 function extractInfoFromJudgmentFormSectionInSearch(options) {
     
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


