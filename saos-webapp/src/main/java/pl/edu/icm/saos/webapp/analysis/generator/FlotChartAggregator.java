package pl.edu.icm.saos.webapp.analysis.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart.FlotSeries;

import com.google.common.base.Preconditions;

/**
 * 
 * A flot chart aggregator.
 * 
 * @author Łukasz Dumiszewski
 */
@Service("flotChartAggregator")
public class FlotChartAggregator {

    private FlotSeriesAggregatorManager flotSeriesAggregatorManager;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Aggregates all series of the given chart by using {@link #setFlotSeriesAggregatorManager(FlotSeriesAggregatorManager)}
     */
    public FlotChart aggregateChart(FlotChart chart, UiyValueType yValueType) {
        
        Preconditions.checkNotNull(chart);
        Preconditions.checkNotNull(yValueType);
        
        FlotChart aggregatedChart = new FlotChart();
        
        int i = 0;
        
        for (FlotSeries flotSeries : chart.getSeriesList()) {
            
            FlotSeries aggregatedFlotSeries = flotSeriesAggregatorManager.aggregateFlotSeries(flotSeries, i++, yValueType);
            
            aggregatedChart.addSeries(aggregatedFlotSeries);
            
        }

        return aggregatedChart;
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setFlotSeriesAggregatorManager(FlotSeriesAggregatorManager flotSeriesAggregatorManager) {
        this.flotSeriesAggregatorManager = flotSeriesAggregatorManager;
    }
    
}
