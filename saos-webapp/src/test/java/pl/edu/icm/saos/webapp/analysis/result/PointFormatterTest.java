package pl.edu.icm.saos.webapp.analysis.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.result.Point;

/**
 * @author Łukasz Dumiszewski
 */

public class PointFormatterTest {

    
    private PointFormatter pointFormatter = new PointFormatter();
    
    private PointValueFormatterManager pointValueFormatterManager = mock(PointValueFormatterManager.class);
    
    
    @Before
    public void before() {
        
        pointFormatter.setPointValueFormatterManager(pointValueFormatterManager);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void formatPoint() {
        
        // given
        Point<String, Integer> point = new Point<>("AAA", 123);
        when(pointValueFormatterManager.format("AAA")).thenReturn("AAA");
        when(pointValueFormatterManager.format(123)).thenReturn("123");
        
        // execute
        String[] formattedPoint = pointFormatter.formatPoint(point);
        
        // assert
        assertNotNull(formattedPoint);
        assertEquals(2, formattedPoint.length);
        assertEquals("AAA", formattedPoint[0]);
        assertEquals("123", formattedPoint[1]);
    }
    
}
