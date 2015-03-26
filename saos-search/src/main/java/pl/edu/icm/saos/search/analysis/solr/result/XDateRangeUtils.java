package pl.edu.icm.saos.search.analysis.solr.result;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.search.analysis.request.XSettings;

/**
 * A utility service with methods used in converting facet values. 
 * 
 * @author Łukasz Dumiszewski
 */
@Service("xdateRangeUtils")
class XDateRangeUtils {
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns {@link XDateRange} from the passed xsettings if {@link XSettings#getRange()} is
     * instance of {@link XDateRange}. Returns null otherwise.
     * 
     */
    public XDateRange getDateRange(XSettings xsettings) {
        
        if (xsettings.getRange() instanceof XDateRange) {
            
            return (XDateRange)xsettings.getRange();
            
        }
        
        return null;
    }
    
    
    /**
     * Generates and returns the end date of the period based on the given startDate and period. Does not change the passed date. 
     */
    public LocalDate generateEndDate(LocalDate startDate, Period period) {
        
        if (period.getUnit().equals(PeriodUnit.DAY)) {
            
            return startDate.plusDays(period.getValue()-1);
            
        }
        
        if (period.getUnit().equals(PeriodUnit.MONTH)) {
            
            return startDate.plusMonths(period.getValue()).minusDays(1);
            
        }
        
        if (period.getUnit().equals(PeriodUnit.YEAR)) {
            
            return startDate.plusYears(period.getValue()).minusDays(1);
            
        }
        
        throw new IllegalArgumentException("Period unit " + period.getUnit() + " not handled");
    }
    

}
