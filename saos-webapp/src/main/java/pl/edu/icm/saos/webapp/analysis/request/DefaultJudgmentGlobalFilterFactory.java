package pl.edu.icm.saos.webapp.analysis.request;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

/**
 * A creator of the default judgment global filter 
 * 
 * @author Łukasz Dumiszewski
 */
@Service("defaultJudgmentGlobalFilterFactory")
public class DefaultJudgmentGlobalFilterFactory {

    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Creates a default {@link JudgmentGlobalFilter} for UI
     */
    public JudgmentGlobalFilter createDefaultJudgmentGlobalFilter() {
         
        JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
        
        MonthYearRange judgmentDateRange = new MonthYearRange();

        LocalDate today = new LocalDate();
        judgmentDateRange.setStartYear(today.getYear() - 20);
        judgmentDateRange.setStartMonth(1);
        judgmentDateRange.setEndYear(today.getYear());
        judgmentDateRange.setEndMonth(today.getMonthOfYear());
        
        globalFilter.setJudgmentDateRange(judgmentDateRange);
        
        return globalFilter;
    }

}
