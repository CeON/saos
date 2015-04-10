package pl.edu.icm.saos.webapp.analysis.request.converter;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.webapp.analysis.request.MonthYearRange;

import com.google.common.base.Preconditions;

/**
 * A {@link MonthYearRange} converter
 *  
 * @author Łukasz Dumiszewski
 */
@Service("monthYearRangeConverter")
public class MonthYearRangeConverter {

    
    private MonthYearStartDateCalculator monthYearStartDateCalculator;
    
    private MonthYearEndDateCalculator monthYearEndDateCalculator;
    
    private MonthYearRangeGapCalculator monthYearRangeGapCalculator;
    
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Converts the given {@link MonthYearRange} into {@linkk XDateRange}
     *
     * @throws NullPointerException if the passed monthYearRange is null
     * @throws IllegalArgumentException if the passed monthYearRange has end period before start period
     */
    public XDateRange convert(MonthYearRange monthYearRange) {
        
        Preconditions.checkNotNull(monthYearRange);
        
        Preconditions.checkArgument((monthYearRange.getEndYear() - monthYearRange.getStartYear()) * 12 + monthYearRange.getEndMonth() - monthYearRange.getStartMonth() >= 0);
        
        
        LocalDate startDate = monthYearStartDateCalculator.calculateStartDate(monthYearRange.getStartYear(), monthYearRange.getStartMonth());
        
        LocalDate endDate = monthYearEndDateCalculator.calculateEndDate(monthYearRange.getEndYear(), monthYearRange.getEndMonth());
        
        Period gap = monthYearRangeGapCalculator.calculateGap(monthYearRange.getStartYear(), monthYearRange.getStartMonth(), monthYearRange.getEndYear(), monthYearRange.getEndMonth());
        
        
        XDateRange xDateRange = new XDateRange(startDate, endDate, gap);
        
        
        return xDateRange;
    }


    
   
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setMonthYearRangeGapCalculator(MonthYearRangeGapCalculator monthYearRangeGapCalculator) {
        this.monthYearRangeGapCalculator = monthYearRangeGapCalculator;
    }

    @Autowired
    public void setMonthYearStartDateCalculator(MonthYearStartDateCalculator monthYearStartDateCalculator) {
        this.monthYearStartDateCalculator = monthYearStartDateCalculator;
    }

    @Autowired
    public void setMonthYearEndDateCalculator(MonthYearEndDateCalculator monthYearEndDateCalculator) {
        this.monthYearEndDateCalculator = monthYearEndDateCalculator;
    }

    
}
