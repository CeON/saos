
    

var initAnalysisJs = function() {    

    
    
    /**********************************************************************************************    
    /******************************************* ATTRIBUTES ***************************************
    /**********************************************************************************************/   

    
    var _this = this;
    
    var mainChart = null;
    var aggregatedMainChart = null;
    var ccCourtChart = null;
    
    
    var generatedChartAnalysisForm = null; // analysis form used to generate charts (see chart variables above)
    
    var mainChartXtickFormatter = null;
    
    var pointTooltipId = "pointTooltip";
    
    var pointTooltipController = new FlotPointTooltipController(pointTooltipId);
    
    
    var isZoomed = false;
    var isChartGenerating = false; 
    
    // colours of subsequent series, the order of the colours is very important - 
    // each next colour has to differ as much as possible from preceding colours in order
    // for the series to look distinct 
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
    
    
    // replacements of phrases in cc court names 
    // parts of the court names on the x-axis of the ccCourt chart will be changed with the defined replacements
    // see: formatXCourtName function somewhere at the bottom
    var ccCourtNameReplacements = {};
    var plCcCourtNameReplacements = {"Sąd Apelacyjny":"SA", "Sąd Rejonowy":"SR", "Sąd Okręgowy":"SO", " w ":"<br/>w ", " we ":"<br/>we "}
    ccCourtNameReplacements['pl'] = plCcCourtNameReplacements;
    
    
    var analysisFormBaseAction = $('#analysisForm').attr('action');
    
    
    
    
    
    
    
    /**********************************************************************************************    
    /******************************** FORM INITIALIZATION & ACTIONS *******************************
    /**********************************************************************************************/   


    
    
    /**
     * Initializes certain analysis form elements
     */
    this.initFormElements = function() {
        
        
        
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

        _this.initInputs();

        _this.colourPhraseInputs();
        
        //Enables bootstrap tooltip
        $('#analysisForm [data-toggle="tooltip"]').tooltip({container: 'body'});
        
        /* Enable bootstrap popover's */
        $('[data-toggle="popover"]').popover({container: 'body'});

        /* Anchor with empty href, should not reload page*/
        $("a[href='']").click(function(event) {
            event.preventDefault();
        });
        
        _this.initButtons();
        
        
    }
    
    
    /**
     * Inits buttons of the analysisForm  
     */
    this.initButtons = function() {
        _this.initAddNewSearchPhraseButton();
        _this.initDeleteSearchPhraseButtons();
        _this.initExportToCsvButtons();
        
    }
    
    /**
     * Returns a colour for the specified index from colours array. A colour is always retrieved even if the
     * given index in bigger than colours.length (uses modulo inside)
     */
    this.getColour = function(index) {
    	
        return colours[index % colours.length];
    	
    }
    
    /**
     * Paints phrase input boxes with the same colours as corresponding series.
    */
    this.colourPhraseInputs = function() {
        
        $('[id^=inputColourBox_]').each(function() {
            $(this).css("background-color", getColour(extractIndex($(this))));
        });
        
    }
    

    
    /****************** REMOVAL OF PHRASE DIV **/
    
    /**
     * Inits deleteSearchPhrase buttons
     */
    this.initDeleteSearchPhraseButtons = function() {
    
        $('[id^=deletePhraseButton_]').click(function() {
            
            $(this).tooltip("hide");
            
            _this.deleteSearchPhrase(extractIndex($(this)));
            
        });
    }
    
    /**
     * Deletes the given search phrase
     * @param phraseIndexToRemove index (in the list of phrases) of the phrase to delete
     */
    this.deleteSearchPhrase = function(phraseIndexToRemove) {
        
        $('#analysisForm').attr('action', analysisFormBaseAction + "/removePhrase");
        $('#analysisForm').append($('<input>').attr('id', 'filterIndexToRemove').attr('type', 'hidden').attr('name', 'filterIndexToRemove').val(phraseIndexToRemove));
        
        _this.submitAndPrintAnalysisForm(true);
        
    }
    
    
    
    /****************** ADDING NEW PHRASE DIV **/

    /**
     * Inits addNewSearchPhraseButton
     */
    this.initAddNewSearchPhraseButton = function() {
        
        $('#addPhraseButton').click(function(){
        
              _this.addNewSearchPhrase();
        });
        
    }
    
    /**
     * Adds new search phrase
     */
    this.addNewSearchPhrase = function() {
        
        $('#analysisForm').attr('action', analysisFormBaseAction + "/addNewPhrase");
        
        _this.submitAndPrintAnalysisForm(true);
    }
    

    
    /**
     * Inits form inputs
     */
    this.initInputs = function() {
        
        $("[id^=seriesSearchPhraseInput]").change(function() {
            _this.generateCharts(true);
        });
        
        $("#yaxisValueType").change(function() {
            $('#yaxisValueTypeHidden').val($(this).val());
            _this.generateCharts(true);
        });
        
        
        $("[id^=seriesSearchPhraseInput]").keypress(function (e) {
            if (e.which == 13) {
                _this.generateCharts(true);
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
    this.initExportToCsvButtons = function() {
        $("[id^=exportToCsv]").click(function () {
            var chartCode = $(this).attr('id').split("-")[1];
            if (isChartGenerating) { // hold the csv generation if the chart is generating
                setTimeout(function() {_this.generateCsv(chartCode)}, 500);
            } else {
                _this.generateCsv(chartCode);
            }
        });
    }

    /**
     * Generates csv data for the chart with the given chartCode
     */
    this.generateCsv = function(chartCode) {
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
            _this.printMainChart(mainChart, null, null);
            
            $('[id$="ccCourtChartZoomCancelHint"]').html("");
            _this.printCcCourtChart(ccCourtChart, null, null);
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
    this.submitAndPrintAnalysisForm = function(regenerateChart) {
        
        
        $('#analysisForm').attr('method', 'post');
        
        $('#analysisForm').ajaxSubmit(function(view) {
            
            $('#analysisFormDiv').html(view);  
        
            _this.initFormElements();
            
            if (regenerateChart) {
                
                _this.generateCharts(true);
                
            }
            
        });
        
        
    }
    
    
    /**
     * Updates url in brower address and history
     */
    this.updateUrl = function() {
        $('[name="chartCode"]').remove();
        
        var newUrl = $(location).attr('protocol') + "//" + $(location).attr('host') + $(location).attr('pathname') + "?" + $("#analysisForm").serialize();
        
        history.pushState('html:newUrl', '', newUrl);
        
    }
    
    /**
     * Shows ajax loader gif in the given div during chart generation process 
     */
    this.showAjaxLoader = function(chartDivId) {
        ajaxLoader = $('#ajaxChartLoaderImg').clone();
        ajaxLoader.css('visibility', "visible");
        $('#'+chartDivId).html(ajaxLoader);
        
    }
    
    
    /**
     * Submits the analysisForm to a proper server endpoint and prints relevant charts based on
     * data received from the server.
     * @param updateUrlLocation should the updateUrl function be invoked after generating the chart
     */
    this.generateCharts = function(updateLocationUrl) {

        isChartGenerating = true;
        
        $('#analysisForm').attr('action', analysisFormBaseAction + "/generate");
            
        _this.showAjaxLoader("mainChart");
        _this.showAjaxLoader("aggregatedMainChart");
        _this.showAjaxLoader("ccCourtChart");
        
        $('#analysisForm').ajaxSubmit(function(analysisResult) {
             $('#analysisForm').attr('action', analysisFormBaseAction);
             
             
             generatedChartAnalysisForm = analysisResult.analysisForm;
             
             var charts = analysisResult.charts;
             
             mainChart = charts['MAIN_CHART'];
             aggregatedMainChart = charts['AGGREGATED_MAIN_CHART'];
             ccCourtChart = charts['CC_COURT_CHART'];
             
             _this.printMainChart(mainChart, null, null);
             _this.printAggregatedMainChart(aggregatedMainChart);
             _this.printCcCourtChart(ccCourtChart);
             
             if (updateLocationUrl) {
                 _this.updateUrl();   
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
    this.printMainChart = function(chart, xmin, xmax) {

        var yMinMax = calculateYAxisRangeForXRange(chart, xmin, xmax);
        var ymin = yMinMax[0];
        var ymax = yMinMax[1];
        
        var seriesArr = [];
        
        for (var i = 0; i < chart.seriesList.length; i++) {
            seriesArr.push({color: getColour(i), label: "", data: chart.seriesList[i].points, points: {fillColor: getColour(i)}});
        }
        
        // point periods for all axticks are the same, so the templating function will also be the same
        mainChartXtickFormatter = xtickPeriodFormatters[chart.xticks[0][1].period]; 
        
        $.plot($("#mainChart"), seriesArr,    {
                                                     lines: {
                                                         steps:false,
                                                         align: 'left',
                                                         show: true
                                                         
                                                    }, 
                                                    points: {
                                                        show:true 
                                                        
                                                        
                                                    },
                                                    xaxis: { 
                                                        min: xmin,
                                                        max: xmax,
                                                        tickSize: 1,
                                                        tickFormatter: function(val, axis) {
                                                            var xtick = chart.xticks[val][1];
                                                            return mainChartXtickFormatter(xtick);
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
        pointTooltipController.controlPointTooltip(pos, item, generateMainChartPointTooltip);
    });
    
    
    
    /** Generates tooltip div for the given point item of the MainChart */
    this.generateMainChartPointTooltip = function(item) {
        
        var pointTooltipDivId = "mainChartPointTooltip";
        var pointTooltipDiv = $('#'+pointTooltipDivId).clone();
        
        pointTooltipDiv.attr('id', pointTooltipId);
        
        var timePeriod = mainChart.xticks[item.dataIndex][1];
        var formattedTimePeriod = mainChartXtickFormatter(timePeriod).replace(/<br\/>/g," ");
        var judgmentCount = item.datapoint[1];
        var formattedJudgmentCount = formatNumber(judgmentCount, 2) + getChartYNumberUnit();
        
        pointTooltipDiv.find("#pointTimePeriod").html(formattedTimePeriod);
        pointTooltipDiv.find("#pointJudgmentCount").html(formattedJudgmentCount);
        
        
        if (judgmentCount > 0) {
            
            var searchUrl = _this.generateMainChartPointSearchUrl(item);
            
            pointTooltipDiv.find("#pointSearchLink").attr("href", searchUrl);
            
        } else {
            
            pointTooltipDiv.find("#pointSearchLinkDiv").remove();
        }
        
        
        return pointTooltipDiv;
    }
    
    
    /** Generates an url to the search page with a list of judgments for the given point of the MainChart */
    this.generateMainChartPointSearchUrl = function(item) {
        
        var searchUrl = _this.generateBasePointSearchUrl(item);
        
        searchUrl += _this.generatePointDatePeriodSearchUrlParams(item);
        
        searchUrl += "&" + _this.generateCourtCriteriaSearchUrlParams();
     
        return searchUrl;
    }
   
    
    this.generatePointDatePeriodSearchUrlParams = function(item) {
        
        var timePeriod = mainChart.xticks[item.dataIndex][1];
        
        var dateFrom = _this.extractStartDate(timePeriod);
        var dateTo = _this.extractEndDate(timePeriod);
        
        return _this.generateDateRangeSearchUrlParams(dateFrom, dateTo);
        
    }
    
    
    this.extractStartDate = function(timePeriod) {
        
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
    
    this.extractEndDate = function(timePeriod) {
        
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
        _this.printMainChart(mainChart, ranges.xaxis.from, ranges.xaxis.to);
        isZoomed = true;
    });
    
    
    
    
    

    /**********************************************************************************************    
    /******************************* AGGREGATED MAIN CHART ****************************************
    /**********************************************************************************************/    
    
    
    
    /*------------------------------------ PRINT CHART ----------------------------------------- */
    
    
    /**
     * Prints the aggregated main chart (number of judgments in the whole judgment date period)
     * @param chart json chart data, see pl.edu.icm.saos.webapp.chart.Chart 
     */
    this.printAggregatedMainChart = function(chart) {

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
    this.generateAggregatedMainChartPointTooltip = function(item) {
        
        var pointTooltipDivId = "aggregatedMainChartPointTooltip";
        var pointTooltipDiv = $('#'+pointTooltipDivId).clone();
        
        pointTooltipDiv.attr('id', pointTooltipId);
        
        var formattedTimePeriod = _this.formatMonth(_this.getGlobalStartDate()) + " - " + _this.formatMonth(_this.getGlobalEndDate()); 
        var judgmentCount = item.datapoint[1];
        var formattedJudgmentCount = _this.formatNumber(judgmentCount, 2) + _this.getChartYNumberUnit();
        
        pointTooltipDiv.find("#pointTimePeriod").html(formattedTimePeriod);
        pointTooltipDiv.find("#pointJudgmentCount").html(formattedJudgmentCount);
        
        
        if (judgmentCount > 0) {
            
            var searchUrl = _this.generateAggregatedChartPointSearchUrl(item);
            
            pointTooltipDiv.find("#pointSearchLink").attr("href", searchUrl);
            
        } else {
            
            pointTooltipDiv.find("#pointSearchLinkDiv").remove();
        }
        
        
        return pointTooltipDiv;
    }
    
    
    
    /** Generates an url to the search page with a list of judgments for the given point of the AggregatedMainChart */
    this.generateAggregatedChartPointSearchUrl = function(item) {
        
        var searchUrl = _this.generateBasePointSearchUrl(item);
        
        searchUrl += _this.generateDateRangeSearchUrlParams(_this.getGlobalStartDate(), _this.getGlobalEndDate());
        
        searchUrl += "&" + _this.generateCourtCriteriaSearchUrlParams();
     
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
    this.printCcCourtChart = function(chart, xmin, xmax) {

        if (chart == null) {
            $('#ccCourtChartDiv').hide();
            return;
        }
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
        
        $.plot($("#ccCourtChart"), seriesArr,    {
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
                                                                return _this.formatXCourtName(chart.xticks[val][1].name);
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
    this.formatXCourtName = function(courtName) {
        var replacements = ccCourtNameReplacements[analysisJsProperties.PAGE_LANG];
        for (key in replacements) {
           courtName = courtName.replace(key, replacements[key]);
        }
        return courtName;
    }
    
    

    /*----------------------------------- TOOLTIP ----------------------------------*/
    
      
    $("#ccCourtChart").on("plothover", function (event, pos, item) {
        pointTooltipController.controlPointTooltip(pos, item, generateCcCourtChartPointTooltip);
    });
    

    /** Generates tooltip div for the given point item of the CcCourtChart */
    this.generateCcCourtChartPointTooltip = function(item) {
        
        var pointTooltipDivId = "ccCourtChartPointTooltip";
        var pointTooltipDiv = $('#'+pointTooltipDivId).clone();
        
        pointTooltipDiv.attr('id', pointTooltipId);
        
        
        pointTooltipDiv.find("#pointCourtName").html(ccCourtChart.xticks[item.dataIndex][1].name);
        
        
        var formattedTimePeriod = _this.formatMonth(_this.getGlobalStartDate()) + " - " + _this.formatMonth(_this.getGlobalEndDate()); 
        pointTooltipDiv.find("#pointTimePeriod").html(formattedTimePeriod);
        
        
        var judgmentCount = item.datapoint[1];
        var formattedJudgmentCount = _this.formatNumber(judgmentCount, 2) + _this.getChartYNumberUnit();
        pointTooltipDiv.find("#pointJudgmentCount").html(formattedJudgmentCount);
        
        
        if (judgmentCount > 0) {
            
            var searchUrl = _this.generateCcCourtChartPointSearchUrl(item);
            
            pointTooltipDiv.find("#pointSearchLink").attr("href", searchUrl);
            
        } else {
            
            pointTooltipDiv.find("#pointSearchLinkDiv").remove();
        }
        
        
        return pointTooltipDiv;
    }
    
    
    /** Generates an url to the search page with a list of judgments for the given point of the CcCourtChart */
    this.generateCcCourtChartPointSearchUrl = function(item) {
        
        var searchUrl = _this.generateBasePointSearchUrl(item);
        
        searchUrl += _this.generateDateRangeSearchUrlParams(getGlobalStartDate(), _this.getGlobalEndDate());
        
        var courtId = ccCourtChart.xticks[item.dataIndex][1].courtId;
        
        var courtUrlParams = "courtCriteria.courtType=COMMON&courtCriteria.ccCourtId="+courtId+"&courtCriteria.ccIncludeDependentCourtJudgments=true";
        searchUrl += "&" + courtUrlParams;
     
        return searchUrl;
    }

    
    
    
    /*---------------------------------------- ZOOM --------------------------------------------*/
    
    $("#ccCourtChart").on("plotselected", function (event, ranges) {
        $('#ccCourtChartZoomCancelHint').text(analysisJsProperties.ZOOM_CANCEL_HINT);
        _this.printCcCourtChart(ccCourtChart, ranges.xaxis.from, ranges.xaxis.to);
        isZoomed = true;
    });

    
   
    
    
    /**********************************************************************************************    
    /******************************** XTICK FORMATTERS ********************************************
    /**********************************************************************************************/    

    
    var xtickPeriodFormatters = {};
        
    xtickPeriodFormatters['YEAR'] = function(xtick) {
        if (xtick.startYear === xtick.endYear) {
            return ""+xtick.startYear;
        } else {
            return xtick.startYear + " - " + xtick.endYear;
        }
    };
    
    xtickPeriodFormatters['MONTH'] = function(xtick) {
        if (xtick.startYear === xtick.endYear && xtick.startMonthOfYear === xtick.endMonthOfYear) {
            return formatMonth(new Date(xtick.startYear, xtick.startMonthOfYear-1, 1));
        } else {
            return formatMonth(new Date(xtick.startYear, xtick.startMonthOfYear-1, 1)) + "<br/> - <br/>" + formatMonth(new Date(xtick.endYear, xtick.endMonthOfYear-1, 1));
        }
    };
    
    xtickPeriodFormatters['DAY'] = function(xtick) {
        if (xtick.startDay.dayOfMonth === xtick.endDay.dayOfMonth 
                && xtick.startDay.monthOfYear === xtick.endDay.monthOfYear
                && xtick.startDay.year === xtick.endDay.year) {
            return formatDate(new Date(xtick.startDay.year, xtick.startDay.monthOfYear-1, xtick.startDay.dayOfMonth));
        } else {
            return formatDate(new Date(xtick.startDay.year, xtick.startDay.monthOfYear-1, xtick.startDay.dayOfMonth)) + "<br/> - <br/>" +
                   formatDate(new Date(xtick.endDay.year, xtick.endDay.monthOfYear-1, xtick.endDay.dayOfMonth));
        }
    };
   
    this.formatMonth = function(date) {
        return moment(date).locale(analysisJsProperties.PAGE_LANG).format("MMMM YYYY").replace(/ /g,"&nbsp;");
    }
 
    this.formatDate = function(date) {
        return moment(date).locale(analysisJsProperties.PAGE_LANG).format("DD MMMM YYYY").replace(/ /g,"&nbsp;");
    }
    
    
    
    
    /**********************************************************************************************    
    /******************************** POINT_TOOLTIP RELATED FUNCTIONS *****************************
    /**********************************************************************************************/    

    this.isChartYPercent = function() {
        return generatedChartAnalysisForm.ysettings.valueType === 'PERCENT';
    }
    
    this.isChartYPer1000 = function() {
        return generatedChartAnalysisForm.ysettings.valueType === 'NUMBER_PER_1000';
    }
    
    this.getChartYNumberUnit = function() {
        
        var numberUnit = '';
        
        if (_this.isChartYPercent()) {
            numberUnit = '%';
        }
        
        if (_this.isChartYPer1000()) {
            numberUnit = '‰';
        }
        
        return numberUnit;
    }
    
    
    this.generateCourtCriteriaSearchUrlParams = function() {
        return generateUrlParams(generatedChartAnalysisForm.globalFilter.courtCriteria, "courtCriteria");
    }

    
    this.getGlobalStartDate = function() {
        var dateRange = generatedChartAnalysisForm.globalFilter.judgmentDateRange;
        return moment([dateRange.startYear, dateRange.startMonth-1, 1]).toDate();
    }

    
    this.getGlobalEndDate = function() {
        var dateRange = generatedChartAnalysisForm.globalFilter.judgmentDateRange;
        return moment([dateRange.endYear, dateRange.endMonth-1, 1]).endOf("month").toDate();
    }
    
    
    this.generateDateRangeSearchUrlParams = function(dateFrom, dateTo) {
        var dateFrom = moment(dateFrom).format(DATE_PATTERN);
        var dateTo = moment(dateTo).format(DATE_PATTERN);
        
        return "dateFrom="+encodeURIComponent(dateFrom)+"&dateTo="+encodeURIComponent(dateTo);
    }
    
    this.generateBasePointSearchUrl = function(item) {
        
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
    /************************************ INITALIZATION *******************************************
    /**********************************************************************************************/    
    
    
    
    _this.initFormElements();
    
    _this.generateCharts(false);
    
    // reloads page on history back (without this
    // after changing the history in {@link #updateUrl()} the back button would not work properly)
    window.onpopstate = function(e){
        location.reload();
    };

}
