package pl.edu.icm.saos.search.analysis.solr.result;

import static pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit.YEAR;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.value.YearPeriod;
import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.util.SearchDateTimeUtils;

import com.google.common.base.Preconditions;

/**
 * A {@link FacetValueConverter} handling year period facet values.
 *
 * 
 * @author Łukasz Dumiszewski
 */
@Service("FacetYearValueConverter")
public class FacetYearPeriodValueConverter implements FacetValueConverter {

    
    private XDateRangeUtils xdateRangeUtils;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public YearPeriod convert(String value, XSettings xsettings) {
        
        Preconditions.checkNotNull(value);
        Preconditions.checkNotNull(xsettings);
        
        LocalDate startDate = SearchDateTimeUtils.convertISOStringToDate(value);
        
        Period period = xdateRangeUtils.getDateRange(xsettings).getGap();
        
        LocalDate endDate = xdateRangeUtils.generateEndDate(startDate, period);
        
        return new YearPeriod(startDate.getYear(), endDate.getYear());
    
    }

    
    @Override
    public boolean handles(XSettings xsettings) {
        
        Preconditions.checkNotNull(xsettings);
        
        XDateRange range = xdateRangeUtils.getDateRange(xsettings);
        
        if (range != null 
                && (range.getGap().getUnit().equals(YEAR) && range.getStartDate().getDayOfMonth() == 1 && range.getStartDate().getMonthOfYear() == 1)) {
                
            return true;
                
        }
        
        return false;
    
    }


    

    
    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setXdateRangeUtils(XDateRangeUtils xdateRangeUtils) {
        this.xdateRangeUtils = xdateRangeUtils;
    }


}
