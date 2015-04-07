package pl.edu.icm.saos.webapp.analysis.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart.FlotSeries;

/**
 * @author Łukasz Dumiszewski
 */

public class RateFlotSeriesAggregatorTest {

    @InjectMocks
    private RateFlotSeriesAggregator seriesAggregator = new RateFlotSeriesAggregator();
    
    @Mock
    private AggregatedSeriesXValueGenerator aggregatedSeriesXValueGenerator = mock(AggregatedSeriesXValueGenerator.class);
    
    
    @Before
    public void before() {
        
        initMocks(this);
    }
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected = NullPointerException.class)
    public void handles_NULL() {
        
        // execute
        seriesAggregator.handles(null);
        
    }
    
    
    @Test
    public void handles_FALSE() {
        
        // execute & assert
        assertFalse(seriesAggregator.handles(UiyValueType.NUMBER));
        
    }
    
    
    @Test
    public void handles_TRUE() {
        
        // execute & assert
        assertTrue(seriesAggregator.handles(UiyValueType.PERCENT));
        assertTrue(seriesAggregator.handles(UiyValueType.NUMBER_PER_1000));
        
    }
    
    
    @Test
    public void aggregateFlotSeries() {
        
        // given
        
        FlotSeries flotSeries = new FlotSeries();
        flotSeries.addPoint(new Integer[]{0, 10});
        flotSeries.addPoint(new Integer[]{1, 20});
        flotSeries.addPoint(new Integer[]{2, 30});
        
        when(aggregatedSeriesXValueGenerator.generateXValue(0)).thenReturn(1);
        
        // execute
        
        FlotSeries aggregatedSeries = seriesAggregator.aggregateFlotSeries(flotSeries, 0);
        
        
        // assert
        
        assertNotNull(aggregatedSeries);
        assertNotNull(aggregatedSeries.getPoints());
        assertEquals(1, aggregatedSeries.getPoints().size());
        assertEquals(1, aggregatedSeries.getPoints().get(0)[0]);
        assertTrue(Math.abs(aggregatedSeries.getPoints().get(0)[1].floatValue() - 20) < 0.001);
    }
}

