package pl.edu.icm.saos.webapp.analysis.request;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.search.analysis.request.XField;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class UixRangeFactoryTest {

    
    private UixRangeFactory uixRangeFactory = new UixRangeFactory();
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected = NullPointerException.class)
    public void createUixRange_NULL_XFIELD() {
        
        // execute
        uixRangeFactory.createUixRange(null);
        
    }
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void createUixRange_NO_HANDLING_RANGE_CREATOR() {
        
        // given 
        UixRangeCreator rangeCreator1 = mock(UixRangeCreator.class);
        when(rangeCreator1.handles(Mockito.any())).thenReturn(false);
        
        uixRangeFactory.setUixRangeCreators(Lists.newArrayList(rangeCreator1));
        
        
        // execute
        uixRangeFactory.createUixRange(XField.JUDGMENT_DATE);
        
    }
    

    @Test
    public void createUixRange() {
        
        // given 
        
        UixRangeCreator rangeCreator1 = mock(UixRangeCreator.class);
        when(rangeCreator1.handles(Mockito.any())).thenReturn(false);
        UixRangeCreator rangeCreator2 = mock(UixRangeCreator.class);
        when(rangeCreator2.handles(XField.JUDGMENT_DATE)).thenReturn(true);
        
        uixRangeFactory.setUixRangeCreators(Lists.newArrayList(rangeCreator1, rangeCreator2));
        
        UixRange range = mock(UixRange.class);
        when(rangeCreator2.createRange()).thenReturn(range);
        
        
        // execute
        
        UixRange uixRange = uixRangeFactory.createUixRange(XField.JUDGMENT_DATE);
        
        
        // assert
        
        assertTrue(range == uixRange);
        verify(rangeCreator1).handles(XField.JUDGMENT_DATE);
        verify(rangeCreator2).handles(XField.JUDGMENT_DATE);
        verify(rangeCreator2).createRange();
        
    }
    
    

    
}
