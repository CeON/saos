package pl.edu.icm.saos.webapp.analysis.request.converter;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.webapp.analysis.request.UixMonthYearRange;
import pl.edu.icm.saos.webapp.analysis.request.UixRange;

import com.google.common.base.Preconditions;

/**
 * An {@link UixRangeConverter} implementation handling converting of {@link UixMonthYearRange}
 * 
 * @author Łukasz Dumiszewski
 */
@Service("UixMonthYearRangeConverter")
public class UixMonthYearRangeConverter implements UixRangeConverter {

    
    private MonthYearStartDateCalculator monthYearStartDateCalculator;
    
    private MonthYearEndDateCalculator monthYearEndDateCalculator;
    
    private MonthYearRangeGapCalculator monthYearRangeGapCalculator;
    
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    @Override
    public boolean handles(Class<? extends UixRange> uixRangeClass) {
        Preconditions.checkNotNull(uixRangeClass);
        
        return uixRangeClass == UixMonthYearRange.class;
    }

    
    @Override
    public XDateRange convert(UixRange uixRange) {
        
        Preconditions.checkNotNull(uixRange);
        
        UixMonthYearRange monthYearRange = (UixMonthYearRange)uixRange;
        
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
