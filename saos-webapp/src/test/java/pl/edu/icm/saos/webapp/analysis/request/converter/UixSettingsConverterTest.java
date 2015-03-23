package pl.edu.icm.saos.webapp.analysis.request.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XRange;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.webapp.analysis.request.UixRange;
import pl.edu.icm.saos.webapp.analysis.request.UixSettings;

/**
 * @author Łukasz Dumiszewski
 */

public class UixSettingsConverterTest {

    
    @InjectMocks
    private UixSettingsConverter uixSettingsConverter = new UixSettingsConverter();
    
    @Mock
    private UixRangeConverterManager uixRangeConverterManager;
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void convert_NULL() {
        
        // execute
        uixSettingsConverter.convert(null);
        
    }

    
    @Test
    public void convert() {
        
        // given
        
        UixSettings uixSettings = new UixSettings();
        uixSettings.setField(XField.JUDGMENT_DATE);
        UixRange uixRange = mock(UixRange.class);
        uixSettings.setRange(uixRange);
        
        XRange xrange = mock(XRange.class);
        
        when(uixRangeConverterManager.convertUixRange(uixRange)).thenReturn(xrange);
        
        // execute
        
        XSettings xsettings = uixSettingsConverter.convert(uixSettings);
        
        
        // assert
        
        assertEquals(uixSettings.getField(), xsettings.getField());
        assertEquals(xrange, xsettings.getRange());
        
    }

}
