package pl.edu.icm.saos.search.analysis.solr.result;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.search.analysis.request.XSettings;

/**
 * A utility service with methods used in converting facet values. 
 * 
 * @author Łukasz Dumiszewski
 */
@Service("xdatePeriodChecker")
class XDatePeriodChecker {
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Checks whether the given xsettings object defines a date range with the passed gap unit
     * 
     */
    public boolean isDateFacet(XSettings xsettings, PeriodUnit periodUnit) {
        
        if (xsettings.getRange() instanceof XDateRange) {
            
            XDateRange xdateRange = (XDateRange)xsettings.getRange();
            
            if (xdateRange.getGap().getUnit().equals(periodUnit)) {
                
                return true;
                
            }
            
        }
        
        return false;
    }

}
