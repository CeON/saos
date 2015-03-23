package pl.edu.icm.saos.search.analysis.solr.result;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.value.Week;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.util.SearchDateTimeUtils;

import com.google.common.base.Preconditions;

/**
 * A {@link FacetValueConverter} handling week facet values.
 *
 * 
 * @author Łukasz Dumiszewski
 */
@Service("facetWeekValueConverter")
public class FacetWeekValueConverter implements FacetValueConverter {

    
    private XDatePeriodChecker xdatePeriodChecker;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public Week convert(String value) {
        
        Preconditions.checkNotNull(value);
        
        LocalDate localDate = SearchDateTimeUtils.convertISOStringToDate(value);
        
        return new Week(localDate, localDate.plusDays(6));
    
    }

    
    @Override
    public boolean handles(XSettings xsettings) {
        
        Preconditions.checkNotNull(xsettings);
        
        return xdatePeriodChecker.isDateFacet(xsettings, PeriodUnit.WEEK);
    
    }


    

    
    
    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setXdatePeriodChecker(XDatePeriodChecker xdatePeriodChecker) {
        this.xdatePeriodChecker = xdatePeriodChecker;
    }
    
}
