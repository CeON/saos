package pl.edu.icm.saos.common.chart.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pl.edu.icm.saos.common.chart.value.YearPeriod;

/**
 * @author Łukasz Dumiszewski
 */

public class PointYearPeriodValueFormatterTest {

    

    private PointYearPeriodValueFormatter formatter = new PointYearPeriodValueFormatter();
    
    
    
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
        assertTrue(formatter.handles(YearPeriod.class));
        
    }
    
    
    @Test
    public void format_Null() {
        
        // execute
        assertNull(formatter.format(null));
        
    }

    
    @Test
    public void format_OneYearPeriod() {
        
        // given
        YearPeriod value = new YearPeriod(2009, 2009);
        
        // execute
        assertEquals("2009", formatter.format(value));
        
    }

    
    @Test
    public void format_RangeYearPeriod() {
        
        // given
        YearPeriod value = new YearPeriod(2009, 2017);
        
        // execute
        assertEquals("2009-2017", formatter.format(value));
        
    }
    
}
