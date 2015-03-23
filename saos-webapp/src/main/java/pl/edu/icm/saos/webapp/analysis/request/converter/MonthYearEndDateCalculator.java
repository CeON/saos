package pl.edu.icm.saos.webapp.analysis.request.converter;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

/**
 * Calculator of end date
 * 
 * @author Łukasz Dumiszewski
 */
@Service("monthYearEndDateCalculator")
class MonthYearEndDateCalculator {

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Calculates end date for the given year and month. It will be the last date of the month or 
     * the current day if endYear and endMonth are current year and month. 
     */
    public LocalDate calculateEndDate(int endYear, int endMonth) {
        
        LocalDate today = new LocalDate();
        
        if (today.getMonthOfYear() == endMonth && today.getYear() == endYear) {
            
            return today;
        }
        
        return new LocalDate(endYear, endMonth, 1).dayOfMonth().withMaximumValue();
        
        
    }

}
