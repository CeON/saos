package pl.edu.icm.saos.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class CommonCourtUtilsTest {

    
    @Test
    public void isValidCommonCourtCode_NotNumeric() {
        assertFalse(CommonCourtUtils.isValidCommonCourtCode("150012A3"));
    }
    
    
    @Test
    public void isValidCommonCourtCode_LengthNot8() {
        assertFalse(CommonCourtUtils.isValidCommonCourtCode("150012031"));
    }
    
    
    @Test
    public void isValidCommonCourtCode_DoesNotStartWith15() {
        assertFalse(CommonCourtUtils.isValidCommonCourtCode("11001203"));
    }
    
    
    @Test
    public void isValidCommonCourtCode_Valid() {
        assertTrue(CommonCourtUtils.isValidCommonCourtCode("15001203"));
    }
    
    
    @Test
    public void extractAppealCourtCode() {
        assertEquals("60", CommonCourtUtils.extractAppealCourtNumber("15600203"));
    }
    
    
    @Test
    public void extractRegionalCourtCode() {
        assertEquals("02", CommonCourtUtils.extractRegionalCourtNumber("15600203"));
    }
    
    
    @Test
    public void extractDistrictCourtCode() {
        assertEquals("03", CommonCourtUtils.extractDistrictCourtNumber("15600203"));
    }
}
