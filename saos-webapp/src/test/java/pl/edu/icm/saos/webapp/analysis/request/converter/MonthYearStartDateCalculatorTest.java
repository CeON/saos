
package pl.edu.icm.saos.webapp.analysis.request.converter;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class MonthYearStartDateCalculatorTest {

    
    private MonthYearStartDateCalculator calculator = new MonthYearStartDateCalculator();
    
    
    
    //------------------------ TESTS --------------------------
    
    
       
    
    @Test
    public void calculateStartDate() {
        
        
        // execute
        LocalDate calculatedDate = calculator.calculateStartDate(2011, 11);
        
        // assert
        assertEquals(new LocalDate(2011, 11, 1), calculatedDate);
    }
    
}
