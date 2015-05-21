package pl.edu.icm.saos.search.analysis.solr.result;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.edu.icm.saos.common.chart.value.CcCourtArea;
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.util.CcCourtAreaFieldValueConverter;

/**
 * @author Łukasz Dumiszewski
 */

public class FacetCcCourtAreaValueConverterTest {
    
    @InjectMocks
    private FacetCcCourtAreaValueConverter converter = new FacetCcCourtAreaValueConverter();
    
    @Mock
    private CcCourtAreaFieldValueConverter ccCourtAreaFieldValueConverter;
    
    
    
    
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
        xsettings.setField(XField.JUDGMENT_DATE);
        
        // execute & assert
        assertFalse(converter.handles(xsettings));
        
    }

    
    @Test
    public void handles_TRUE_APPEAL() {
        
        // given
        XSettings xsettings = new XSettings();
        xsettings.setField(XField.CC_APPEAL);
        
        // execute & assert
        assertTrue(converter.handles(xsettings));

        
    }
    
    
    @Test
    public void handles_TRUE_REGION() {
        
        // given
        XSettings xsettings = new XSettings();
        xsettings.setField(XField.CC_REGION);
        
        // execute & assert
        assertTrue(converter.handles(xsettings));

        
    }
    
    
    @Test
    public void handles_TRUE_DISTRICT() {
        
        // given
        XSettings xsettings = new XSettings();
        xsettings.setField(XField.CC_DISTRICT);
        
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
        String value = "123_123#Appeal Court in Chicago";
        CcCourtArea ccCourtArea = new CcCourtArea();
        when(ccCourtAreaFieldValueConverter.convert(value)).thenReturn(ccCourtArea);
        
        // execute
        assertTrue(ccCourtArea == converter.convert(value, null));
        
    }

  
}
