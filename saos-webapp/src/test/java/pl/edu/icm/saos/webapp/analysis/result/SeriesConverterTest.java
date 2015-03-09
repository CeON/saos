package pl.edu.icm.saos.webapp.analysis.result;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.result.Point;
import pl.edu.icm.saos.search.analysis.result.Series;
import pl.edu.icm.saos.webapp.analysis.result.UiChart.UiSeries;

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
        
        String[] uiPoint1 = new String[]{"X", "1243"};
        String[] uiPoint2 = new String[]{"Y", "223"};
        
        when(pointFormatter.formatPoint(point1)).thenReturn(uiPoint1);
        when(pointFormatter.formatPoint(point2)).thenReturn(uiPoint2);
        
        
        // execute
        
        UiSeries uiSeries = seriesConverter.convert(series);
        
        
        // assert
        
        assertNotNull(uiSeries);
        assertEquals(2, uiSeries.getData().size());
        assertArrayEquals(uiPoint1, uiSeries.getData().get(0));
        assertArrayEquals(uiPoint2, uiSeries.getData().get(1));
        
    }

    
    

    
}
