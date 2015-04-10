package pl.edu.icm.saos.webapp.analysis.request;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class DefaultJudgmentGlobalFilterFactoryTest {

    
    private DefaultJudgmentGlobalFilterFactory factory = new DefaultJudgmentGlobalFilterFactory();
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test
    public void createDefaultJudgmentGlobalFilter() {
        
        // execute
        
        JudgmentGlobalFilter globalFilter = factory.createDefaultJudgmentGlobalFilter();
        
        
        // assert
        
        MonthYearRange judgmentDateRange = globalFilter.getJudgmentDateRange();
        LocalDate today = new LocalDate();
        assertEquals(today.getYear()-20, judgmentDateRange.getStartYear());
        assertEquals(1, judgmentDateRange.getStartMonth());
        assertEquals(today.getYear(), judgmentDateRange.getEndYear());
        assertEquals(today.getMonthOfYear(), judgmentDateRange.getEndMonth());
        
    }

}
