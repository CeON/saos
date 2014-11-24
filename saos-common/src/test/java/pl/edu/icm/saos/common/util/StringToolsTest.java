package pl.edu.icm.saos.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class StringToolsTest {

    
    @Test
    public void squashAndTrim_NULL() {
        
        assertNull(StringTools.squashAndTrim(null));
    }
    
    @Test
    public void squashAndTrim_Blank() {
        
        assertEquals("", StringTools.squashAndTrim("   \t\t "));
        
    }
    
    @Test
    public void squashAndTrim_Text() {
        
        assertEquals("Jan Nowak", StringTools.squashAndTrim("Jan \t\t  Nowak "));
        
    }
    
    
    @Test
    public void toRootLowerCase() {
    
        assertEquals("i", StringTools.toRootLowerCase("I"));
        
    }
    
    

    
}
