package pl.edu.icm.saos.common.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class JsonNormalizerTest {

    
    @Test
    public void normalizeJson_DoubleQuotted() {
        
        String value = JsonNormalizer.normalizeJson("{\"bre\":\"sss\",\"arr\":[\"1112\",\"abc\"]}");
        
        assertEquals("{\"bre\":\"sss\",\"arr\":[\"1112\",\"abc\"]}", value);
        
    }
    
    
    @Test
    public void normalizeJson_SingleQuottedWithSpaces() {
        
        String value = JsonNormalizer.normalizeJson("{bre:'sss'\n, arr:['1112','abc']}\n");
        
        assertEquals("{\"bre\":\"sss\",\"arr\":[\"1112\",\"abc\"]}", value);
        
    }
    
}
