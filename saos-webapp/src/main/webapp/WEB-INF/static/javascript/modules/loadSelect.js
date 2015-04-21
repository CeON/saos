/*
 * Load with options.
 * 
 * @author Łukasz Pawełczak
 */
function LoadSelect(userOptions) {
    
    var defaultOptions = {
        selectId: "",
        url: "",
        firstOption: {
            text: "",
            value: ""
        }
    },
    options = {},
    
    options = userOptions || defaultOptions;
    
    this.load = function() {
    
        $.ajax(options.url)
        .done(function(data) {
            
            var html = prepareOption(options.firstOption.text, options.firstOption.value);
            
            for (var i = 0, length = data.length; i < length; i += 1) {
                html += prepareOption(data[i].name, data[i].id);
            }
            
            //load options into select
            $(options.selectId).html(html).removeAttr("disabled");
            
        })
        .fail(function() {
            console.log("Cannot load data for field" + options.selectId);
        });
        
        
        function prepareOption(text, value) { 
            return "<option value='" + value + "' >" + text + "</option>";
        }
    }
    
}


function loadSelect(options) {
    new LoadSelect(options).load();
}

