package pl.edu.icm.saos.webapp.analysis.request.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;

/**
 * @author Łukasz Dumiszewski
 */

public class MonthYearRangeGapCalculatorTest {

    private MonthYearRangeGapCalculator calculator = new MonthYearRangeGapCalculator();
    
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void calculateGap_DAY() {
        
        // execute & assert
        assertEquals(new Period(1, PeriodUnit.DAY), calculator.calculateGap(2014, 1, 2014, 2));
        
    }

    
    @Test
    public void calculateGap_WEEK() {
        
        // execute & assert
        assertEquals(new Period(7, PeriodUnit.DAY), calculator.calculateGap(2014, 1, 2014, 3));
        assertEquals(new Period(7, PeriodUnit.DAY), calculator.calculateGap(2014, 1, 2014, 12));
        
    }
    
    @Test
    public void calculateGap_MONTH() {
        
        // execute & assert
        assertEquals(new Period(1, PeriodUnit.MONTH), calculator.calculateGap(2014, 2, 2015, 3));
        assertEquals(new Period(1, PeriodUnit.MONTH), calculator.calculateGap(2004, 2, 2014, 1));
        
    }
    
    @Test
    public void calculateGap_YEAR() {
        
        // execute & assert
        assertEquals(new Period(1, PeriodUnit.YEAR), calculator.calculateGap(2004, 2, 2014, 3));
        
    }

}
