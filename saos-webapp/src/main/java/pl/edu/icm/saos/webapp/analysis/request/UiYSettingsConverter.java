package pl.edu.icm.saos.webapp.analysis.request;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.AbsoluteNumberYValue;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.request.YValueType;

/**
 * 
 * A converter of {@link UiYSettings} objects
 * 
 * @author Łukasz Dumiszewski
 */
@Service("uiYSettingsConverter")
public class UiYSettingsConverter {


    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts {@link UiYSettings} into {@link YSettings}
     */
    public YSettings convert(UiYSettings uiYSettings) {
        
        // TODO: will be changed in https://github.com/CeON/saos/issues/600
        
        YSettings ysettings = new YSettings();
        
        YValueType yValueType = new AbsoluteNumberYValue();
        
        ysettings.setValueType(yValueType);
        
        return ysettings;
        
    }
    
    
}
