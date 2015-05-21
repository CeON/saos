package pl.edu.icm.saos.search.analysis.solr.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.util.FieldValuePrefixAdder;

/**
 * @author Łukasz Dumiszewski
 */

public class FacetPrefixedValueConverterTest {
    
    
    @InjectMocks
    private FacetPrefixedValueConverter converter = new FacetPrefixedValueConverter();
    
    @Mock
    private FieldValuePrefixAdder fieldValuePrefixAdder;
    
    
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    
    
    @Test(expected = NullPointerException.class)
    public void handles_Null() {
        
        // execute
        converter.handles(null);
        
    }
    
    
    @Test
    public void handles_FALSE() {
        
        // given
        XSettings xsettings = new XSettings();
        
        // execute & assert
        assertFalse(converter.handles(xsettings));
        
    }

    
    @Test
    public void handles_TRUE() {
        
        // given
        XSettings xsettings = new XSettings();
        xsettings.setFieldValuePrefix("PREFIX");
        
        // execute & assert
        assertTrue(converter.handles(xsettings));

    }
    
   
    @Test(expected = NullPointerException.class)
    public void convert_NullValue() {
        
        // execute
        converter.convert(null, mock(XSettings.class));
        
    }
    
    
        
    @Test
    public void convert() {
        
        // given
        String value = "PREFIX_VALUE";
        when(fieldValuePrefixAdder.extractFieldValue(value)).thenReturn("VALUE");
        
        // execute
        assertEquals("VALUE", converter.convert(value, null));
        
    }

}
  