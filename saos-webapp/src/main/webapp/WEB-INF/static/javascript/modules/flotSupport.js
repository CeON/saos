/**
 * 
 * A set of methods that add or extend the functionality of the flot chart library
 * 
 */

/**
 * Formats x ticks labels so they do not overlap each other (hides some labels).
 * <br/>
 * @param minXtickLabelLength min length in px that should be reserved for each x tick label
 * 
 * Taken from: http://www.willhallonline.co.uk/blog/dynamically-hide-tick-labels-when-using-flot-chart-categories
 */
function formatXTicks(minXtickLabelLength){
    
    var xWidth = $('.flot-x-axis').width();
    var xTicks = $('.flot-x-axis .flot-tick-label').length;
    
    var limiter = Math.floor(xWidth/xTicks);
            
    if(limiter < minXtickLabelLength){
                
        $('.flot-x-axis .flot-tick-label').each(function(index){
            if(index%Math.floor(minXtickLabelLength/limiter) > 0){
                $(this).css("display","none");
            }
         });
     
    }
}


/** Shows a point tooltip with a number on y-axis. The number values are formatted (there are spaces added
 * every 3 digits) 
 * 
 * @param decimalPrecision the precision that will be used for formatting numbers that are not integers
 * */
function showYNumberPointTooltip(event, pos, item, decimalPrecision) {
	if (item) {
		
			$("#tooltip").remove();
			var x =  item.series.xaxis.ticks[item.dataIndex].label;
			x = x.replace(/<br\/>/g, "");
			
			var y = formatNumber(item.datapoint[1], decimalPrecision);
			
			showTooltip(item.pageX, item.pageY, x + ",  <b>" + y + "</b>");
		
	} else {
		$("#tooltip").remove();
		
	}
}



/** Shows number/number point tooltip on flot plothover event */
function showNumberNumberPointTooltip(event, pos, item) {
	if (item) {
		
			$("#tooltip").remove();
			var x = item.datapoint[0].toFixed(0), y = addSpacesEvery3Digits(item.datapoint[1].toFixed(0)-item.datapoint[2].toFixed(0));

			showTooltip(item.pageX, item.pageY, x + ", <b>" + y + "</b>");
		
	} else {
		$("#tooltip").remove();
		
	}
}


/**
 * Returns the Y axis value for the given x value
 */
function getY(series, x) {
	var index;
	var lastValue = null;
	for (index = 0; index < series.length; index++) {
		var currentValue = series[index][0];
		if (series[index][0]==x || 
			(lastValue!=null && (
				(x>lastValue && x<currentValue) || (x < lastValue && x > currentValue)
			))) {
	    	return series[index][1];
	    }
		lastValue = currentValue;
	}
	
}


/**
 * Returns min y value for the given chart and x range. Note that every chart can have many series - the returned value is the minimum y value
 * of all series
 * @param chart pl.edu.icm.sedno.web.statistics.Chart object
 * @param xmin
 * @param xmax
 */
function getMinYForXRange(chart, xmin, xmax) {
	return getMinMaxYForXRange(chart, xmin, xmax, false);
}

/**
 * Returns max y value for the given chart and x range. Note that every chart can have many series - the returned value is the maximum y value
 * of all series
 * @param chart pl.edu.icm.sedno.web.statistics.Chart object
 * @param xmin
 * @param xmax
 */
function getMaxYForXRange(chart, xmin, xmax) {
	return getMinMaxYForXRange(chart, xmin, xmax, true);
}

/**
 * Shows the tooltip for the point in chart
 * @param x position of the point on the x axis
 * @param y position of the point on the y axis
 * @param contents Content of the tooltip
 */
function showTooltip(x, y, content) {
    $('<div id="tooltip">' + content + '</div>').css( {
        position: 'absolute',
        display: 'none',
        top: y + 5,
        left: x + 17,
        border: '1px solid #fdd',
        padding: '4px',
        'background-color': '#fee',
        opacity: 0.80
    }).appendTo("body").fadeIn(200);
}

/**
 * Returns the Y axis range for the given x range. The returned value is a 2-element array with ymin as the first element and ymax as the second element.
 * ymin is the minimum y value of the y axis range and ymax is the maximum value for that axis. 
 * ymin is calculated as #getMinYForRange - ((#getMaxYForRange - #getMinYForRange) * 0.1)
 * ymin is calculated as #getMaxYForRange + ((#getMaxYForRange - #getMinYForRange) * 0.1)
 * if xmin or xmas == null then ymin and ymax will be null too
 * @param chart chart data, js equivalent of pl.edu.icm.saos.webapp.chart.Chart
 * @param xmin min value of x range
 * @param xmax max value of x range 
 * @param yminLessThanZeroToZero if set to true then if ymin < 0 -> ymin=0, if not set defaults to true
 * Does not currently support stacked charts
 */
function calculateYAxisRangeForXRange(chart, xmin, xmax, yminLessThanZeroToZero) {
	var yAxisRange = [];
	
	var ymin = null;
	var ymax = null;
	
	if (xmin != null && xmax != null) {
		ymax = getMaxYForXRange(chart, xmin, xmax);
		ymin = getMinYForXRange(chart, xmin, xmax);
	
		yMinMaxDelta = ymax - ymin;
		ymax = ymax + (yMinMaxDelta * 0.1);
		ymin = ymin - (yMinMaxDelta * 0.1);

	}
	
	if (yminLessThanZeroToZero==null || yminLessThanZeroToZero==true) {
		if (ymin<0) {
			ymin=0;
		}
	}
	
	
	yAxisRange.push(ymin);
	yAxisRange.push(ymax);

	
	return yAxisRange;
}

/** Returns min x value for the given series */
function getMinX(series) {
	var minX = null;
	var index;
	for (index = 0; index < series.points.length; index++) {
		var currentX = parseFloat(series.points[index][0]);
		if (minX == null || minX > currentX) {
			minX = currentX;
		}
	};
	return minX;
}

/** Returns max x value for the given series */
function getMaxX(series) {
	var maxX = null;
	var index;
	for (index = 0; index < series.points.length; index++) {
		var currentX = parseFloat(series.points[index][0]);
		if (maxX == null || maxX < currentX) {
			maxX = currentX;
		}
	};
	return maxX;
}


/* private */     	
function getMinMaxYForXRange(chart, xmin, xmax, max) {
	var index, seriesIndex;
	var minMaxValue = null;
	exloop: for (seriesIndex = 0; seriesIndex < chart.seriesList.length; seriesIndex++) {
		var series = chart.seriesList[seriesIndex];
		inloop: for (index = 0; index < series.points.length; index++) {
			var currentValue = parseInt(series.points[index][1]);
			var currentX = parseFloat(series.points[index][0]);
			if (parseFloat(xmin) > currentX) {
				continue inloop;
			}
			if (parseFloat(xmax) < currentX) {
				continue exloop;
			};
			if (max) {
				if (minMaxValue == null || minMaxValue < currentValue) {
					minMaxValue = currentValue;
				};
			} else {
				if (minMaxValue == null || minMaxValue > currentValue) {
					minMaxValue = currentValue;
				};
			}

		};
	};
	return minMaxValue;
};
 

/**
 * 
 * @param chartDivId div where the chart will be placed
 * @param chartData see pl.edu.icm.sedno.web.chart.Chart
 * @param xmin min x value, used for zooming, can be null
 * @param xmax max x value, used for zooming, can be null
 */

function printIntIntBarChart(chartDivId, chartData, xmin, xmax) {

	var yMinMax = calculateYAxisRangeForXRange(chartData, xmin, xmax);
	var ymin = yMinMax[0];
	var ymax = yMinMax[1];
	
	var colors = [];
	colors[0] = "#0000BB";
	colors[1] = "#EE0000";
	colors[2] = "#00BB00";
	
	var data = [];
	for (var i=0; i<chartData.seriesList.length; i++) {
		data[i] = {color: colors[i], label: '', data: chartData.seriesList[i].data};
	}
	
	$.plot($("#"+chartDivId), data,	{
	                         				    bars: {
	                         				    	barWidth: 0.8,
	                         				    	show: true,
	                         				    	align: 'center'
	                        					}, 
	                        				    xaxis: { 
	                        				    	min: xmin,
	                        				    	max: xmax,
	                        				    	tickDecimals: 0
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
	

    	