package pl.edu.icm.saos.webapp.analysis.generator;

import org.springframework.stereotype.Service;

/**
 * 
 * Generator of x value of the aggregated series
 * 
 * @author Łukasz Dumiszewski
 */
@Service("aggregatedSeriesXValueGenerator")
public class AggregatedSeriesXValueGenerator {

    
    /**
     * Generates x value based on the given series index in chart
     */
    public int generateXValue(int seriesIndexInChart) {
        
        return (seriesIndexInChart*2)+1;
        
    }
    
}
