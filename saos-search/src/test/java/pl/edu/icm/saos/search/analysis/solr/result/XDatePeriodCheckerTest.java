package pl.edu.icm.saos.search.analysis.solr.result;

import static org.junit.Assert.assertFalse;
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

public class XDatePeriodCheckerTest {

    
    private XDatePeriodChecker checker = new XDatePeriodChecker();
    
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void isDateFacet_NO_DATE_RANGE() {
        
        // given
        XSettings xsettings = new XSettings();
        xsettings.setRange(mock(XRange.class));
        
        // execute & assert
        assertFalse(checker.isDateFacet(xsettings, PeriodUnit.DAY));
        
    }
    
    
    @Test
    public void isDateFacet_DIFFERENT_PERIOD() {
        
        // given
        XSettings xsettings = new XSettings();
        XDateRange xrange = new XDateRange(new LocalDate(2012, 10, 1), new LocalDate(2013, 10, 2), new Period(1, PeriodUnit.DAY));
        xsettings.setRange(xrange);
        
        // execute & assert
        assertFalse(checker.isDateFacet(xsettings, PeriodUnit.MONTH));
        
    }
    
    @Test
    public void isDateFacet() {
        
        // given
        XSettings xsettings = new XSettings();
        XDateRange xrange = new XDateRange(new LocalDate(2012, 10, 1), new LocalDate(2013, 10, 2), new Period(1, PeriodUnit.DAY));
        xsettings.setRange(xrange);
        
        // execute & assert
        assertTrue(checker.isDateFacet(xsettings, PeriodUnit.DAY));
        
    }
    
}
