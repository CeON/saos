package pl.edu.icm.saos.search.analysis.solr.result;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.XSettings;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class FacetValueConverterManagerTest {



    
    private FacetValueConverterManager manager = new FacetValueConverterManager();
 
    private FacetValueConverter facetValueConverter1 = mock(FacetValueConverter.class);
    private FacetValueConverter facetValueConverter2 = mock(FacetValueConverter.class);
    
    
    
    @Before
    public void before() {
    
        manager.setConverters(Lists.newArrayList(facetValueConverter1, facetValueConverter2));
           
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void getConverter_NULL_X_SETTINGS() {
        
        // execute
        manager.getConverter(null);
        
    }
        
    
    
    @Test(expected = IllegalArgumentException.class)
    public void getConverter_NO_HANDLING_CONVERTER_FOUND() {
        
        // given
        XSettings xsettings1 = mock(XSettings.class);
        
        
        when(facetValueConverter1.handles(xsettings1)).thenReturn(false);
        when(facetValueConverter2.handles(xsettings1)).thenReturn(false);
        
        // execute & assert
        manager.getConverter(xsettings1);
        
    }
    
    
    
    
    @Test
    public void getConverter() {
        
        // given
        XSettings xsettings1 = mock(XSettings.class);
        XSettings xsettings2 = mock(XSettings.class);
        
        when(facetValueConverter1.handles(xsettings1)).thenReturn(true);
        when(facetValueConverter1.handles(xsettings2)).thenReturn(false);
        when(facetValueConverter2.handles(xsettings1)).thenReturn(false);
        when(facetValueConverter2.handles(xsettings2)).thenReturn(true);
        
        // execute & assert
        assertTrue(facetValueConverter1 == manager.getConverter(xsettings1));
        assertFalse(facetValueConverter1 == manager.getConverter(xsettings2));
        assertTrue(facetValueConverter2 == manager.getConverter(xsettings2));
        assertFalse(facetValueConverter2 == manager.getConverter(xsettings1));
        
    }
    
    
   
    
    
}
