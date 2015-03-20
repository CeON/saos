package pl.edu.icm.saos.webapp.analysis.request.converter;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

/**
 * 
 * Calculator of start date
 * 
 * @author Łukasz Dumiszewski
 */
@Service("monthYearStartDateCalculator")
class MonthYearStartDateCalculator {

    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Calculates start date for the given year and month (the first day of the month) 
     */
    public LocalDate calculateStartDate(int startYear, int startMonth) {
        
        return new LocalDate(startYear, startMonth, 1);
        
    }
}
