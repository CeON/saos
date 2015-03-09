package pl.edu.icm.saos.webapp.analysis.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class PointValueFormatterManagerTest {

    private PointValueFormatterManager pointValueFormatterManager = new PointValueFormatterManager();
    
    private PointValueFormatter pointValueFormatter1 = mock(PointValueFormatter.class);
    private PointValueFormatter pointValueFormatter2 = mock(PointValueFormatter.class);
    private PointValueFormatter pointValueFormatter3 = mock(PointValueFormatter.class);
    
    
    @Before
    public void before() {
        
        when(pointValueFormatter1.getOrder()).thenReturn(100);
        when(pointValueFormatter2.getOrder()).thenReturn(2);
        when(pointValueFormatter3.getOrder()).thenReturn(3);
        
        when(pointValueFormatter1.handles(String.class)).thenReturn(true);
        when(pointValueFormatter1.handles(Date.class)).thenReturn(true);
        when(pointValueFormatter2.handles(Integer.class)).thenReturn(true);
        when(pointValueFormatter3.handles(String.class)).thenReturn(true);
    
        pointValueFormatterManager.setPointValueFormatters(Lists.newArrayList(pointValueFormatter1, pointValueFormatter2, pointValueFormatter3));
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void getFormatter_Null() {
        
        // execute
        pointValueFormatterManager.getFormatter(null);
        
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void getFormatter_NoFormattedFound() {
        
        // execute
        pointValueFormatterManager.getFormatter(Double.class);
        
    }
    
    
    @Test
    public void getFormatter_String() {
        
        // execute & assert
        assertTrue(pointValueFormatter3 == pointValueFormatterManager.getFormatter(String.class));
        verify(pointValueFormatter2).handles(String.class);
        verify(pointValueFormatter2, never()).format(Mockito.any());
        verify(pointValueFormatter1, never()).handles(Mockito.any());
        verify(pointValueFormatter1, never()).format(Mockito.any());
     
    }
    
    @Test
    public void getFormatter_Integer() {
    
        // execute & assert
        assertTrue(pointValueFormatter2 == pointValueFormatterManager.getFormatter(Integer.class));
        verify(pointValueFormatter3, never()).handles(Mockito.any());
        verify(pointValueFormatter3, never()).format(Mockito.any());
        verify(pointValueFormatter1, never()).handles(Mockito.any());
        verify(pointValueFormatter1, never()).format(Mockito.any());
        
    }
        
    @Test
    public void getFormatter_Date() {

        // execute & assert
        assertTrue(pointValueFormatter1 == pointValueFormatterManager.getFormatter(Date.class));
        verify(pointValueFormatter3, times(1)).handles(Date.class);
        verify(pointValueFormatter3, never()).format(Mockito.any());
        verify(pointValueFormatter2, times(1)).handles(Date.class);
        verify(pointValueFormatter2, never()).format(Mockito.any());

    }
    
    
    @Test
    public void format_Null() {
        
        // execute & assert
        assertNull(pointValueFormatterManager.format(null));
        
    }
    
    
    @Test
    public void format() {
        
        // given
        when(pointValueFormatter3.format("ABC")).thenReturn("abc");
        
        // execute
        String formattedValue = pointValueFormatterManager.format("ABC");
        
        // assert
        assertEquals("abc", formattedValue);
        verify(pointValueFormatter3).handles(String.class);
        verify(pointValueFormatter2).handles(String.class);
        verify(pointValueFormatter2, Mockito.never()).format(Mockito.any());
        verify(pointValueFormatter1, Mockito.never()).handles(Mockito.any());
        verify(pointValueFormatter1, Mockito.never()).format(Mockito.any());
    }
    
    
}
