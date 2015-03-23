package pl.edu.icm.saos.webapp.analysis.request.converter;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class MonthYearEndDateCalculatorTest {

    
    private MonthYearEndDateCalculator calculator = new MonthYearEndDateCalculator();
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test
    public void calculateEndDate_TODAY() {
        
        // given
        LocalDate today = new LocalDate();
        
        // execute
        LocalDate calculatedDate = calculator.calculateEndDate(today.getYear(), today.getMonthOfYear());
        
        // assert
        assertEquals(today, calculatedDate);
    }
    
    
    @Test
    public void calculateEndDate_NOT_TODAY() {
        
        
        // execute
        LocalDate calculatedDate = calculator.calculateEndDate(2011, 11);
        
        // assert
        assertEquals(new LocalDate(2011, 11, 30), calculatedDate);
    }
    
}
