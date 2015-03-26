package pl.edu.icm.saos.search.analysis.solr.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.search.analysis.request.XRange;
import pl.edu.icm.saos.search.analysis.request.XSettings;

/**
 * @author Łukasz Dumiszewski
 */

public class XDateRangeUtilsTest {

    
    private XDateRangeUtils utils = new XDateRangeUtils();
    
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void getDateRange_NO_DATE_RANGE() {
        
        // given
        XSettings xsettings = new XSettings();
        xsettings.setRange(mock(XRange.class));
        
        // execute & assert
        assertNull(utils.getDateRange(xsettings));
        
    }
    
    
    @Test
    public void getDateRange() {
        
        // given
        XSettings xsettings = new XSettings();
        XDateRange xrange = new XDateRange(new LocalDate(2012, 10, 1), new LocalDate(2013, 10, 2), new Period(1, PeriodUnit.DAY));
        xsettings.setRange(xrange);
        
        // execute 
        XDateRange dateRange = utils.getDateRange(xsettings);
        
        // assert
        assertNotNull(dateRange);
        assertTrue(dateRange == xrange);
        
    }
    
    
    @Test
    public void generateEndDate_DAY() {
        
        // given
        LocalDate startDate = new LocalDate(2011, 11, 11);
        
        // execute 
        LocalDate endDate = utils.generateEndDate(startDate, new Period(1, PeriodUnit.DAY));
        
        // assert
        assertEquals(new LocalDate(2011, 11, 11), endDate);
        
    }
    
    
    @Test
    public void generateEndDate_MONTH() {
        
        // given
        LocalDate startDate = new LocalDate(2011, 11, 11);
        
        // execute 
        LocalDate endDate = utils.generateEndDate(startDate, new Period(1, PeriodUnit.MONTH));
        
        // assert
        assertEquals(new LocalDate(2011, 12, 10), endDate);
        
    }
   
    
    @Test
    public void generateEndDate_YEAR() {
        
        // given
        LocalDate startDate = new LocalDate(2011, 11, 11);
        
        // execute 
        LocalDate endDate = utils.generateEndDate(startDate, new Period(1, PeriodUnit.YEAR));
        
        // assert
        assertEquals(new LocalDate(2012, 11, 10), endDate);
        
    }
    
    
}
