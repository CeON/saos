package pl.edu.icm.saos.webapp.analysis.request.converter;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.AbsoluteNumberYValue;
import pl.edu.icm.saos.search.analysis.request.RateYValue;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.request.YValueType;
import pl.edu.icm.saos.webapp.analysis.request.UiySettings;
import pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType;

/**
 * 
 * A converter of {@link UiySettings} into {@link YSettings} objects
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
        
        YSettings ysettings = new YSettings();
        
        YValueType yValueType = new AbsoluteNumberYValue();
        
        if (uiySettings.getValueType().equals(UiyValueType.PERCENT)) {
            
            yValueType = new RateYValue(100);
            
        } else if (uiySettings.getValueType().equals(UiyValueType.NUMBER_PER_1000)) {
            
            yValueType = new RateYValue(1000);
        }
                
        ysettings.setValueType(yValueType);
        
        return ysettings;
        
    }
    
    
}
