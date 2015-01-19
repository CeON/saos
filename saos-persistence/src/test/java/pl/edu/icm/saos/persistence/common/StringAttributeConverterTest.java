package pl.edu.icm.saos.persistence.common;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * @author madryk
 */
@RunWith(DataProviderRunner.class)
public class StringAttributeConverterTest {

    private StringAttributeConverter stringAttributeConverter = new StringAttributeConverter();
    
    @DataProvider
    public static Object[][] convertToDatabaseColumnData() {
        return new Object[][] {
                { null, null },
                { "", "" },
                { "abc2", "abc2" },
                { "\u017b", "\u017b" },
                { "\u0000", "" },
                { "ab\u0000c", "abc" },
        };
    }
    
    @DataProvider
    public static Object[][] convertToEntityAttributeData() {
        return new Object[][] {
                { null, null },
                { "", "" },
                { "abc2", "abc2" },
                { "\u017b", "\u017b" },
        };
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    @UseDataProvider("convertToDatabaseColumnData")
    public void convertToDatabaseColumn(String given, String expected) {
        
        // execute
        String actualString = stringAttributeConverter.convertToDatabaseColumn(given);
        
        // assert
        assertEquals(expected, actualString);
    }
    
    @Test
    @UseDataProvider("convertToEntityAttributeData")
    public void convertToEntityAttribute(String given, String expected) {
        
        // execute
        String actual = stringAttributeConverter.convertToEntityAttribute(given);
        
        // assert
        assertEquals(expected, actual);
    }
    
}
