
    

var initAnalysisJs = function() {    

    var mainChart = null;
    var aggregatedMainChart = null;
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
    
    
    var analysisFormBaseAction = $('#analysisForm').attr('action');
    
    
    
    initFormElements();
    
    generateCharts(false);
    
    // reloads page on history back (without this
    // after changing the history in {@link #updateUrl()} the back button would not work properly)
    window.onpopstate = function(e){
        location.reload();
    };

    
    
    //------------------------------- FUNCTIONS ----------------------------------
    
    
    
    /**
     * Initializes certain analysis form elements
     */
    function initFormElements() {
        
        
        
        initInfoFormSections('judgmentDateRangeInfoDiv', 'judgmentDateRangeSelectDiv', updateJudgmentDateRangeInfo);
         
        tieMonthYearRangeSelects("judgmentDateStartMonth", "judgmentDateStartYear", "judgmentDateEndMonth", "judgmentDateEndYear");

        initButtons();
        
        initInputs();

        colourPhraseInputs();
        
        //Enables bootstrap tooltip
        $('#analysisForm [data-toggle="tooltip"]').tooltip({container: 'body'});

        /* Anchor with empty href, should not reload page*/
        $("a[href='']").click(function(event) {
            event.preventDefault();
        });
        
        
    }
    
    
    /**
     * Inits buttons of the analysisForm  
     */
    function initButtons() {
        initAddNewSearchPhraseButton();
        initDeleteSearchPhraseButtons();
        initExportToCsvButtons();
        
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
    
    
    /****************** CONTEXT DIV **************/
    
    
    /**
     * Updates the html content of the judgmentDateRangeInfoDiv with information from
     * judgmentDateRangeSelectDiv inputs.
     */
    function updateJudgmentDateRangeInfo() {
        var dateRangeHtml = '<b>' + $('#judgmentDateStartMonth option:selected').text() + ' ' +
                       $('#judgmentDateStartYear').val() + ' - ' +
                       $('#judgmentDateEndMonth option:selected').text() + ' ' +
                       $('#judgmentDateEndYear').val() + '</b>';
        
        $('#judgmentDateRangeInfoDiv').html(dateRangeHtml);
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
        
        $('#analysisForm').attr('action', analysisFormBaseAction + "/removePhrase");
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
        
        $('#analysisForm').attr('action', analysisFormBaseAction + "/addNewPhrase");
        
        submitAndPrintAnalysisForm(true);
    }
    

    
    
    /****************** Phrase inputs **/
    
    /**
     * Inits form inputs
     */
    function initInputs() {
        
        $("[id^=seriesSearchPhraseInput], #judgmentDateStartMonth, #judgmentDateStartYear, #judgmentDateEndMonth, #judgmentDateEndYear, #yaxisValueType").change(function() {
            generateCharts(true);
        });
        
        $("[id^=seriesSearchPhraseInput]").keypress(function (e) {
            if (e.which == 13) {
                generateCharts(true);
                e.preventDefault();
            }
        });
        
    }


    
    /****************** CSV GENERATION **/
    
    function initExportToCsvButtons() {
        $("[id^=exportToCsv]").click(function () {
            var chartCode = $(this).attr('id').split("-")[1];
            $('#analysisForm').attr('action', analysisFormBaseAction + "/generateCsv");
            $('[name="chartCode"]').remove();
            $('#analysisForm').append($("<input>").attr('type','hidden').attr("name", "chartCode").val(chartCode));
            $('#analysisForm').submit();
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
        $('[name="chartCode"]').remove();
        
        var newUrl = $(location).attr('protocol') + "//" + $(location).attr('host') + $(location).attr('pathname') + "?" + $("#analysisForm").serialize();
        
        history.pushState('html:newUrl', '', newUrl);
        
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
     * Submits the analysisForm to a proper server endpoint and prints relevant charts based on
     * data received from the server.
     * @param updateUrlLocation should the updateUrl function be invoked after generating the chart
     */
    function generateCharts(updateLocationUrl) {
        
        $('#analysisForm').attr('action', analysisFormBaseAction + "/generate");
            
        showAjaxLoader("mainChart");
        showAjaxLoader("mainAggregatedChart");
            
        $('#analysisForm').ajaxSubmit(function(charts) {
            $('#analysisForm').attr('action', analysisFormBaseAction)
             
             mainChart = charts['MAIN_CHART'];
             printMainChart(mainChart, null, null);
             
             aggregatedMainChart = charts['AGGREGATED_MAIN_CHART'];
             printAggregatedMainChart(aggregatedMainChart, null, null);
             
             if (updateLocationUrl) {
                 updateUrl();   
             }
         });

    }
    
    
    /** cancel zoom if the user clicks outside the chart */
    $(document).click(function(event) { 
        if($(event.target).parents().index($('#mainChart')) == -1 && isZoomed) {
            $('[id$="mainChartZoomCancelHint"]').html("");
            printMainChart(mainChart, null, null);
            isZoomed = false;
        }        
    });
    
    
    //******************************* MAIN CHART ***************************************************
        
    
    /**
     * Prints the main chart (number of judgments over the judgment date)
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
                                                        ticks: chart.xticks,
                                                        tickFormatter: function(val, axis) { // TODO: 
                                                            return chart.xticks[val];
                                                        }
                                                        
                                                    }, 
                                                    yaxis: {
                                                        tickDecimals: 0,
                                                        max: ymax,
                                                        min: ymin
                                                    }, 
                                                    grid: {
                                                        hoverable: true,
                                                        borderWidth: 1,
                                                        borderColor: '#888'
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
    
   

    
    //******************************* AGGREGATED MAIN CHART ***************************************************
        
    
    /**
     * Prints the aggregated main chart (number of judgments in the whole judgment date period)
     * @param chart json chart data, see pl.edu.icm.saos.webapp.chart.Chart 
     */
    function printAggregatedMainChart(chart) {

        var xmin = 0;
        var xmax = chart.seriesList[chart.seriesList.length-1].points[0][0] + 1;
        
        var yMinMax = calculateYAxisRangeForXRange(chart, xmin, xmax);
        var ymin = 0;
        var ymax = yMinMax[1]+yMinMax[1]/10;
        
        var seriesArr = [];
        
        for (var i = 0; i < chart.seriesList.length; i++) {
            seriesArr.push({color: getColour(i), label: "", data: chart.seriesList[i].points});
        }
        
        $.plot($("#aggregatedMainChart"), seriesArr,    {
                                                    bars: {
                                                        show: true,
                                                        align: 'right',
                                                        barWidth: 0.4
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
                                                        hoverable: true,
                                                        borderWidth: 1,
                                                        borderColor: '#888',
                                                    },
                                                    selection: {
                                                        mode: "x"
                                                    }
                                                      
                                                     
                                                }
                                            );
        
        
    }
    
   
    /** Show a point tooltip on the main chart */
    $("#aggregatedMainChart").on("plothover", function (event, pos, item) {
        if (item) {
            
            $("#tooltip").remove();
            
            var y = formatNumber(item.datapoint[1], 2);
            
            showTooltip(item.pageX, item.pageY, "<b>"+y+"</b>");
        
        } else {
            $("#tooltip").remove();
        
        }
    });

}
