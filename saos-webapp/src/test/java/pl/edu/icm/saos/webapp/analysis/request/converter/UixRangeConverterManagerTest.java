package pl.edu.icm.saos.webapp.analysis.request.converter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.search.analysis.request.XRange;
import pl.edu.icm.saos.webapp.analysis.request.UixRange;
import pl.edu.icm.saos.webapp.analysis.request.converter.UixRangeConverter;
import pl.edu.icm.saos.webapp.analysis.request.converter.UixRangeConverterManager;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class UixRangeConverterManagerTest {

    private UixRangeConverterManager manager = new UixRangeConverterManager();
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void convertUixRange_NULL() {
        
        // execute
        manager.convertUixRange(null);
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void convertUixRange_NO_HANDLING_CONVERTER() {
        
        // given
        
        UixRangeConverter converter1 = mock(UixRangeConverter.class);
        when(converter1.handles(Mockito.any())).thenReturn(false);
        
        manager.setUixRangeConverters(Lists.newArrayList(converter1));
        
        
        // execute
        
        manager.convertUixRange(mock(UixRange.class));
        
    }

   
    @Test
    public void convertUixRange() {
        
        // given
        UixRange uixRange = mock(UixRange.class);
        XRange xRange = mock(XRange.class);
        
        UixRangeConverter converter1 = mock(UixRangeConverter.class);
        when(converter1.handles(uixRange.getClass())).thenReturn(false);
        
        UixRangeConverter converter2 = mock(UixRangeConverter.class);
        when(converter2.handles(uixRange.getClass())).thenReturn(true);
        when(converter2.convert(uixRange)).thenReturn(xRange);
        
        
        manager.setUixRangeConverters(Lists.newArrayList(converter1, converter2));
        
        
        // execute
        
        XRange convertedXrange = manager.convertUixRange(uixRange);
        
        // assert
        
        assertTrue(xRange == convertedXrange);
        verify(converter1).handles(uixRange.getClass());
        verifyNoMoreInteractions(converter1);
        verify(converter2).handles(uixRange.getClass());
        verify(converter2).convert(uixRange);
    }

}
