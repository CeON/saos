package pl.edu.icm.saos.webapp.analysis.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import pl.edu.icm.saos.common.chart.formatter.PointValueFormatter;

/**
 * @author Łukasz Dumiszewski
 */

public class PointBrAddingValueFormatterTest {

    
    private PointValueFormatter delegatedFormatter = mock(PointValueFormatter.class);
    
    private PointBrAddingValueFormatter formatter = new PointBrAddingValueFormatter(delegatedFormatter);
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void handles() {
        
        // given
        when(delegatedFormatter.handles(Integer.class)).thenReturn(true);
        
        // execute & assert
        assertFalse(formatter.handles(String.class));
        assertTrue(formatter.handles(Integer.class));
        
    }
    
    
   
  
    @Test
    public void format() {
        
        // given
        Integer value = 3;
        when(delegatedFormatter.format(value)).thenReturn("3-3");
        
        // execute & assert
        assertEquals("3<br/>-<br/>3", formatter.format(value));
        
    }
}
