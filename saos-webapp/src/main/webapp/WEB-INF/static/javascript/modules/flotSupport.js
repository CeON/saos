/** Flot point tooltip controlling class. Its main responsibility is caring about proper
 * showing, hiding of chart point tooltip element.
 * 
 *  @param pointTooltipId id that will be given to any controlled tooltip element. It is assumed
 *  that there can be no more than one tooltip element for the given chart. 
 *  The specified id makes it easier to show/remove or replace this element with a new one. 
 *  @param generatePointTooltip function generating the point tooltip html element, the function will be passed 
 *  the current item (see #controlPointTooltip(pos, item)). 
 *  @param onPointTooltipOpenAction callback fired after opening the point tooltip, can be null
 *  @param onPointTooltipCloseAction callback fired after closing the point tooltip, it will be passed current pos and item (see #controlPointTooltip(pos, item)), can be null
 *   
 *  */
function FlotPointTooltipController(pointTooltipId, generatePointTooltip, onPointTooltipOpenAction, onPointTooltipCloseAction) {
    
    var pointTooltipId = pointTooltipId;
    
    var generatePointTooltip = generatePointTooltip;
    
    var onPointTooltipCloseAction = onPointTooltipCloseAction;
    
    var onPointTooltipOpenAction = onPointTooltipOpenAction;
    
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
     */    
    this.controlPointTooltip = function(pos, item) {
            
        var $pointTooltip = $("#"+pointTooltipId);
        
        if (item) {
            if (currentPointTooltipItem == null || currentPointTooltipItem.dataIndex != item.dataIndex
                                                || currentPointTooltipItem.seriesIndex != item.seriesIndex) {
                _this.closePointTooltip();
            }
            if (!$pointTooltip.length) {
                pointTooltipDiv = generatePointTooltip(item);
                pointTooltipDiv.attr('id', pointTooltipId);  
                
                _this.showPointTooltip(pointTooltipDiv, item.pageX, item.pageY);
                
                $pointTooltip = $("#"+pointTooltipId);
                $pointTooltip.mouseleave(function(event) {
                    _this.closePointTooltip();
                });
                currentPointTooltipItem = item;
                
                if (onPointTooltipOpenAction) {
                    onPointTooltipOpenAction(pos, item);
                }
            }
        } else {
            if ($pointTooltip.length) {
                if (!isInsideElement(pointTooltipId, pos.pageX, pos.pageY)) {
                    _this.closePointTooltip();
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
    this.closePointTooltip = function() {
        $('#'+pointTooltipId).remove();
        if (onPointTooltipCloseAction) {
            onPointTooltipCloseAction();
        }
    }
    
    


}


/**
 * Function highlighting the current series and diminishing other ones. You can use it in
 * flot plothover/plotclick events. The function assumes that series colors are given as rgb strings (e.g. 'rgb(0, 255, 0)'),
 * if the colors of the series have different formats then this function will not work properly.
 * 
 * @param plot the current plot
 * @param pointTooltipId the id of the tooltip of the current point of the series, the highlighting will be kept while hovering
 * on the tooltip element; set it to null if you do not want this behaviour 
 * <br/>
 * The 3 parameters below comes from the flot plothover/plotclick event callback function
 * @param pos mouse position
 * @param item the current item
 */
function highlightCurrentSeries(plot, pointTooltipId, pos, item) {
    var re = /\(([0-9]+,[0-9]+,[0-9]+)/;
    var opacity = 1;
    var seriesIdx = -1;
    
    if (item) {
        seriesIdx = item.seriesIndex;
        opacity = 0.05;
    } 
    
    if (pointTooltipId) {
        if ($("#"+pointTooltipId).length && isInsideElement("pointTooltip", pos.pageX, pos.pageY)) {
            if (!item) {
                return;
            }
        }
    }
    
    // loop all the series and adjust the opacity 
    var modSeries = 
    $.map(plot.getData(),function(series,idx){
        
        var seriesColor = series.color.replace(/\s/g,"");
        var calcColor;
        
        if (idx == seriesIdx){
           calcColor = 'rgba(' + re.exec(seriesColor)[1] + ',' + 2 + ')'  
        } else {
           calcColor = 'rgba(' + re.exec(seriesColor)[1] + ',' + opacity + ')';
        }
        
        series.color = calcColor
        series.points.fillColor = calcColor;
        
        return series;
    });
    
    // reload the series and redraw
    plot.setData(modSeries);
    plot.draw();
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
	

    	