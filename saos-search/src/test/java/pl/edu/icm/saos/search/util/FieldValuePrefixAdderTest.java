package pl.edu.icm.saos.search.util;

import static org.junit.Assert.assertEquals;
import static pl.edu.icm.saos.search.config.model.IndexFieldsConstants.FIELD_VALUE_PREFIX_SEPARATOR;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class FieldValuePrefixAdderTest {

    
    private FieldValuePrefixAdder fieldValuePrefixAdder = new FieldValuePrefixAdder();
    
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected = NullPointerException.class)
    public void prefixWithSeparator_NULL() {
        
        // execute
        fieldValuePrefixAdder.prefixWithSeparator(null);
        
    }

    
    @Test
    public void prefixWithSeparator() {
        
        // execute & assert
        assertEquals("ALA_MA_KOTA"+FIELD_VALUE_PREFIX_SEPARATOR, fieldValuePrefixAdder.prefixWithSeparator("ALA_MA_KOTA"));
        
    }
    
    
    @Test(expected = NullPointerException.class)
    public void addFieldPrefix_valueNull_prefixNotNull() {
        
        // execute
        fieldValuePrefixAdder.addFieldPrefix(null, "prefix");
        
    }
    
    
    @Test(expected = NullPointerException.class)
    public void addFieldPrefix_valueNotNull_prefixNull() {
        
        // execute
        fieldValuePrefixAdder.addFieldPrefix("value", null);
        
    }
    
    
    @Test
    public void addFieldPrefix() {
        
        // execute & assert
        assertEquals("prefix"+FIELD_VALUE_PREFIX_SEPARATOR+"value", fieldValuePrefixAdder.addFieldPrefix("prefix","value"));
        
    }
    
    
    @Test(expected = NullPointerException.class)
    public void extractFieldValue_Null() {
        
        // execute
        fieldValuePrefixAdder.extractFieldValue(null);
        
    }
    
    
    @Test
    public void extractFieldValue() {
        
        // execute & assert
        assertEquals("value", fieldValuePrefixAdder.extractFieldValue("prefix"+FIELD_VALUE_PREFIX_SEPARATOR+"value"));
        
    }
    
}
