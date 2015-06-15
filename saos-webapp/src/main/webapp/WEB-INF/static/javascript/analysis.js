
    

var initAnalysisJs = function() {    

    
    
    /**********************************************************************************************    
    /******************************************* ATTRIBUTES ***************************************
    /**********************************************************************************************/   

    
    
    var mainChart = null;
    var aggregatedMainChart = null;
    var ccCourtChart = null;
    
    
    var generatedChartAnalysisForm = null; // analysis form used to generate charts (see chart variables above)
    
    var pointTooltipId = "pointTooltip";
    
    var pointTooltipController = new FlotPointTooltipController(pointTooltipId);
    
    
    var isZoomed = false;
    var isChartGenerating = false; 
    
    // colours of subsequent series, the order of the colours is very important - 
    // each next colour has to differ as much as possible from preceding colours in order
    // for the series to look distinct 
    var colours = [
                   "rgb(  0,    0,  255)", // blue
                   "rgb(255,    0,    0)", // red
                   "rgb(  0,  128,    0)", // green
                   "rgb(  0,    0,    0)", // black
                   "rgb(255,  165,    0)", // orange
                   "rgb(128,    0,  128)", // purple
                   "rgb(  0,  255,  255)", // cyan
                   "rgb(165,   42,   42)", // brown
                   "rgb(128,  128,    0)", // olive
                   "rgb(255,  255,    0)", // yellow
                   "rgb(255,  192,  203)", // pink  
                   "rgb(  0,  139,  139)", // darkcyan
                   "rgb(  0,    0,  128)", // navy
                   "rgb(144,  238,  144)", // lightgreen
                   "rgb(189,  183,  107)", // darkkhaki
                   "rgb(169,  169,  169)", // darkgrey
                   "rgb( 85,  107,   47)", // darkolivegreen
                   "rgb(255,  140,    0)", // darkorange
                   "rgb(153,   50,  204)", // darkorchid
                   "rgb(255,    0,  255)", // magenta
                   "rgb(139,    0,    0)", // darkred
                   "rgb(233,  150,  122)", // darksalmon
                   "rgb(148,    0,  211)", // darkviolet
                   "rgb(139,    0,  139)", // darkmagenta
                   "rgb(  0,  100,    0)", // darkgreen
                   "rgb(255,  215,    0)", // gold
                   "rgb( 75,    0,  130)", // indigo
                   "rgb(240,  230,  140)", // khaki
                   "rgb(173,  216,  230)", // lightblue
                   "rgb(224,  255,  255)", // lightcyan
                   "rgb(211,  211,  211)", // lightgrey
                   "rgb(255,  182,  193)", // lighpink
                   "rgb(255,  255,  224)", // lightyellow
                   "rgb(  0,  255,    0)", // lime
                   "rgb(128,    0,    0)", // maroon
                   "rgb(  0,    0,  139)", // darkblue
                   "rgb(245,  245,  220)", // beige
                   "rgb(192,  192,  192)"  // silver
                
               	   ];
    
    
    // replacements of phrases in cc court names 
    // parts of the court names on the x-axis of the ccCourt chart will be changed with the defined replacements
    // see: formatXCourtName function somewhere at the bottom
    var ccCourtNameReplacements = {};
    var plCcCourtNameReplacements = {"Sąd Apelacyjny":"SA", "Sąd Rejonowy":"SR", "Sąd Okręgowy":"SO", " w ":"<br/>w ", " we ":"<br/>we "}
    ccCourtNameReplacements['pl'] = plCcCourtNameReplacements;
    
    var nullPhraseReplacements = {'pl':'wszystkie'};
    
    var analysisFormBaseAction = $('#analysisForm').attr('action');
    
    
    
    /**********************************************************************************************    
    /************************************ INITALIZATION *******************************************
    /**********************************************************************************************/    
    
    
    
    initFormElements();
    
    generateCharts(false);
    
    // reloads page on history back (without this
    // after changing the history in {@link #updateUrl()} the back button would not work properly)
    window.onpopstate = function(e){
        location.reload();
    };
    
    
    
    /**********************************************************************************************    
    /******************************** FORM INITIALIZATION & ACTIONS *******************************
    /**********************************************************************************************/   


    
    
    /**
     * Initializes certain analysis form elements
     */
    function initFormElements() {
        
        
        
        CourtCriteriaForm.init();
        
        infoFormSection({
            formSectionId: "#court-form-section",
            infoSectionId: "#court-info-section",
            extractInfoFromFormCustom: extractInfoFromCourtSection,
            onFormSectionCloseAction: function() {generateCharts(true);}
        });

        infoFormSection({
            formSectionId: "#date-range-form-section",
            infoSectionId: "#date-range-info-section",
            defaultInfoSectionText: null,
            extractInfoFromFormCustom: extractJudgmentDateRangeFromAnalysisFormSection,
            onFormSectionCloseAction: function() {generateCharts(true);}
            
        });
        
        
        tieMonthYearRangeSelects("judgmentDateStartMonth", "judgmentDateStartYear", "judgmentDateEndMonth", "judgmentDateEndYear");

        initInputs();

        colourPhraseInputs();
        
        //Enables bootstrap tooltip
        $('#analysisForm [data-toggle="tooltip"]').tooltip({container: 'body'});
        
        /* Enable bootstrap popover's */
        $('[data-toggle="popover"]').popover({container: 'body'});

        /* Anchor with empty href, should not reload page*/
        $("a[href='']").click(function(event) {
            event.preventDefault();
        });
        
        initButtons();
        
        
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
    

    
    /**
     * Inits form inputs
     */
    function initInputs() {
        
        $("[id^=seriesSearchPhraseInput]").change(function() {
            generateCharts(true);
        });
        
        $("#yaxisValueType").change(function() {
            $('#yaxisValueTypeHidden').val($(this).val());
            generateCharts(true);
        });
        
        
        $("[id^=seriesSearchPhraseInput]").keypress(function (e) {
            if (e.which == 13) {
                generateCharts(true);
                e.preventDefault();
            }
        });
        
        $('#select-common-court').change(function() {
            if ($(this).val() != '') {
                $('#ccIncludeDependentCourtJudgments').val(true);
                $('#ccIncludeDependentCourtJudgments').prop('checked', true);
                $("[name='_globalFilter.courtCriteria.ccIncludeDependentCourtJudgments']").val("true");
            }
        });
        
        
        
    }


    
    /**********************************************************************************************    
    /*********************************** CSV GENERATION *****************************************
    /**********************************************************************************************/   

    
    /**
     * Initializes export to csv buttons. Clicking on these buttons will execute csv chart data
     * generation.
     */
    function initExportToCsvButtons() {
        $("[id^=exportToCsv]").click(function () {
            var chartCode = $(this).attr('id').split("-")[1];
            if (isChartGenerating) { // hold the csv generation if the chart is generating
                setTimeout(function() {generateCsv(chartCode)}, 500);
            } else {
                generateCsv(chartCode);
            }
        });
    }

    /**
     * Generates csv data for the chart with the given chartCode
     */
    function generateCsv(chartCode) {
        if (isChartGenerating) {return;}
        $('#analysisForm').attr('action', analysisFormBaseAction + "/generateCsv");
        $('[name="chartCode"]').remove();
        $('#analysisForm').append($("<input>").attr('type','hidden').attr("name", "chartCode").val(chartCode));
        $('#analysisForm').submit();
    }
    
    
    
    
    
    /**********************************************************************************************    
    /*********************************** ZOOM CANCEL **********************************************
    /**********************************************************************************************/   

        
    
 
    /** cancels zoom if the user clicks outside the chart */
    $(document).click(function(event) { 
        if($(event.target).parents().index($('#mainChart')) == -1 && $(event.target).parents().index($('#ccCourtChart')) == -1 && isZoomed) {
            $('[id$="mainChartZoomCancelHint"]').html("");
            printMainChart(mainChart, null, null);
            
            $('[id$="ccCourtChartZoomCancelHint"]').html("");
            printCcCourtChart(ccCourtChart, null, null);
            isZoomed = false;
        }        
    });
    
    
    
    /**********************************************************************************************    
    /*********************************** CHART GENERATION *****************************************
    /**********************************************************************************************/   
    
    
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

        isChartGenerating = true;
        
        $('#analysisForm').attr('action', analysisFormBaseAction + "/generate");
            
        showAjaxLoader("mainChart");
        showAjaxLoader("aggregatedMainChart");
        showAjaxLoader("ccCourtChart");
        clearChartTables();
        
        $('#analysisForm').ajaxSubmit(function(analysisResult) {
             $('#analysisForm').attr('action', analysisFormBaseAction);
             
             
             generatedChartAnalysisForm = analysisResult.analysisForm;
             
             var charts = analysisResult.charts;
             
             mainChart = charts['MAIN_CHART'];
             aggregatedMainChart = charts['AGGREGATED_MAIN_CHART'];
             ccCourtChart = charts['CC_COURT_CHART'];
             
             printMainChart(mainChart, null, null);
             generateMainChartTable(mainChart);
             printAggregatedMainChart(aggregatedMainChart);
             
             if (ccCourtChart) {
                 printCcCourtChart(ccCourtChart);
                 generateCcCourtChartTable(ccCourtChart);
             } else {
                 $('#ccCourtChartDiv').hide();

             }
             
             if (updateLocationUrl) {
                 updateUrl();   
             }
             
             isChartGenerating = false;
         });

    }
    
    
    
    
    
    
    /**********************************************************************************************    
    /******************************* MAIN CHART ***************************************************
    /**********************************************************************************************/   
    
    
    
    
    /*------------------------------------ PRINT CHART ----------------------------------------- */
    
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
            seriesArr.push({color: getColour(i), label: "", data: chart.seriesList[i].points, points: {fillColor: getColour(i)}});
        }
        
        mainChartPlot = $.plot($("#mainChart"), seriesArr,    {
                                                    lines: {
                                                         steps:false,
                                                         align: 'left',
                                                         show: true
                                                         
                                                    }, 
                                                    points: {
                                                        show:true, 
                                                        radius: 2
                                                        
                                                    },
                                                    xaxis: { 
                                                        min: xmin,
                                                        max: xmax,
                                                        tickSize: 1,
                                                        tickFormatter: function(val, axis) {
                                                            var xtick = chart.xticks[val][1];
                                                            return formatXtickPeriod(xtick);
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
        
        formatXTicks(140, "mainChart");
        $('#mainChart').find('.flot-x-axis .flot-tick-label').css("max-width","");
        
    }
    
    
    
    
    /*----------------------------------- TOOLTIP ---------------------------------------*/
    
    
    $("#mainChart").on("plothover", function (event, pos, item) {
        highlightCurrentSeries(mainChartPlot, null, event, pos, item);
        pointTooltipController.controlPointTooltip(pos, item, generateMainChartPointTooltip);
    });
    
    
    
    
    /** Generates tooltip div for the given point item of the MainChart */
    function generateMainChartPointTooltip(item) {
        
        var pointTooltipDivId = "mainChartPointTooltip";
        var pointTooltipDiv = $('#'+pointTooltipDivId).clone();
        
        pointTooltipDiv.attr('id', pointTooltipId);
        
        var timePeriod = mainChart.xticks[item.dataIndex][1];
        var formattedTimePeriod = formatXtickPeriod(timePeriod, " - ", " - ", " - ");
        pointTooltipDiv.find("#pointTimePeriod").html(formattedTimePeriod);
        
        
        var searchedPhrase = getSearchedPhrase(item.seriesIndex);
        pointTooltipDiv.find("#pointSearchedPhrase").html(searchedPhrase);
        
        
        var judgmentCount = item.datapoint[1];
        var formattedJudgmentCount = formatNumber(judgmentCount, 2) + getChartYNumberUnit();
        pointTooltipDiv.find("#pointJudgmentCount").html(formattedJudgmentCount);
        
        
        if (judgmentCount > 0) {
            
            var searchUrl = generateMainChartPointSearchUrl(item);
            
            pointTooltipDiv.find("#pointSearchLink").attr("href", searchUrl);
            
        } else {
            
            pointTooltipDiv.find("#pointSearchLinkDiv").remove();
        }
        
        
        return pointTooltipDiv;
    }
    
    
    /** Generates an url to the search page with a list of judgments for the given point of the MainChart */
    function generateMainChartPointSearchUrl(item) {
        
        var searchUrl = generateBasePointSearchUrl(item);
        
        searchUrl += generatePointDatePeriodSearchUrlParams(item);
        
        searchUrl += "&" + generateCourtCriteriaSearchUrlParams();
     
        return searchUrl;
    }
   
    
    function generatePointDatePeriodSearchUrlParams(item) {
        
        var timePeriod = mainChart.xticks[item.dataIndex][1];
        
        var dateFrom = extractStartDate(timePeriod);
        var dateTo = extractEndDate(timePeriod);
        
        return generateDateRangeSearchUrlParams(dateFrom, dateTo);
        
    }
    
    
    function extractStartDate(timePeriod) {
        
        if (timePeriod.period === "YEAR") {
            return new Date(timePeriod.startYear, 1-1, 1);
        }
        
        if (timePeriod.period === "MONTH") {
            return new Date(timePeriod.startYear, timePeriod.startMonthOfYear-1, 1);
        }
        
        if (timePeriod.period === "DAY") {
            return new Date(timePeriod.startDay.year, timePeriod.startDay.monthOfYear-1, timePeriod.startDay.dayOfMonth);
        }
        
    }
    
    function extractEndDate(timePeriod) {
        
        if (timePeriod.period === "YEAR") {
            return new Date(timePeriod.endYear, 12-1, 31);
        }
        
        if (timePeriod.period === "MONTH") {
            return moment([timePeriod.endYear, timePeriod.endMonthOfYear-1, 1]).endOf("month").toDate();
        }
        
        if (timePeriod.period === "DAY") {
            return new Date(timePeriod.endDay.year, timePeriod.endDay.monthOfYear-1, timePeriod.endDay.dayOfMonth);
        }


    }
    


    /*------------------------------------ ZOOM --------------------------------------------*/

    
    $("#mainChart").on("plotselected", function (event, ranges) {
        $('#mainChartZoomCancelHint').text(analysisJsProperties.ZOOM_CANCEL_HINT);
        printMainChart(mainChart, ranges.xaxis.from, ranges.xaxis.to);
        isZoomed = true;
    });
    
    
    
    /*------------------------------------ GENERATE TABLE /WCAG/ --------------------------------------------*/

    
    function generateMainChartTable(chart) {
        
        generateChartTable('mainChartTable', chart, function(xtick) {
            return formatXtickPeriod(xtick, " - ", " - ", " - ", function(date) { return formatDateWithPattern(date, "MMMM YYYY")}, function(date) { return formatDateWithPattern(date, "DD MMMM YYYY")})
        });
        
    }
    
    
    
    
    

    /**********************************************************************************************    
    /******************************* AGGREGATED MAIN CHART ****************************************
    /**********************************************************************************************/    
    
    
    
    /*------------------------------------ PRINT CHART ----------------------------------------- */
    
    
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
                                                        barWidth: 0.5
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
    
   
    /*----------------------------------- TOOLTIP ----------------------------------*/
    
    $("#aggregatedMainChart").on("plothover", function (event, pos, item) {
        pointTooltipController.controlPointTooltip(pos, item, generateAggregatedMainChartPointTooltip);
    });
    
    
    
    /** Generates tooltip div for the given point item of the AggregatedMainChart */
    function generateAggregatedMainChartPointTooltip(item) {
        
        var pointTooltipDivId = "aggregatedMainChartPointTooltip";
        var pointTooltipDiv = $('#'+pointTooltipDivId).clone();
        
        pointTooltipDiv.attr('id', pointTooltipId);
        
        var formattedTimePeriod = formatMonth(getGlobalStartDate()) + " - " + formatMonth(getGlobalEndDate()); 
        pointTooltipDiv.find("#pointTimePeriod").html(formattedTimePeriod);
        
        var searchedPhrase = getSearchedPhrase(item.seriesIndex);
        pointTooltipDiv.find("#pointSearchedPhrase").html(searchedPhrase);
        
        var judgmentCount = item.datapoint[1];
        var formattedJudgmentCount = formatNumber(judgmentCount, 2) + getChartYNumberUnit();
        pointTooltipDiv.find("#pointJudgmentCount").html(formattedJudgmentCount);
        
        
        if (judgmentCount > 0) {
            
            var searchUrl = generateAggregatedChartPointSearchUrl(item);
            
            pointTooltipDiv.find("#pointSearchLink").attr("href", searchUrl);
            
        } else {
            
            pointTooltipDiv.find("#pointSearchLinkDiv").remove();
        }
        
        
        return pointTooltipDiv;
    }
    
    
    
    /** Generates an url to the search page with a list of judgments for the given point of the AggregatedMainChart */
    function generateAggregatedChartPointSearchUrl(item) {
        
        var searchUrl = generateBasePointSearchUrl(item);
        
        searchUrl += generateDateRangeSearchUrlParams(getGlobalStartDate(), getGlobalEndDate());
        
        searchUrl += "&" + generateCourtCriteriaSearchUrlParams();
     
        return searchUrl;
    }
   
    
    
    
    /**********************************************************************************************    
    /************************************ CC COURT CHART ******************************************
    /**********************************************************************************************/    
        
        
    
    /*------------------------------------ PRINT CHART ----------------------------------------- */
    
    
    /**
     * Prints the court chart (number of judgments for each common court)
     * @param chart json chart data, see pl.edu.icm.saos.webapp.chart.Chart 
     * @param xmin min x value that will be shown on the chart, null - the min x value from chart data
     * @param xmax max x value that will be shown on the chart, null - the max x value from chart data
     */
    function printCcCourtChart(chart, xmin, xmax) {

        $('#ccCourtChartDiv').show();
        
        if (xmin == null) {
            xmin = -1;
        }
        
        if (xmax == null) {
            xmax = chart.seriesList[0].points.length;
        }
        
        var yMinMax = calculateYAxisRangeForXRange(chart, xmin, xmax);
        var ymin = 0;
        var ymax = yMinMax[1]+yMinMax[1]/10;
        
        var seriesArr = [];
        
        var numberOfSeries = chart.seriesList.length;
        for (var i = 0; i < numberOfSeries; i++) {
            seriesArr.push({color: getColour(i), label: "", data: chart.seriesList[i].points, bars: {order:i}});
        }
        
        var numberOfBarsInSeries = chart.seriesList[0].points.length; 
        
        var calcBarWidth = numberOfBarsInSeries / (numberOfBarsInSeries*numberOfSeries*2);
        
        ccCourtChartPlot = $.plot($("#ccCourtChart"), seriesArr,    {
                                                    bars: {
                                                        show: true,
                                                        align: 'left',
                                                        barWidth: calcBarWidth
                                                       
                                                    }, 
                
                                                    xaxis: { 
                                                        min: xmin,
                                                        max: xmax,
                                                        tickSize: 1,
                                                        tickFormatter: function(val, axis) {  
                                                            if (val > xmin && val < xmax) {
                                                                return formatXCourtName(chart.xticks[val][1].name);
                                                            } else {
                                                                return "";
                                                            }
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
                                                        borderColor: '#888',
                                                    },
                                                    selection: {
                                                        mode: "x"
                                                    }
                                                      
                                                     
                                                }
                                            );
        
        
    }
    
    /**
     * Formats the given court name, replaces each phrase of the name with a replacement from ccCourtNameReplacements
     * (more precisely: from a set of replacements for the current language of the analysis page)
     */
    function formatXCourtName(courtName) {
        var replacements = ccCourtNameReplacements[analysisJsProperties.PAGE_LANG];
        for (key in replacements) {
           courtName = courtName.replace(key, replacements[key]);
        }
        return courtName;
    }
    
    

    /*----------------------------------- TOOLTIP ----------------------------------*/
    
      
    $("#ccCourtChart").on("plothover", function (event, pos, item) {
        highlightCurrentSeries(ccCourtChartPlot, pointTooltipId, event, pos, item);
        pointTooltipController.controlPointTooltip(pos, item, generateCcCourtChartPointTooltip);
    });
    

    /** Generates tooltip div for the given point item of the CcCourtChart */
    function generateCcCourtChartPointTooltip(item) {
        
        var pointTooltipDivId = "ccCourtChartPointTooltip";
        var pointTooltipDiv = $('#'+pointTooltipDivId).clone();
        
        pointTooltipDiv.attr('id', pointTooltipId);
        
        
        pointTooltipDiv.find("#pointCourtName").html(ccCourtChart.xticks[item.dataIndex][1].name);
        
        
        var formattedTimePeriod = formatMonth(getGlobalStartDate()) + " - " + formatMonth(getGlobalEndDate()); 
        pointTooltipDiv.find("#pointTimePeriod").html(formattedTimePeriod);
        

        var searchedPhrase = getSearchedPhrase(item.seriesIndex);
        pointTooltipDiv.find("#pointSearchedPhrase").html(searchedPhrase);
        
        var judgmentCount = item.datapoint[1];
        var formattedJudgmentCount = formatNumber(judgmentCount, 2) + getChartYNumberUnit();
        pointTooltipDiv.find("#pointJudgmentCount").html(formattedJudgmentCount);
        
        
        if (judgmentCount > 0) {
            
            var searchUrl = generateCcCourtChartPointSearchUrl(item);
            
            pointTooltipDiv.find("#pointSearchLink").attr("href", searchUrl);
            
        } else {
            
            pointTooltipDiv.find("#pointSearchLinkDiv").remove();
        }
        
        
        return pointTooltipDiv;
    }
    
    
    /** Generates an url to the search page with a list of judgments for the given point of the CcCourtChart */
    function generateCcCourtChartPointSearchUrl(item) {
        
        var searchUrl = generateBasePointSearchUrl(item);
        
        searchUrl += generateDateRangeSearchUrlParams(getGlobalStartDate(), getGlobalEndDate());
        
        var courtId = ccCourtChart.xticks[item.dataIndex][1].courtId;
        
        var courtUrlParams = "courtCriteria.courtType=COMMON&courtCriteria.ccCourtId="+courtId+"&courtCriteria.ccIncludeDependentCourtJudgments=true";
        searchUrl += "&" + courtUrlParams;
     
        return searchUrl;
    }

    
    
    
    /*---------------------------------------- ZOOM --------------------------------------------*/
    
    $("#ccCourtChart").on("plotselected", function (event, ranges) {
        $('#ccCourtChartZoomCancelHint').text(analysisJsProperties.ZOOM_CANCEL_HINT);
        printCcCourtChart(ccCourtChart, ranges.xaxis.from, ranges.xaxis.to);
        isZoomed = true;
    });

    
    
    /*------------------------------------ GENERATE TABLE /WCAG/ --------------------------------------------*/

    
    function generateCcCourtChartTable(chart) {
        
        generateChartTable('ccCourtChartTable', chart, function(xtick) {return xtick.name;});
        
    }
   
    
    
    /**********************************************************************************************    
    /******************************** XTICK FORMATTERS ********************************************
    /**********************************************************************************************/    

    
    /**
     * Formats the given xtick that contains a period of type pl.edu.icm.saos.common.chart.value.TimePeriod
     * 
     * xtick xtick object that contains a period of type pl.edu.icm.saos.common.chart.value.TimePeriod
     * yearRangeConnector the separator of period start and end year for year periods, defaults to: -
     * monthRangeConnector the separator of period start and end month-year for month-year periods, defaults to: &lt;br/&gr; - &lt;br/&gr;
     * dayRangeConnector the separator of period start and end dates for day periods, defaults to: &lt;br/&gr; - &lt;br/&gr;
     * monthDateFormatter the function formatting start and end dates for month-year periods, defaults to formatMonth
     * dayDateFormatter the function formatting start and end dates for day periods, defaults to formatDate
     * 
     */
    function formatXtickPeriod(xtick, yearRangeConnector, monthRangeConnector, dayRangeConnector, monthDateFormatter, dayDateFormatter) {
        
        if (!monthDateFormatter) {
            monthDateFormatter = formatMonth;
        }
        
        if (!dayDateFormatter) {
            dayDateFormatter = formatDate;
        }
        
        if (!yearRangeConnector) {
            yearRangeConnector = " - ";
        }
        
        if (!monthRangeConnector) {
            monthRangeConnector = "<br/> - <br/>";
        }
        

        if (!dayRangeConnector) {
            dayRangeConnector = "<br/> - <br/>";
        }
        
        if (xtick.period === 'YEAR') {
            
            if (xtick.startYear === xtick.endYear) {
                return ""+xtick.startYear;
            } else {
                return xtick.startYear + yearRangeConnector + xtick.endYear;
            }
            
        } else if (xtick.period === 'MONTH') {
            
            if (xtick.startYear === xtick.endYear && xtick.startMonthOfYear === xtick.endMonthOfYear) {
                return monthDateFormatter(new Date(xtick.startYear, xtick.startMonthOfYear-1, 1));
            } else {
                return monthDateFormatter(new Date(xtick.startYear, xtick.startMonthOfYear-1, 1)) + monthRangeConnector + monthDateFormatter(new Date(xtick.endYear, xtick.endMonthOfYear-1, 1));
            }
            
        } else if (xtick.period === 'DAY') {
            
            if (xtick.startDay.dayOfMonth === xtick.endDay.dayOfMonth 
                    && xtick.startDay.monthOfYear === xtick.endDay.monthOfYear
                    && xtick.startDay.year === xtick.endDay.year) {
                return dayDateFormatter(new Date(xtick.startDay.year, xtick.startDay.monthOfYear-1, xtick.startDay.dayOfMonth));
            } else {
                return dayDateFormatter(new Date(xtick.startDay.year, xtick.startDay.monthOfYear-1, xtick.startDay.dayOfMonth)) + dayRangeConnector +
                       dayDateFormatter(new Date(xtick.endDay.year, xtick.endDay.monthOfYear-1, xtick.endDay.dayOfMonth));
            }
        }
        
    }
    
   
    function formatMonth(date) {
        return formatDateWithPattern(date, "MMM YYYY");
    }
 
    function formatDate(date) {
        return formatDateWithPattern(date, "DD MMM YYYY");
    }
    
    function formatDateWithPattern(date, pattern) {
        return moment(date).locale(analysisJsProperties.PAGE_LANG).format(pattern).replace(/ /g,"&nbsp;");
    }
    
    
    
    /**********************************************************************************************    
    /******************************** POINT_TOOLTIP RELATED FUNCTIONS *****************************
    /**********************************************************************************************/    

    function isChartYPercent() {
        return generatedChartAnalysisForm.ysettings.valueType === 'PERCENT';
    }
    
    function isChartYPer1000() {
        return generatedChartAnalysisForm.ysettings.valueType === 'NUMBER_PER_1000';
    }
    
    function getChartYNumberUnit() {
        
        var numberUnit = '';
        
        if (isChartYPercent()) {
            numberUnit = '%';
        }
        
        if (isChartYPer1000()) {
            numberUnit = '‰';
        }
        
        return numberUnit;
    }
    
    
    function generateCourtCriteriaSearchUrlParams() {
        return generateUrlParams(generatedChartAnalysisForm.globalFilter.courtCriteria, "courtCriteria");
    }

    
    function getGlobalStartDate() {
        var dateRange = generatedChartAnalysisForm.globalFilter.judgmentDateRange;
        return moment([dateRange.startYear, dateRange.startMonth-1, 1]).toDate();
    }

    
    function getGlobalEndDate() {
        var dateRange = generatedChartAnalysisForm.globalFilter.judgmentDateRange;
        return moment([dateRange.endYear, dateRange.endMonth-1, 1]).endOf("month").toDate();
    }
    
    
    function getSearchedPhrase(seriesIndex) {
        return generatedChartAnalysisForm.seriesFilters[seriesIndex].phrase;
    }
    
    function generateDateRangeSearchUrlParams(dateFrom, dateTo) {
        var dateFrom = moment(dateFrom).format(DATE_PATTERN);
        var dateTo = moment(dateTo).format(DATE_PATTERN);
        
        return "dateFrom="+encodeURIComponent(dateFrom)+"&dateTo="+encodeURIComponent(dateTo);
    }
    
    function generateBasePointSearchUrl(item) {
        
        var searchUrl = "search";
        
        var phrase = generatedChartAnalysisForm.seriesFilters[item.seriesIndex].phrase;
        
        if (phrase) {
            searchUrl += "?all="+ encodeURIComponent(phrase) + "&";
        } else {
            searchUrl += "?";
        }
        
        return searchUrl;
        
    }
    
    
    
    
    /**********************************************************************************************    
    /******************************** CHART TABLES ************************************************
    /**********************************************************************************************/    

    function clearChartTables() {
        $('table[id$="ChartTable"] > thead').html("");
        $('table[id$="ChartTable"] > tbody').html("");
    }
    

    /**
     * 
     * Generates table from chart data
     * 
     * tableId - id of the table element
     * chart - chart data to generate table headers and rows from
     * xtickFormatter - function formatting chart.xticks, formatted values will be column headers
     */
    function generateChartTable(tableId, chart, xtickFormatter) {
        
        $table = $('#'+tableId);
        
        $thead = $table.children("thead");
     
        $tbody = $table.children("tbody");
        
        
        var $theadTr = $('<tr>');
        
        $theadTr.append('<th>'+analysisJsProperties.CHART_TABLE_SEARCHED_PHRASE+'</th>');
        
        
        for (var i = 0; i < chart.xticks.length; i++) {
            
            var thValue = xtickFormatter(chart.xticks[i][1]);
            $theadTr.append('<th>'+thValue+'</th>')
            
        }
        
        $thead.append($theadTr);
        
        
        for (var i = 0; i < chart.seriesList.length; i++) {
            
                        
            var phrase = generatedChartAnalysisForm.seriesFilters[i].phrase;
            
            if (phrase == null) {
                phrase = analysisJsProperties.CHART_TABLE_NULL_PHRASE;
            }
            
            var $rowTr = $('<tr>');
            
            $rowTr.append('<th scope="row">'+phrase+'</th>');
            
            var series = chart.seriesList[i];
            
            for (var j = 0; j < series.points.length; j++) {
                
                $rowTr.append('<td>'+formatNumber(series.points[j][1], 2)+getChartYNumberUnit()+'</td>');    
                
            }
            
            $tbody.append($rowTr);
        }

    }
}
