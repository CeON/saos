package pl.edu.icm.saos.common.chart.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class PointLocalDateValueFormatterTest {

    private PointLocalDateValueFormatter formatter = new PointLocalDateValueFormatter();
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void handles_Null() {
        
        // execute
        formatter.handles(null);
        
    }
    
    
    @Test
    public void handles() {
        
        // execute & assert
        assertFalse(formatter.handles(Object.class));
        assertTrue(formatter.handles(LocalDate.class));
        
    }
    
    
    @Test
    public void format_Null() {
        
        // execute
        assertNull(formatter.format(null));
        
    }

    
    @Test
    public void format() {
        
        // given
        LocalDate value = new LocalDate(2013, 12, 1);
        
        // execute
        assertEquals("01/12/13", formatter.format(value));
        
    }

    
}
