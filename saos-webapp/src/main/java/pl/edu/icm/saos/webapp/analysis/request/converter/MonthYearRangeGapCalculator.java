package pl.edu.icm.saos.webapp.analysis.request.converter;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;


/**
 * Calculator of month-year range gap
 * 
 * @author Łukasz Dumiszewski
 */
@Service("monthYearRangeGapCalculator")
class MonthYearRangeGapCalculator {

    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Calculates the default gap for the given range. 
     */
    public Period calculateGap(int startYear, int startMonth, int endYear, int endMonth) {
        
        
        int monthDiff = (endYear - startYear) * 12 + (endMonth - startMonth + 1);
        
        
        if (monthDiff <= 2) {
            
            return new Period(1, PeriodUnit.DAY);
            
        }
        
        
        if (monthDiff <= 12) {
            
            return new Period(1, PeriodUnit.WEEK);
            
        }
        
        
        if (monthDiff <= 120) {
            
            return new Period(1, PeriodUnit.MONTH);
            
        }
        
        
        return new Period(1, PeriodUnit.YEAR);
        
    }
    
}
