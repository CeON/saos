package pl.edu.icm.saos.webapp.analysis.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.common.chart.value.Week;

/**
 * @author Łukasz Dumiszewski
 */

public class PointWeekBrValueFormatterTest {

    
    private PointWeekBrValueFormatter formatter = new PointWeekBrValueFormatter();
    
    
    
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
        assertTrue(formatter.handles(Week.class));
        
    }
    
    
    @Test
    public void format_Null() {
        
        // execute
        assertNull(formatter.format(null));
        
    }

    
    @Test
    public void format() {
        
        // given
        LocalDate startDate = new LocalDate(2013, 12, 1);
        LocalDate endDate = new LocalDate(2013, 12, 7);
        Week week = new Week(startDate, endDate);
        
        // execute
        assertEquals("01/12/13<br/>-<br/>07/12/13", formatter.format(week));
        
    }
}
