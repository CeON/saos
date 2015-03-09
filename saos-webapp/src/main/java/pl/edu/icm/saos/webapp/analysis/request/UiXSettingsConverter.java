package pl.edu.icm.saos.webapp.analysis.request;

import static pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit.MONTH;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;

/**
 * 
 * A converter of {@link UiXSettings} objects
 * 
 * @author Łukasz Dumiszewski
 */
@Service("uiXSettingsConverter")
public class UiXSettingsConverter {

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts {@link UiXSettings} into {@link XSettings}
     */
    public XSettings convert(UiXSettings uiXSettings) {
        
        // TODO: will be changed in https://github.com/CeON/saos/issues/599
        
        XSettings xsettings = new XSettings();
        xsettings.setField(XField.JUDGMENT_DATE);
        
        XDateRange dateRange = new XDateRange(new LocalDate().minusYears(10), new LocalDate(), new Period(1, MONTH));
        xsettings.setRange(dateRange);
        
        return xsettings;
        
    }
    
    
}
