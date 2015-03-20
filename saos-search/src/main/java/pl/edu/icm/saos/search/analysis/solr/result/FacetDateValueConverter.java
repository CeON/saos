package pl.edu.icm.saos.search.analysis.solr.result;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.util.SearchDateTimeUtils;

import com.google.common.base.Preconditions;

/**
 * 
 * A {@link FacetValueConverter} handling date facet values.
 *
 * 
 * @author Łukasz Dumiszewski
 */
@Service("facetDateValueConverter")
public class FacetDateValueConverter implements FacetValueConverter {
    
    
    private XDatePeriodChecker xdatePeriodChecker;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public LocalDate convert(String value) {
        
        Preconditions.checkNotNull(value);
        
        return SearchDateTimeUtils.convertISOStringToDate(value);
    
    }

    
    @Override
    public boolean handles(XSettings xsettings) {
        
        Preconditions.checkNotNull(xsettings);
        
        return xdatePeriodChecker.isDateFacet(xsettings, PeriodUnit.DAY);
    
    }


    

    
    
    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setXdatePeriodChecker(XDatePeriodChecker xdatePeriodChecker) {
        this.xdatePeriodChecker = xdatePeriodChecker;
    }


}
