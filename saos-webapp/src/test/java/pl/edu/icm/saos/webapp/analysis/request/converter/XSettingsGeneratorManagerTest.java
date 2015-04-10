package pl.edu.icm.saos.webapp.analysis.request.converter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.webapp.analysis.result.ChartCode;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class XSettingsGeneratorManagerTest {

    
    private XSettingsGeneratorManager xsettingsGeneratorManager = new XSettingsGeneratorManager();
    
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void getXSettingsGenerator_NULL() {
        
        // execute
        xsettingsGeneratorManager.getXSettingsGenerator(null);
        
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void getXSettingsGenerator_NoGeneratorsSet() {
        
        // execute
        xsettingsGeneratorManager.getXSettingsGenerator(ChartCode.MAIN_CHART);
        
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void getXSettingsGenerator_NoProperGeneratorFound() {
        
        // given
        
        XSettingsGenerator xsettingsGenerator = mock(XSettingsGenerator.class);
        when(xsettingsGenerator.handles(Mockito.any(ChartCode.class))).thenReturn(false);
        xsettingsGeneratorManager.setXsettingsGenerators(Lists.newArrayList(xsettingsGenerator));
        
        
        // execute
        
        xsettingsGeneratorManager.getXSettingsGenerator(ChartCode.MAIN_CHART);
        
    }
    
    @Test
    public void getXSettingsGenerator() {
        
        // given
        
        XSettingsGenerator xsettingsGenerator1 = mock(XSettingsGenerator.class);
        when(xsettingsGenerator1.handles(Mockito.any(ChartCode.class))).thenReturn(false);
        
        XSettingsGenerator xsettingsGenerator2 = mock(XSettingsGenerator.class);
        when(xsettingsGenerator2.handles(ChartCode.MAIN_CHART)).thenReturn(true);
        
        xsettingsGeneratorManager.setXsettingsGenerators(Lists.newArrayList(xsettingsGenerator1, xsettingsGenerator2));
        
        
        // execute & assert
        
        assertTrue(xsettingsGenerator2 == xsettingsGeneratorManager.getXSettingsGenerator(ChartCode.MAIN_CHART));
        
    }
    
}
