package pl.edu.icm.saos.webapp.analysis.generator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType.NUMBER;

import java.util.List;

import org.junit.Test;

import pl.edu.icm.saos.webapp.analysis.result.FlotChart.FlotSeries;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class FlotSeriesAggregatorManagerTest {
    
    
    private FlotSeriesAggregatorManager flotSeriesAggregatorManager = new FlotSeriesAggregatorManager();
    
        
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void aggregateFlotSeries_NULL_flotSeries() {
        
        // execute
        flotSeriesAggregatorManager.aggregateFlotSeries(null, 0, NUMBER);
        
    }
    
    
    @Test(expected = NullPointerException.class)
    public void aggregateFlotSeries_NULL_yValueType() {
        
        // execute
        flotSeriesAggregatorManager.aggregateFlotSeries(mock(FlotSeries.class), 0, null);
        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void aggregateFlotSeries_NO_HANDLING_AGGREGATOR() {
        
        // given
        
        FlotSeriesAggregator seriesAggregator1 = mock(FlotSeriesAggregator.class);
        when(seriesAggregator1.handles(NUMBER)).thenReturn(false);
        List<FlotSeriesAggregator> flotSeriesAggregators = Lists.newArrayList(seriesAggregator1);
        flotSeriesAggregatorManager.setFlotSeriesAggregators(flotSeriesAggregators);
        
        // execute
        flotSeriesAggregatorManager.aggregateFlotSeries(mock(FlotSeries.class), 0, NUMBER);
    }
    
    @Test
    public void aggregateFlotSeries() {
        
        // given
        
        FlotSeriesAggregator seriesAggregator1 = mock(FlotSeriesAggregator.class);
        FlotSeriesAggregator seriesAggregator2 = mock(FlotSeriesAggregator.class);
        
        when(seriesAggregator1.handles(NUMBER)).thenReturn(false);
        when(seriesAggregator2.handles(NUMBER)).thenReturn(true);
        
        List<FlotSeriesAggregator> flotSeriesAggregators = Lists.newArrayList(seriesAggregator1, seriesAggregator2);
        flotSeriesAggregatorManager.setFlotSeriesAggregators(flotSeriesAggregators);
        
        FlotSeries series = mock(FlotSeries.class);
        FlotSeries aggrSeries = mock(FlotSeries.class);
        
        when(seriesAggregator2.aggregateFlotSeries(series, 0)).thenReturn(aggrSeries);
        
        
        // execute
        
        FlotSeries retSeries = flotSeriesAggregatorManager.aggregateFlotSeries(series, 0, NUMBER);
        
        // assert
        
        assertTrue(aggrSeries == retSeries);
        
    }

}
