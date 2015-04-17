package pl.edu.icm.saos.webapp.analysis.generator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart.FlotSeries;


/**
 * @author Łukasz Dumiszewski
 */

public class FlotChartAggregatorTest {

    @InjectMocks
    private FlotChartAggregator chartAggregator = new FlotChartAggregator();
    
    @Mock
    private FlotSeriesAggregatorManager flotSeriesAggregatorManager;
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void aggregateChart_NULL_chart() {
        
        // execute
        chartAggregator.aggregateChart(null, UiyValueType.NUMBER);
        
    }
    
    @Test(expected = NullPointerException.class)
    public void aggregateChart_NULL_yValueType() {
        
        // execute
        chartAggregator.aggregateChart(mock(FlotChart.class), null);
        
    }
    
    @Test
    public void aggregateChart() {
        
        // given
        
        FlotChart chart = mock(FlotChart.class);
        FlotSeries series1 = mock(FlotSeries.class);
        FlotSeries series2 = mock(FlotSeries.class);
        
        List<FlotSeries> seriesList = Lists.newArrayList(series1, series2);
        
        when(chart.getSeriesList()).thenReturn(seriesList);
        
        UiyValueType yValueType = UiyValueType.PERCENT;
        
        FlotSeries aggrSeries1 = mock(FlotSeries.class);
        FlotSeries aggrSeries2 = mock(FlotSeries.class);
        
        when(flotSeriesAggregatorManager.aggregateFlotSeries(series1, 0, yValueType)).thenReturn(aggrSeries1);
        when(flotSeriesAggregatorManager.aggregateFlotSeries(series2, 1, yValueType)).thenReturn(aggrSeries2);
        
        
        // execute
        
        FlotChart aggrChart = chartAggregator.aggregateChart(chart, yValueType);
        
        // assert
        
        assertNotNull(aggrChart);
        assertThat(aggrChart.getSeriesList(), Matchers.contains(aggrSeries1, aggrSeries2));
        
    }
    
    
    
    
}
