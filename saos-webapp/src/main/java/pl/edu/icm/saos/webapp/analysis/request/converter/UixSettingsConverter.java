package pl.edu.icm.saos.webapp.analysis.request.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XRange;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.webapp.analysis.request.UixSettings;

import com.google.common.base.Preconditions;

/**
 * 
 * A converter of {@link UixSettings} into {@link XSettings} objects
 * 
 * @author Łukasz Dumiszewski
 */
@Service("uixSettingsConverter")
public class UixSettingsConverter {

    
    private UixRangeConverterManager uixRangeConverterManager;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts {@link UixSettings} into {@link XSettings}
     */
    public XSettings convert(UixSettings uixSettings) {
        
        Preconditions.checkNotNull(uixSettings);
        
        
        XSettings xsettings = new XSettings();
        
        
        xsettings.setField(uixSettings.getField());
        
        
        XRange xrange = uixRangeConverterManager.convertUixRange(uixSettings.getRange());
        
        xsettings.setRange(xrange);
        
        
        return xsettings;
        
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setUixRangeConverterManager(UixRangeConverterManager uixRangeConverterManager) {
        this.uixRangeConverterManager = uixRangeConverterManager;
    }
    
    
}
