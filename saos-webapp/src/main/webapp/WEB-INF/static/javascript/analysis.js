
    

var initAnalysisJs = function() {    

    var mainChart = null;
    var isZoomed = false;
    
    // based on jquery.colors Colour.names
    var colours = [
                   "#0000ff", // blue
                   "#ff0000", // red
                   "#008000", // green
                   "#000000", // black
                   "#ffa500", // orange
                   "#800080", // purple
                   "#a9a9a9", // darkgrey
                   "#00ffff", // cyan
                   "#a52a2a", // brown
                   "#808000", // olive
                   "#ffff00", // yellow
                   "#ffc0cb", // pink  
                   "#008b8b", // darkcyan
                   "#000080", // navy
                   "#90ee90", // lightgreen
                   "#bdb76b", // darkkhaki
                   "#556b2f", // darkolivegreen
                   "#ff8c00", // darkorange
                   "#9932cc", // darkorchid
                   "#ff00ff", // magenta
                   "#8b0000", // darkred
                   "#e9967a", // darksalmon
                   "#9400d3", // darkviolet
                   "#ff00ff", // fuchsia
                   "#8b008b", // darkmagenta
                   "#006400", // darkgreen
                   "#ffd700", // gold
                   "#4b0082", // indigo
                   "#f0e68c", // khaki
                   "#add8e6", // lightblue
                   "#e0ffff", // lightcyan
                   "#d3d3d3", // lightgrey
                   "#ffb6c1", // lighpink
                   "#ffffe0", // lightyellow
                   "#00ff00", // lime
                   "#800000", // maroon
                   "#00ffff", // aqua
                   "#00008b", // darkblue
                   "#f5f5dc", // beige
                   "#c0c0c0"  // silver
                   //"#ffffff"  // white
                   //"#f0ffff", // azure
                
               	   ];
    
    
    
    initFormElements();
    
    generateCharts(false);
    
    window.onpopstate = function(e){
        location.reload();
    };

    
    
    //------------------------------- FUNCTIONS ----------------------------------
    
    
    
    /**
     * Initializes certain analysis form elements
     */
    function initFormElements() {
        
        tieMonthYearRangeSelects("xRangeStartMonth", "xRangeStartYear", "xRangeEndMonth", "xRangeEndYear");

        initButtons();
        
        initInputs();

        colourPhraseInputs();
        
        //Enables bootstrap tooltip
        $('#analysisForm [data-toggle="tooltip"]').tooltip({container: 'body'});

    }
    
    
    /**
     * Inits buttons of the analysisForm  
     */
    function initButtons() {
        initAddNewSearchPhraseButton();
        initDeleteSearchPhraseButtons();
    }
    
    /**
     * Returns a colour for the specified index from colours array. A colour is always retrieved even if the
     * given index in bigger than colours.length (uses modulo inside)
     */
    function getColour(index) {
    	
        return colours[index % colours.length];
    	
    }
    
    /**
     * Paints phrase input boxes with the same colours as corresponding series.
    */
    function colourPhraseInputs() {
        
        $('[id^=inputColourBox_]').each(function() {
            $(this).css("background-color", getColour(extractIndex($(this))));
        });
        
    }
    
    
    /****************** REMOVAL OF PHRASE DIV **/
    
    /**
     * Inits deleteSearchPhrase buttons
     */
    function initDeleteSearchPhraseButtons() {
    
        $('[id^=deletePhraseButton_]').click(function() {
            
            $(this).tooltip("hide");
            
            deleteSearchPhrase(extractIndex($(this)));
            
        });
    }
    
    /**
     * Deletes the given search phrase
     * @param phraseIndexToRemove index (in the list of phrases) of the phrase to delete
     */
    function deleteSearchPhrase(phraseIndexToRemove) {
        
        var oldAction = $('#analysisForm').attr('action');
        $('#analysisForm').attr('action', oldAction + "/removePhrase");
        $('#analysisForm').append($('<input>').attr('id', 'filterIndexToRemove').attr('type', 'hidden').attr('name', 'filterIndexToRemove').val(phraseIndexToRemove));
        
        submitAndPrintAnalysisForm(true);
        
    }
    
    
    
    /****************** ADDING NEW PHRASE DIV **/

    /**
     * Inits addNewSearchPhraseButton
     */
    function initAddNewSearchPhraseButton() {
        
        $('#addPhraseButton').click(function(){
        
              addNewSearchPhrase();
        });
        
    }
    
    /**
     * Adds new search phrase
     */
    function addNewSearchPhrase() {
        
        var oldAction = $('#analysisForm').attr('action');
        $('#analysisForm').attr('action', oldAction + "/addNewPhrase");
        
        submitAndPrintAnalysisForm(true);
    }
    

    
    
    /****************** Phrase inputs **/
    
    /**
     * Inits form inputs
     */
    function initInputs() {
        
        $("[id^=seriesSearchPhraseInput], #xRangeStartMonth, #xRangeStartYear, #xRangeEndMonth, #xRangeEndYear, #yaxisValueType").change(function() {
            generateCharts(true);
        });
        
    }

    

    
    /****************** COMMON **/
    
        
    
    /**
     * Submit (ajaxly) the analysisForm and reprints (and initializes) it with the version (html) received 
     * from the server endpoint.
     * @param regenerateChart if true then the charts will be regenerated
     *  
     */
    function submitAndPrintAnalysisForm(regenerateChart) {
        
        
        $('#analysisForm').attr('method', 'post');
        
        $('#analysisForm').ajaxSubmit(function(view) {
            
            $('#analysisFormDiv').html(view);  
        
            initFormElements();
            
            if (regenerateChart) {
                
                generateCharts(true);
                
            }
            
        });
        
        
    }
    
    
    /**
     * Updates url in brower address and history
     */
    function updateUrl() {
        var newUrl = $(location).attr('protocol') + "//" + $(location).attr('host') + $(location).attr('pathname') + "?" + $("#analysisForm").serialize();
        
        history.pushState('html:newUrl', '', newUrl);
        
    }
  
    //******************************* MAIN CHART ***************************************************
    
    /**
     * Submits the analysisForm to a proper server endpoint and prints relevant charts based on
     * data received from the server.
     * @param updateUrlLocation should the updateUrl function be invoked after generating the chart
     */
    function generateCharts(updateLocationUrl) {
        
        var analysisForm = $('#analysisForm');
        var oldAction = analysisForm.attr('action');
        analysisForm.attr('action', oldAction + "/generate");
            
        showAjaxLoader("mainChart");
            
        analysisForm.ajaxSubmit(function(chart) {
             analysisForm.attr('action', oldAction)
             mainChart = chart;
             printMainChart(chart, null, null);
             if (updateLocationUrl) {
                 updateUrl();   
             }
         });

    }

    /**
     * Shows ajax loader gif in the given div during chart generation process 
     */
    function showAjaxLoader(chartDivId) {
        ajaxLoader = $('#ajaxChartLoaderImg').clone();
        ajaxLoader.css('visibility', "visible");
        $('#'+chartDivId).html(ajaxLoader);
        
    }
    
    
    
    /**
     * Prints main chart (number of judgments over the judgment date)
     * @param chart json chart data, see pl.edu.icm.saos.webapp.chart.Chart 
     * @param xmin min x value that will be shown on the chart, null - the min x value from chart data
     * @param xmax max x value that will be shown on the chart, null - the max x value from chart data
     */
    function printMainChart(chart, xmin, xmax) {

        var yMinMax = calculateYAxisRangeForXRange(chart, xmin, xmax);
        var ymin = yMinMax[0];
        var ymax = yMinMax[1];
        
        var seriesArr = [];
        
        for (var i = 0; i < chart.seriesList.length; i++) {
            seriesArr.push({color: getColour(i), label: "", data: chart.seriesList[i].points});
        }
        
        $.plot($("#mainChart"), seriesArr,    {
                                                     lines: {
                                                         steps:false,
                                                         align: 'left'
                                                    }, 
                                                    xaxis: { 
                                                        min: xmin,
                                                        max: xmax,
                                                        ticks: chart.xticks
                                                        
                                                    }, 
                                                    yaxis: {
                                                        tickDecimals: 0,
                                                        max: ymax,
                                                        min: ymin
                                                    }, 
                                                    grid: {
                                                        hoverable: true
                                                    },
                                                    selection: {
                                                        mode: "x"
                                                    }
                                                      
                                                     
                                                }
                                            );
        
        formatXTicks(140);
        
    }
    
    /** Show a point tooltip on the main chart */
    $("#mainChart").on("plothover", function (event, pos, item) {
        showYNumberPointTooltip(event, pos, item, 2);
    });
    
    
    /** zoom the main chart */
    $("#mainChart").on("plotselected", function (event, ranges) {
        $('#mainChartZoomCancelHint').text(analysisJsProperties.ZOOM_CANCEL_HINT);
        printMainChart(mainChart, ranges.xaxis.from, ranges.xaxis.to);
        isZoomed = true;
    });
    
    /** cancel zoom if the user clicks outside the chart */
    $(document).click(function(event) { 
        if($(event.target).parents().index($('#mainChart')) == -1 && mainChart != null && isZoomed) {
            $('[id$="ZoomCancelHint"]').html("");
            printMainChart(mainChart, null, null);
            isZoomed = false;
        }        
    });
    

}
