package pl.edu.icm.saos.importer.commoncourt.court;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class CommonCourtDivisionUtilsTest {
    
    
    @Test
    public void isValidCcDivisionCode_3digit() {
        assertTrue(CommonCourtDivisionUtils.isTolerantlyValidCcDivisionCode("550"));
    }
    
    @Test
    public void isValidCcDivisionCode_4digit() {
        assertTrue(CommonCourtDivisionUtils.isTolerantlyValidCcDivisionCode("1503"));
    }
    
    @Test
    public void isStrictlyValidCcDivisionCode_DivisionNumberNotDivisibleBy5() {
        assertFalse(CommonCourtDivisionUtils.isStrictlyValidCcDivisionCode("1303"));
    }
    
    @Test
    public void isTolerantlyValidCcDivisionCode_DivisionNumberNotDivisibleBy5() {
        assertTrue(CommonCourtDivisionUtils.isTolerantlyValidCcDivisionCode("1303"));
    }
   
    @Test
    public void isValidCcDivisionCode_NotNumeric() {
        assertFalse(CommonCourtDivisionUtils.isTolerantlyValidCcDivisionCode("1I03"));
    }
    
   
    
    @Test
    public void extractDivisionCode_3digit() {
        assertEquals("56", CommonCourtDivisionUtils.tryExtractDivisionTypeCode("556"));
    }
    
    
    @Test
    public void extractDivisionCode_4digit() {
        assertEquals("63", CommonCourtDivisionUtils.tryExtractDivisionTypeCode("1563"));
    }
    
   
    @Test
    public void extractDivisionNumber_3digit() {
        assertEquals("05", CommonCourtDivisionUtils.tryExtractDivisionNumber("556"));
    }
   
    
    @Test
    public void extractDivisionNumber_4digit() {
        assertEquals("15", CommonCourtDivisionUtils.tryExtractDivisionNumber("1536"));
    }
    
    
    @Test
    public void extractNonLocalType_3digit() {
        assertNull(CommonCourtDivisionUtils.tryExtractNonLocalType("123"));
    }
    
    @Test
    public void extractNonLocalType_5digit() {
        assertEquals("1", CommonCourtDivisionUtils.tryExtractNonLocalType("12123"));
    }
    
    @Test
    public void extractNonLocalType_6digit() {
        assertEquals("1", CommonCourtDivisionUtils.tryExtractNonLocalType("212123"));
    }
    
   
}