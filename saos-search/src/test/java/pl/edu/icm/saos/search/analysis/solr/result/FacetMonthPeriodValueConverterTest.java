package pl.edu.icm.saos.search.analysis.solr.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.common.chart.value.MonthPeriod;
import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.search.analysis.request.XSettings;

/**
 * @author Łukasz Dumiszewski
 */

public class FacetMonthPeriodValueConverterTest {

    
    private FacetMonthPeriodValueConverter converter = new FacetMonthPeriodValueConverter();
    
    private XDateRangeUtils xDateRangeUtils = mock(XDateRangeUtils.class);
    
    private XSettings xsettings = mock(XSettings.class);
    
    
    @Before
    public void before() {
        
        converter.setXdateRangeUtils(xDateRangeUtils);
        
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected = NullPointerException.class)
    public void handles_Null() {
        
        // execute
        converter.handles(null);
        
    }
    
    
    @Test
    public void handles_FALSE_NULL_DATE_RANGE() {
        
        // given
        when(xDateRangeUtils.getDateRange(xsettings)).thenReturn(null);
        
        // execute & assert
        assertFalse(converter.handles(xsettings));
        
    }

    
    @Test
    public void handles_FALSE_DAY() {
        
        // given
        XDateRange dateRange = new XDateRange(new LocalDate(2011, 11, 1), new LocalDate(2011, 12, 12), new Period(3, PeriodUnit.DAY));
        when(xDateRangeUtils.getDateRange(xsettings)).thenReturn(dateRange);
        
        // execute & assert
        assertFalse(converter.handles(xsettings));
        
    }


    @Test
    public void handles_FALSE_MONTH() {
        
        // given
        XDateRange dateRange = new XDateRange(new LocalDate(2011, 11, 11), new LocalDate(2011, 12, 12), new Period(3, PeriodUnit.MONTH));
        when(xDateRangeUtils.getDateRange(xsettings)).thenReturn(dateRange);
        
        // execute & assert
        assertFalse(converter.handles(xsettings));
        
    }

    @Test
    public void handles_FALSE_YEAR() {
        
        // given
        XDateRange dateRange = new XDateRange(new LocalDate(2011, 11, 11), new LocalDate(2015, 12, 12), new Period(3, PeriodUnit.YEAR));
        when(xDateRangeUtils.getDateRange(xsettings)).thenReturn(dateRange);
        
        // execute & assert
        assertFalse(converter.handles(xsettings));
        
    }
    
    
    @Test
    public void handles_TRUE_MONTH() {
        
        // given
        XDateRange dateRange = new XDateRange(new LocalDate(2011, 11, 1), new LocalDate(2012, 12, 12), new Period(3, PeriodUnit.MONTH));
        when(xDateRangeUtils.getDateRange(xsettings)).thenReturn(dateRange);
        
        // execute & assert
        assertTrue(converter.handles(xsettings));
        
    }
    
    
    @Test
    public void handles_TRUE_YEAR() {
        
        // given
        XDateRange dateRange = new XDateRange(new LocalDate(2011, 11, 1), new LocalDate(2012, 12, 12), new Period(1, PeriodUnit.YEAR));
        when(xDateRangeUtils.getDateRange(xsettings)).thenReturn(dateRange);
        
        // execute & assert
        assertTrue(converter.handles(xsettings));
        
    }

    
    @Test(expected = NullPointerException.class)
    public void convert_NullValue() {
        
        // execute
        converter.convert(null, mock(XSettings.class));
        
    }
    
    
    @Test(expected = NullPointerException.class)
    public void convert_XSettings() {
        
        // execute
        converter.convert("XYZ", null);
        
    }


    
    @Test
    public void convert() {
        
        // given
        String value = "2010-10-01T00:00:00Z";
        
        XDateRange range = mock(XDateRange.class);
        Period period = new Period(1, PeriodUnit.MONTH);
        when(range.getGap()).thenReturn(period);
        
        
        LocalDate startDate = new LocalDate(2010, 10, 1);
        LocalDate endDate = new LocalDate(2010, 10, 30);
        
        when(xDateRangeUtils.getDateRange(xsettings)).thenReturn(range);
        when(xDateRangeUtils.generateEndDate(startDate, period)).thenReturn(endDate);
        
        // execute
        assertEquals(new MonthPeriod(2010, 10, 2010, 10), converter.convert(value, xsettings));
        
    }

  

    
}