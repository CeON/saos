package pl.edu.icm.saos.search.analysis.solr.request;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.XDateRange;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class XRangeConverterManagerTest {

    private XRangeConverterManager xRangeConverterManager = new XRangeConverterManager();
    
    
    private XRangeConverter xRangeConverter1 = mock(XRangeConverter.class);
    
    private XRangeConverter xRangeConverter2 = mock(XRangeConverter.class);
    
    
    @Before
    public void setUp() {
        xRangeConverterManager.setxRangeConverters(Lists.newArrayList(xRangeConverter1, xRangeConverter2));
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void getXRangeConverter_FOUND() {
        // given
        when(xRangeConverter1.isApplicable(XDateRange.class)).thenReturn(false);
        when(xRangeConverter2.isApplicable(XDateRange.class)).thenReturn(true);
        
        // execute
        XRangeConverter retXRangeConverter = xRangeConverterManager.getXRangeConverter(XDateRange.class);
        
        // assert
        assertTrue(retXRangeConverter == xRangeConverter2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getXRangeConverter_NOT_FOUND() {
        // given
        when(xRangeConverter1.isApplicable(any())).thenReturn(false);
        when(xRangeConverter2.isApplicable(any())).thenReturn(false);
        
        // execute
        xRangeConverterManager.getXRangeConverter(XDateRange.class);
    }
    
    @Test(expected = NullPointerException.class)
    public void getXRangeConverter_NULL_CLASS_ARGUMENT() {
        // execute
        xRangeConverterManager.getXRangeConverter(null);
    }
    
}
