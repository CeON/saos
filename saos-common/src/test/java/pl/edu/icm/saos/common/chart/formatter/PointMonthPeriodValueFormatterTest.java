package pl.edu.icm.saos.common.chart.formatter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pl.edu.icm.saos.common.chart.value.MonthPeriod;

/**
 * @author Łukasz Dumiszewski
 */

public class PointMonthPeriodValueFormatterTest {

    private PointMonthPeriodValueFormatter formatter = new PointMonthPeriodValueFormatter();
    
    
    
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
        assertTrue(formatter.handles(MonthPeriod.class));
        
    }
    
    
    @Test
    public void format_Null() {
        
        // execute
        assertNull(formatter.format(null));
        
    }

    
    @Test
    public void format_OneMonthPeriod() {
        
        // given
        MonthPeriod value = new MonthPeriod(2009, 10, 2009, 10);
        
        // execute
        assertEquals("10/2009", formatter.format(value));
        
    }

    
    @Test
    public void format_RangeMonthPeriod() {
        
        // given
        MonthPeriod value = new MonthPeriod(2009, 10, 2009, 11);
        
        // execute
        assertEquals("10/2009-11/2009", formatter.format(value));
        
    }
    
}
