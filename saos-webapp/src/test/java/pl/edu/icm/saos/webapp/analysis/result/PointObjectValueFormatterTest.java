package pl.edu.icm.saos.webapp.analysis.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class PointObjectValueFormatterTest {

    private PointObjectValueFormatter formatter = new PointObjectValueFormatter();
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void handles_Null() {
        
        // execute
        formatter.handles(null);
        
    }
    
    
    @Test
    public void handles() {
        
        // execute & assert
        assertTrue(formatter.handles(Object.class));
        assertTrue(formatter.handles(String.class));
        
    }
    
    
    @Test
    public void format_Null() {
        
        // execute
        assertNull(formatter.format(null));
        
    }

    
    @Test
    public void format() {
        
        // given
        Object value = mock(Object.class);
        when(value.toString()).thenReturn("123");
        
        // execute
        assertEquals("123", formatter.format(value));
        
    }

    
}
