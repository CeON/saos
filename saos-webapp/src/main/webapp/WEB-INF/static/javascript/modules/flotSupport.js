/** Flot point tooltip controlling class. Its main responsibility is caring about proper
 * showing, hiding of chart point tooltip element.
 * 
 *  @param pointTooltipId id that will be given to any controlled tooltip element. It is assumed
 *  that there can be no more than one tooltip element on the page (controlled by one controller). 
 *  The specified id makes it easier to show/remove or replace this element with a new one.
 *  */
function FlotPointTooltipController(pointTooltipId) {
    
    var pointTooltipId = pointTooltipId;
    
    var currentPointTooltipItem = null;
        
    var _this = this;
        
    /**
     * Main entry point.<br/><br/>
     * Controls the point tooltip: generates and shows or removes one. <br/><br/>
     * If the item is specified and no tooltip is shown on the page, or the tooltip shown is a tooltip
     * for a different item, then a new tooltip is generated and shown.<br/>
     * If the item is not specified and the pos is outside the existing tooltip then the tooltip is removed.<br/>
     * 
     * @param pos position of a mouse pointer
     * @param item item pointed on a flot chart
     * @param generatePointTooltip function generating the point tooltip html element, the function will be passed 
     * the given item. <b> The returned element id will be changed to this.pointTooltipId </b>
     */    
    this.controlPointTooltip = function(pos, item, generatePointTooltip) {
            
        var $pointTooltip = $("#"+pointTooltipId);
        
        if (item) {
            if (currentPointTooltipItem == null || currentPointTooltipItem.dataIndex != item.dataIndex) {
                _this.removePointTooltip();
            }
            if (!$pointTooltip.length) {
                pointTooltipDiv = generatePointTooltip(item);
                pointTooltipDiv.attr('id', pointTooltipId);  
                
                _this.showPointTooltip(pointTooltipDiv, item.pageX, item.pageY);
                
                $pointTooltip = $("#"+pointTooltipId);
                $pointTooltip.mouseleave(function(event) {
                    _this.removePointTooltip();
                });
                currentPointTooltipItem = item;
            }
               
        } else {
            if ($pointTooltip.length) {
                if (!isInsideElement(pointTooltipId, pos.pageX, pos.pageY)) {
                    _this.removePointTooltip();
                    currentPointTooltipItem = null;
                }
            }
        }
    }
    

    /**
     * Shows the given tooltip in x,y page position.
     */
    this.showPointTooltip = function(pointTooltip, x, y) {

        
        pointTooltip
          .css({
                top: y-1,
                left: x-1,
                display: 'inline'
            }).appendTo("body");
        
        var windowWidth = $(window).width();
        var distanceToRight = windowWidth - x;
        if (distanceToRight < pointTooltip.width()) {
            x = x - pointTooltip.width() + 1;
        };
        pointTooltip.css("left", x);
    }
    
    /**
     * Removes tooltip controlled by this object
     */
    this.removePointTooltip = function() {
        $('#'+pointTooltipId).remove();
    }
    
    


}



/**
 * 
 * A set of methods that add or extend the functionality of the flot chart library
 * 
 */

/**
 * Formats x ticks labels so they do not overlap each other (hides some labels).
 * <br/>
 * @param minXtickLabelLength min length in px that should be reserved for each x tick label
 * @param containingDivId if set then only xticks contained by a div with the given containingDivId will be formatted, use it if
 * you want to e.g. format xticks of a specific chart
 * 
 * Taken from: http://www.willhallonline.co.uk/blog/dynamically-hide-tick-labels-when-using-flot-chart-categories
 */
function formatXTicks(minXtickLabelLength, containingDivId){
    
    var xWidth, xTicks;

    if (containingDivId == null) {
        xWidth = $('.flot-x-axis').width();
        xTicks = $('.flot-x-axis .flot-tick-label').length;
    } else {
        xWidth = $('#'+containingDivId).find('.flot-x-axis').width();
        xTicks = $('#'+containingDivId).find('.flot-x-axis .flot-tick-label').length;
    }
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
 * @param yNumberUnit unit of y number, e.g. % or $
 * */
function showYNumberPointTooltip(event, pos, item, decimalPrecision, yNumberUnit) {
	if (item) {
		
			$("#tooltip").remove();
			var x =  item.series.xaxis.ticks[item.dataIndex].label;
			x = x.replace(/<br\/>/g, "");
			
			var y = formatNumber(item.datapoint[1], decimalPrecision);
			
			if (yNumberUnit) {
			    y = y + yNumberUnit;
			}
			
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
	

    	