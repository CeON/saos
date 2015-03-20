package pl.edu.icm.saos.common.chart.formatter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pl.edu.icm.saos.common.chart.value.MonthYear;

/**
 * @author Łukasz Dumiszewski
 */

public class PointMonthYearValueFormatterTest {

    private PointMonthYearValueFormatter formatter = new PointMonthYearValueFormatter();
    
    
    
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
        assertTrue(formatter.handles(MonthYear.class));
        
    }
    
    
    @Test
    public void format_Null() {
        
        // execute
        assertNull(formatter.format(null));
        
    }

    
    @Test
    public void format() {
        
        // given
        MonthYear value = new MonthYear(12, 2013);
        
        // execute
        assertEquals("12/2013", formatter.format(value));
        
    }

    
}
