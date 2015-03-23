package pl.edu.icm.saos.webapp.analysis.result;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pl.edu.icm.saos.common.chart.Point;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart.FlotSeries;

/**
 * @author Łukasz Dumiszewski
 */

public class SeriesConverterTest {

    
    private SeriesConverter seriesConverter = new SeriesConverter();
    
    
        
    //------------------------ TESTS --------------------------
    
    
    @Test(expected = NullPointerException.class)
    public void convert_Null() {
        
        // execute
        seriesConverter.convert(null);
        
    }
    

    @Test
    public void convert() {
        
        // given
        
        Series<String, Number> series = new Series<>();
        
        Point<String, Number> point1 = new Point<>("A", 123); 
        Point<String, Number> point2 = new Point<>("B", 245);
        series.addPoint(point1);
        series.addPoint(point2);
        
        Number[] flotPoint1 = new Number[]{0, 123};
        Number[] flotPoint2 = new Number[]{1, 245};
        
        
        
        // execute
        
        FlotSeries uiSeries = seriesConverter.convert(series);
        
        
        // assert
        
        assertNotNull(uiSeries);
        assertEquals(2, uiSeries.getPoints().size());
        assertArrayEquals(flotPoint1, uiSeries.getPoints().get(0));
        assertArrayEquals(flotPoint2, uiSeries.getPoints().get(1));
        
    }

    
    

    
}
