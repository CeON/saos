package pl.edu.icm.saos.webapp.analysis.generator;

import pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart.FlotSeries;

/**
 * 
 * A contract for classes which aggregate {@link FlotSeries}.
 * 
 * @author Łukasz Dumiszewski
 */

public interface FlotSeriesAggregator {

    /**
     * Aggregates the given flotSeries. The result of aggregation is a new flotSeries with
     * (supposedly) one x-value and y-value. 
     * @param flotSeries a flot series to aggregate
     * @param flotSeriesIndex a flot series index that can be used in aggregation (think about an
     * aggregation of a flot series as a part of a chart (that can hold more than one series) aggregation)
     * 
     */
    FlotSeries aggregateFlotSeries(FlotSeries flotSeries, int flotSeriesIndex);
    
    /**
     * Returns true if the aggregator handles aggregating of {@link FlotSeries} that
     * holds y values of the specified type. 
     */
    boolean handles(UiyValueType uiyValueType);
    
}
