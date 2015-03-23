package pl.edu.icm.saos.api.analysis;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.api.analysis.SeriesConverter;
import pl.edu.icm.saos.api.analysis.ApiChart.ApiSeries;
import pl.edu.icm.saos.common.chart.Point;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.common.chart.formatter.PointFormatter;

/**
 * @author Łukasz Dumiszewski
 */

public class SeriesConverterTest {

    
    private SeriesConverter seriesConverter = new SeriesConverter();
    
    private PointFormatter pointFormatter = mock(PointFormatter.class);
    
    
    
    @Before
    public void before() {
        
        seriesConverter.setPointFormatter(pointFormatter);
        
    }

    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected = NullPointerException.class)
    public void convert_Null() {
        
        // execute
        seriesConverter.convert(null);
        
    }
    

    @Test
    public void convert() {
        
        // given
        
        Series<String, Integer> series = new Series<>();
        
        Point<String, Integer> point1 = new Point<>("A", 1); 
        Point<String, Integer> point2 = new Point<>("B", 2);
        series.addPoint(point1);
        series.addPoint(point2);
        
        String[] apiPoint1 = new String[]{"X", "1243"};
        String[] apiPoint2 = new String[]{"Y", "223"};
        
        when(pointFormatter.formatPoint(point1)).thenReturn(apiPoint1);
        when(pointFormatter.formatPoint(point2)).thenReturn(apiPoint2);
        
        
        // execute
        
        ApiSeries apiSeries = seriesConverter.convert(series);
        
        
        // assert
        
        assertNotNull(apiSeries);
        assertEquals(2, apiSeries.getPoints().size());
        assertArrayEquals(apiPoint1, apiSeries.getPoints().get(0));
        assertArrayEquals(apiPoint2, apiSeries.getPoints().get(1));
        
    }

    
    

    
}
