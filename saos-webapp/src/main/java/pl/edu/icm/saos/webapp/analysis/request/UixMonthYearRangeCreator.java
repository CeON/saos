package pl.edu.icm.saos.webapp.analysis.request;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XField;

import com.google.common.base.Preconditions;

/**
 * A {@link UixRangeCreator} implementation handling {@link XField#JUDGMENT_DATE} type.
 * 
 * @author Łukasz Dumiszewski
 */
@Service("uixMonthYearRangeCreator")
public class UixMonthYearRangeCreator implements UixRangeCreator {

    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public boolean handles(XField xfield) {
        Preconditions.checkNotNull(xfield);
        
        return XField.JUDGMENT_DATE.equals(xfield);
    }

    
    @Override
    public UixMonthYearRange createRange() {
        
        UixMonthYearRange range = new UixMonthYearRange();

        LocalDate today = new LocalDate();
        range.setStartYear(today.getYear() - 20);
        range.setStartMonth(1);
        range.setEndYear(today.getYear());
        range.setEndMonth(today.getMonthOfYear());
        
        return range;
    }

}
