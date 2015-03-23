package pl.edu.icm.saos.search.analysis.solr.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.common.chart.value.Week;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.analysis.request.XSettings;

/**
 * @author Łukasz Dumiszewski
 */

public class FacetWeekValueConverterTest {
    
    
    private FacetWeekValueConverter converter = new FacetWeekValueConverter();
    
    private XDatePeriodChecker xdatePeriodChecker = mock(XDatePeriodChecker.class);
    
    private XSettings xsettings = mock(XSettings.class);
    
    
    @Before
    public void before() {
        
        converter.setXdatePeriodChecker(xdatePeriodChecker);
        
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
        when(xdatePeriodChecker.isDateFacet(xsettings, PeriodUnit.WEEK)).thenReturn(false);
        
        // execute & assert
        assertFalse(converter.handles(xsettings));
        
    }

    
    @Test
    public void handles_TRUE() {
        
        // given
        when(xdatePeriodChecker.isDateFacet(xsettings, PeriodUnit.WEEK)).thenReturn(true);
        
        // execute & assert
        assertTrue(converter.handles(xsettings));
        
    }

    
    @Test(expected = NullPointerException.class)
    public void convert_Null() {
        
        // execute
        converter.convert(null);
        
    }

    
    @Test
    public void convert() {
        
        // given
        String value = "2010-12-07T00:00:00Z";
        
        // execute
        assertEquals(new Week(new LocalDate(2010, 12, 7), new LocalDate(2010, 12, 13)), converter.convert(value));
        
    }

    
}