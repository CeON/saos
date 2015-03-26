package pl.edu.icm.saos.common.chart.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.common.chart.value.DayPeriod;

/**
 * @author Łukasz Dumiszewski
 */

public class PointDayPeriodValueFormatterTest {

    private PointDayPeriodValueFormatter formatter = new PointDayPeriodValueFormatter();
    
    
    
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
        assertTrue(formatter.handles(DayPeriod.class));
        
    }
    
    
    @Test
    public void format_Null() {
        
        // execute
        assertNull(formatter.format(null));
        
    }

    
    @Test
    public void format_OneDayPeriod() {
        
        // given
        DayPeriod value = new DayPeriod(new LocalDate(2011, 12, 13), new LocalDate(2011, 12, 13));
        
        // execute
        assertEquals("13/12/11", formatter.format(value));
        
    }

    
    @Test
    public void format_RangeDayPeriod() {
        
        // given
        DayPeriod value = new DayPeriod(new LocalDate(2011, 11, 13), new LocalDate(2011, 12, 13));
        
        // execute
        assertEquals("13/11/11-13/12/11", formatter.format(value));
        
    }
}
