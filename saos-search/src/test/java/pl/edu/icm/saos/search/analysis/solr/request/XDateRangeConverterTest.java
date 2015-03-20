package pl.edu.icm.saos.search.analysis.solr.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.search.analysis.request.XRange;
import pl.edu.icm.saos.search.analysis.solr.request.XDateRangeConverter;

/**
 * @author madryk
 */
public class XDateRangeConverterTest {

    private XDateRangeConverter xDateRangeConverter = new XDateRangeConverter();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void isApplicable() {
        // execute & assert
        assertTrue(xDateRangeConverter.isApplicable(XDateRange.class));
        assertFalse(xDateRangeConverter.isApplicable(XRange.class));
    }
    
    @Test(expected = NullPointerException.class)
    public void isApplicable_NULL_CLASS_ARGUMENT() {
        // execute
        xDateRangeConverter.isApplicable(null);
    }
    
    
    @Test
    public void convertStart() {
        // given
        XDateRange xDateRange = new XDateRange(new LocalDate(2012, 5, 7), new LocalDate(), new Period(1, PeriodUnit.MONTH));
        
        // execute
        String startParam = xDateRangeConverter.convertStart(xDateRange);
        
        // assert
        assertEquals("2012-05-07T00:00:00Z", startParam);
    }
    
    @Test(expected = NullPointerException.class)
    public void convertStart_NULL_XRANGE() {
        // execute
        xDateRangeConverter.convertStart(null);
    }
    
    
    @Test
    public void convertEnd() {
        // given
        XDateRange xDateRange = new XDateRange(new LocalDate(), new LocalDate(2012, 5, 7), new Period(1, PeriodUnit.MONTH));
        
        // execute
        String endParam = xDateRangeConverter.convertEnd(xDateRange);
        
        // assert
        assertEquals("2012-05-07T23:59:59Z", endParam);
    }
    
    @Test(expected = NullPointerException.class)
    public void convertEnd_NULL_XRANGE() {
        // execute
        xDateRangeConverter.convertEnd(null);
    }
    
    
    @Test
    public void convertGap_DAY() {
        // given
        XDateRange xDateRange = new XDateRange(new LocalDate(), new LocalDate(), new Period(2, PeriodUnit.DAY));
        
        // execute
        String gapParam = xDateRangeConverter.convertGap(xDateRange);
        
        // assert
        assertEquals("+2DAYS", gapParam);
    }
    
    
    @Test
    public void convertGap_WEEK() {
        // given
        XDateRange xDateRange = new XDateRange(new LocalDate(), new LocalDate(), new Period(2, PeriodUnit.WEEK));
        
        // execute
        String gapParam = xDateRangeConverter.convertGap(xDateRange);
        
        // assert
        assertEquals("+14DAYS", gapParam);
    }
    
    @Test
    public void convertGap_MONTH() {
        // given
        XDateRange xDateRange = new XDateRange(new LocalDate(), new LocalDate(), new Period(4, PeriodUnit.MONTH));
        
        // execute
        String gapParam = xDateRangeConverter.convertGap(xDateRange);
        
        // assert
        assertEquals("+4MONTHS", gapParam);
    }
    
    @Test
    public void convertGap_YEAR() {
        // given
        XDateRange xDateRange = new XDateRange(new LocalDate(), new LocalDate(), new Period(1, PeriodUnit.YEAR));
        
        // execute
        String gapParam = xDateRangeConverter.convertGap(xDateRange);
        
        // assert
        assertEquals("+1YEARS", gapParam);
    }
    
    @Test(expected = NullPointerException.class)
    public void convertGap_NULL_XRANGE() {
        // execute
        xDateRangeConverter.convertGap(null);
    }
}
