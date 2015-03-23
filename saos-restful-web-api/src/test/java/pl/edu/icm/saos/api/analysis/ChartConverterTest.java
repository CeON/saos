package pl.edu.icm.saos.api.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.api.analysis.ApiChart.ApiSeries;
import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Series;

/**
 * @author Łukasz Dumiszewski
 */

public class ChartConverterTest {

    
    private ChartConverter chartConverter = new ChartConverter();
    
    private SeriesConverter seriesConverter = mock(SeriesConverter.class);
    
        
    @Before
    public void before() {
        
        chartConverter.setSeriesConverter(seriesConverter);
        
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
        
        ApiSeries apiSeries1 = mock(ApiSeries.class);
        ApiSeries apiSeries2 = mock(ApiSeries.class);
        
        when(seriesConverter.convert(chart.getSeriesList().get(0))).thenReturn(apiSeries1);
        when(seriesConverter.convert(chart.getSeriesList().get(1))).thenReturn(apiSeries2);
        
        // execute
        ApiChart apiChart = chartConverter.convert(chart);
        
        // assert
        assertNotNull(apiChart);
        assertEquals(2, apiChart.getSeriesList().size());
        assertTrue(apiSeries1 == apiChart.getSeriesList().get(0));
        assertTrue(apiSeries2 == apiChart.getSeriesList().get(1));
        
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
