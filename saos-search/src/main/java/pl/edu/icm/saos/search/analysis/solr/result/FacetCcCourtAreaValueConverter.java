package pl.edu.icm.saos.search.analysis.solr.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.util.CcCourtAreaFieldValueConverter;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("facetCcCourtAreaValueConverter")
public class FacetCcCourtAreaValueConverter implements FacetValueConverter {

    
    private CcCourtAreaFieldValueConverter ccCourtAreaFieldValueConverter;
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    @Override
    public Object convert(String value, XSettings xsettings) {
        
        Preconditions.checkNotNull(value);
        
        return ccCourtAreaFieldValueConverter.convert(value);
    }

    
    @Override
    public boolean handles(XSettings xsettings) {
        
        Preconditions.checkNotNull(xsettings);
        
        return xsettings.getField() == XField.CC_APPEAL || xsettings.getField() == XField.CC_REGION || xsettings.getField() == XField.CC_DISTRICT;
    }


    
    //------------------------ SETTERS --------------------------
    
    
    @Autowired
    public void setCcCourtAreaFieldValueConverter(CcCourtAreaFieldValueConverter ccCourtAreaFieldValueConverter) {
        this.ccCourtAreaFieldValueConverter = ccCourtAreaFieldValueConverter;
    }


}
