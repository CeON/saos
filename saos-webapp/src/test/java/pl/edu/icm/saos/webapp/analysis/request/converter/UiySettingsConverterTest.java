package pl.edu.icm.saos.webapp.analysis.request.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.AbsoluteNumberYValue;
import pl.edu.icm.saos.search.analysis.request.RateYValue;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.webapp.analysis.request.UiySettings;
import pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType;

/**
 * @author Łukasz Dumiszewski
 */

public class UiySettingsConverterTest {

    
    private UiySettingsConverter converter = new UiySettingsConverter();
    
    private UiySettings uiySettings = new UiySettings();
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void convert_NULL() {
        
        // execute
        converter.convert(null);
    }
    
    
    @Test
    public void convert_NUMBER() {
        
        // given
        uiySettings.setValueType(UiyValueType.ABSOLUTE_NUMBER);
        
        // execute
        YSettings ysettings = converter.convert(uiySettings);
        
        // assert
        assertNotNull(ysettings.getValueType());
        assertTrue(ysettings.getValueType() instanceof AbsoluteNumberYValue);
    }
    
    
    @Test
    public void convert_PERCENT() {
        
        // given
        uiySettings.setValueType(UiyValueType.PERCENT);
        
        // execute
        YSettings ysettings = converter.convert(uiySettings);
        
        // assert
        assertNotNull(ysettings.getValueType());
        assertTrue(ysettings.getValueType() instanceof RateYValue);
        assertEquals(100, ((RateYValue)ysettings.getValueType()).getRateRatio());
    }


    @Test
    public void convert_NUMBER_PER_1000() {
        
        // given
        uiySettings.setValueType(UiyValueType.NUMBER_PER_1000);
        
        // execute
        YSettings ysettings = converter.convert(uiySettings);
        
        // assert
        assertNotNull(ysettings.getValueType());
        assertTrue(ysettings.getValueType() instanceof RateYValue);
        assertEquals(1000, ((RateYValue)ysettings.getValueType()).getRateRatio());
    }

}
