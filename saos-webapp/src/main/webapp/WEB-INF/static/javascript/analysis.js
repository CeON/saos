
    

var initAnalysisJs = function() {    

    var mainChart = null;
    
    var colours = ["#AA0000", "#00AA00", "#0000AA", "#AA00AA", "#AAAA00", "#00AAAA", "#AAAAAA", "#AA5500"]
    
    initButtons();
    
    generateCharts();
    
    
    /**
     * Inits buttons of the analysisForm  
     */
    function initButtons() {
        initAddNewSearchPhraseButton();
        initDeleteSearchPhraseButtons();
    }
    
    
    /****************** REMOVAL OF PHRASE DIV **/
    
    /**
     * Inits deleteSearchPhrase buttons
     */
    function initDeleteSearchPhraseButtons() {
    
        $('[id^=deletePhraseButton_]').click(function() {
            
            // TODO: delete tooltip
            
            deleteSearchPhrase($(this).attr('id').split('_')[1]);
            
        });
    }
    
    /**
     * Deletes the given search phrase
     * @param phraseIndexToRemove index (in the list of phrases) of the phrase to delete
     */
    function deleteSearchPhrase(phraseIndexToRemove) {
        
        var oldAction = $('#analysisForm').attr('action');
        $('#analysisForm').attr('action', oldAction + "/removePhrase");
        $('#analysisForm').append($('<input>').attr('id', 'searchCriteriaIndexToRemove').attr('type', 'hidden').attr('name', 'searchCriteriaIndexToRemove').val(phraseIndexToRemove));
        
        submitAndPrintAnalysisForm();
        
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
        
        submitAndPrintAnalysisForm();
    }
    
    
    /****************** COMMON **/
    
    /**
     * Submit (ajaxly) the analysisForm and reprints (and initiaties) it with the version (html) received 
     * from the server endpoint.
     *  
     */
    function submitAndPrintAnalysisForm() {
        
        $('#analysisForm').attr('method', 'post');
        
        $('#analysisForm').ajaxSubmit(function(view) {
            
            $('#analysisFormDiv').html(view);  
        
            initButtons();
            generateCharts();
            
        });
    }
    
    
    
    
    <!--$('input[id="daterange"]').daterangepicker();-->
    
    
    
    //******************************* MAIN CHART ***************************************************
    
    /**
     * If the analysis form is not empty (uses #isAnalysisFormEmpty()),
     * submits the analysisForm to a proper server endpoint and prints relevant charts based on
     * data received from the server.
     */
    function generateCharts() {
        
        if (!isAnalysisFormEmpty()) {
            
            var analysisForm = $('#analysisForm');
            var oldAction = analysisForm.attr('action');
            analysisForm.attr('action', oldAction + "/generate");
            
            analysisForm.ajaxSubmit(function(chart) {
                 analysisForm.attr('action', oldAction)
                 mainChart = chart;
                 printMainChart(chart, null, null);
             });
         }

    }
    
    /**
     * Returns true if the analysis form fields inputs are empty
     */
    function isAnalysisFormEmpty() {
        var empty = true;
        $("#analysisForm :text").each(function() {
            if($(this).val() !== "") {
                empty = false;
                return;
            }
        });
        
        return empty;
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
            seriesArr.push({color: colours[i], label: "", data: chart.seriesList[i].data});
        }
        
        $.plot($("#mainChart"), seriesArr,    {
                                                     lines: {
                                                         steps:false,
                                                         align: 'left'
                                                    }, 
                                                    xaxis: { 
                                                        mode: "time", 
                                                        timeformat: "%m/%y",
                                                        minTickSize: [1, "month"],
                                                        min: xmin,
                                                        max: xmax
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
    }
    
    /** Show a point tooltip on the main chart */
    $("#mainChart").bind("plothover", function (event, pos, item) {
        showDateNumberPointTooltip(event, pos, item);
    });
    
    
    /** zoom the main chart */
    $("#mainChart").bind("plotselected", function (event, ranges) {
        $('#mainChartZoomCancelHint').text(zoomCancelHint);
        printMainChart(mainChart, ranges.xaxis.from, ranges.xaxis.to);
    });
    
    /** cancel zoom if the user clicks outside the chart */
    $(document).click(function(event) { 
        if($(event.target).parents().index($('#mainChart')) == -1) {
            $('[id$="ZoomCancelHint"]').html("");
            printMainChart(mainChart, null, null);
        }        
    });
    

}
