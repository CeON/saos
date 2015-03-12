package pl.edu.icm.saos.webapp.analysis.request;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.AbsoluteNumberYValue;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.request.YValueType;

/**
 * 
 * A converter of {@link UiySettings} objects
 * 
 * @author Łukasz Dumiszewski
 */
@Service("uiYSettingsConverter")
public class UiySettingsConverter {


    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts {@link UiySettings} into {@link YSettings}
     */
    public YSettings convert(UiySettings uiySettings) {
        
        // TODO: will be changed in https://github.com/CeON/saos/issues/600
        
        YSettings ysettings = new YSettings();
        
        YValueType yValueType = new AbsoluteNumberYValue();
        
        ysettings.setValueType(yValueType);
        
        return ysettings;
        
    }
    
    
}