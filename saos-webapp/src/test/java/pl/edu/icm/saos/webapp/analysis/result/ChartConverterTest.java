package pl.edu.icm.saos.webapp.analysis.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.result.Chart;
import pl.edu.icm.saos.search.analysis.result.Series;
import pl.edu.icm.saos.webapp.analysis.result.UiChart.UiSeries;

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
        
        UiSeries uiSeries1 = new UiSeries();
        UiSeries uiSeries2 = new UiSeries();
        
        when(seriesConverter.convert(chart.getSeriesList().get(0))).thenReturn(uiSeries1);
        when(seriesConverter.convert(chart.getSeriesList().get(1))).thenReturn(uiSeries2);
        
        // execute
        UiChart uiChart = chartConverter.convert(chart);
        
        // assert
        assertNotNull(uiChart);
        assertEquals(2, uiChart.getSeriesList().size());
        assertEquals(uiSeries1, uiChart.getSeriesList().get(0));
        assertEquals(uiSeries2, uiChart.getSeriesList().get(1));
        
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
