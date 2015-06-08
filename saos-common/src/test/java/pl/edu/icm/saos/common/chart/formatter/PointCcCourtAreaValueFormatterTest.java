package pl.edu.icm.saos.common.chart.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pl.edu.icm.saos.common.chart.value.CcCourtArea;

/**
 * @author Łukasz Dumiszewski
 */

public class PointCcCourtAreaValueFormatterTest {

    
    private PointCcCourtAreaValueFormatter formatter = new PointCcCourtAreaValueFormatter();
    
    
    
    
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
        assertTrue(formatter.handles(CcCourtArea.class));
        
    }
    
    
    @Test
    public void format_Null() {
        
        // execute
        assertNull(formatter.format(null));
        
    }

    
    @Test
    public void format() {
        
        // given
        CcCourtArea ccCourtArea = new CcCourtArea();
        ccCourtArea.setName("Sąd Apelacyjny w Poznaniu");
        
        // execute
        assertEquals("Sąd Apelacyjny w Poznaniu", formatter.format(ccCourtArea));
        
    }

   
}
