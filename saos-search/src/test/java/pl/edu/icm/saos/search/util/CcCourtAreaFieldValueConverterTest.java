package pl.edu.icm.saos.search.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.common.chart.value.CcCourtArea;

/**
 * @author Łukasz Dumiszewski
 */

public class CcCourtAreaFieldValueConverterTest {

    
    @InjectMocks
    private CcCourtAreaFieldValueConverter ccCourtAreaFieldValueConverter;
    
    @Mock
    private FieldValuePrefixAdder fieldValuePrefixAdder;
    
    
    
    @Before
    public void before() {
        
        MockitoAnnotations.initMocks(this);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected=NullPointerException.class)
    public void convert_NULL() {
        
        // execute
        ccCourtAreaFieldValueConverter.convert(null);
        
    }
    
    

    @Test
    public void convert() {
        
        // given
        String sep = CcCourtAreaFieldValueCreator.CC_COURT_AREA_VALUE_PART_SEPARATOR;
        String courtAreaFieldValue = "123_Sąd Okręgowy w Poznaniu"+sep+"124";
        when(fieldValuePrefixAdder.extractFieldValue(courtAreaFieldValue)).thenReturn("Sąd Okręgowy w Poznaniu"+sep+"124");
        
        // execute
        CcCourtArea ccCourtArea = ccCourtAreaFieldValueConverter.convert(courtAreaFieldValue);
        
        // assert
        assertNotNull(ccCourtArea);
        assertEquals(124L, ccCourtArea.getCourtId());
        assertEquals("Sąd Okręgowy w Poznaniu", ccCourtArea.getName());
    }
    
    
    @Test(expected=Exception.class)
    public void convert_IncorrectFieldValue() {
        
        // given
        String courtAreaFieldValue = "123_124Sąd Okręgowy w Poznaniu";
        when(fieldValuePrefixAdder.extractFieldValue(courtAreaFieldValue)).thenReturn("124Sąd Okręgowy w Poznaniu");
        
        // execute
        ccCourtAreaFieldValueConverter.convert(courtAreaFieldValue);
        
    }
    
}
