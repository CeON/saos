package pl.edu.icm.saos.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class CommonCourtDivisionUtilsTest {
    
    
    @Test
    public void isValidCcDivisionCode_3digit() {
        assertTrue(CommonCourtDivisionUtils.isValidCcDivisionCode("550"));
    }
    
    @Test
    public void isValidCcDivisionCode_4digit() {
        assertTrue(CommonCourtDivisionUtils.isValidCcDivisionCode("1503"));
    }
    
    @Test
    public void isValidCcDivisionCode_DivisionNumberNotDivisibleBy5() {
        assertFalse(CommonCourtDivisionUtils.isValidCcDivisionCode("1303"));
    }
   
    @Test
    public void isValidCcDivisionCode_NotNumeric() {
        assertFalse(CommonCourtDivisionUtils.isValidCcDivisionCode("1I03"));
    }
    
   
    
    @Test
    public void extractDivisionCode_3digit() {
        assertEquals("56", CommonCourtDivisionUtils.extractDivisionTypeCode("556"));
    }
    
    
    @Test
    public void extractDivisionCode_4digit() {
        assertEquals("63", CommonCourtDivisionUtils.extractDivisionTypeCode("1563"));
    }
    
   
    @Test
    public void extractDivisionNumber_3digit() {
        assertEquals("5", CommonCourtDivisionUtils.extractDivisionNumber("556"));
    }
   
    
    @Test
    public void extractDivisionNumber_4digit() {
        assertEquals("15", CommonCourtDivisionUtils.extractDivisionNumber("1536"));
    }
    
    
    @Test
    public void extractNormalizedDivisionNumber_4digit() {
        assertEquals(3, CommonCourtDivisionUtils.extractNormalizedDivisionNumber("1536"));
    }
}