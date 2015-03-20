package pl.edu.icm.saos.webapp.analysis.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart.FlotSeries;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class ChartConverterTest {

    
    private ChartConverter chartConverter = new ChartConverter();
    
    private SeriesConverter seriesConverter = mock(SeriesConverter.class);
    
    private FlotXticksGenerator flotXticksGenerator = mock(FlotXticksGenerator.class);
        
    
    
    @Before
    public void before() {
        
        chartConverter.setSeriesConverter(seriesConverter);
        chartConverter.setFlotXticksGenerator(flotXticksGenerator);
    }

    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected = NullPointerException.class)
    public void convert_Null() {
        
        // execute
        chartConverter.convert(null);
        
    }
    

    @Test
    public void convert() {
        
        // given
        Chart<Object, Number> chart = createChart();
        
        FlotSeries flotSeries1 = mock(FlotSeries.class);
        FlotSeries flotSeries2 = mock(FlotSeries.class);
        
        List<Object[]> xticks = Lists.newArrayList();
        
        when(seriesConverter.convert(chart.getSeriesList().get(0))).thenReturn(flotSeries1);
        when(seriesConverter.convert(chart.getSeriesList().get(1))).thenReturn(flotSeries2);
        when(flotXticksGenerator.generateXticks(chart)).thenReturn(xticks);
        
        // execute
        FlotChart flotChart = chartConverter.convert(chart);
        
        // assert
        assertNotNull(flotChart);
        assertEquals(2, flotChart.getSeriesList().size());
        assertTrue(flotSeries1 == flotChart.getSeriesList().get(0));
        assertTrue(flotSeries2 == flotChart.getSeriesList().get(1));
        assertTrue(xticks == flotChart.getXticks());
    }

    
    

    
    
    //------------------------ PRIVATE --------------------------
    
    private Chart<Object, Number> createChart() {
        Chart<Object, Number> chart = new Chart<>();
        Series<Object, Number> series1 = new Series<>();
        series1.addPoint("XYZ", 123);
        chart.addSeries(series1);
        Series<Object, Number> series2 = new Series<>();
        series2.addPoint("XYZ", 223);
        chart.addSeries(series2);
        return chart;
    }

   

    
}
