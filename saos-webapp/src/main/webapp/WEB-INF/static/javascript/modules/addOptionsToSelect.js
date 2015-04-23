/*
 * Add options to select field.
 * 
 * @author Łukasz Pawełczak
 * 
 * 
 * 
 * @param options - object
 * {
 *     url - required - url where options data can be found 
 *     selectId - required - id of select 
 *     createOption - not required - function that creates option to insert into select
 * }
 * 
 */
function addOptionsToSelect(options) {
    
     /*
     * Creates option and fills it with value and text.
     * 
     * @param element - variable that contains value & text
     * @return string option with value and text
     */
    var createOption = function(element) { 
        return "<option value='" + element.id + "' >" + element.name + "</option>";
    }, 
    
    
    init = function() {
        if (options.createOption !== undefined && typeof options.createOption === "function") {
            createOption = options.createOption;
        }
    },
    
    
    /*
     *  Finds options and adds them to select.
     */
    findAndAddOptions = function() {
    
        
        $.ajax(options.url)
        .done(function(data) {
            
            var selectOptions = "";
            
            for (var i = 0, length = data.length; i < length; i += 1) {
                selectOptions += createOption(data[i]);
            }
            
            //load options into select
            $(options.selectId).append(selectOptions).removeAttr("disabled");
            
        })
        .fail(function() {
            console.log("Cannot load data for field" + options.selectId);
        });
        
        
    }
    
    
    //run methods
    init();
    findAndAddOptions();
    
}

