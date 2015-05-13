package pl.edu.icm.saos.search.analysis.solr.result;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.util.FieldValuePrefixAdder;

import com.google.common.base.Preconditions;

/**
 * Converter of facet values that are prefixed
 * 
 * @author Łukasz Dumiszewski
 */
@Service("prefixedFacetValueConverter")
public class PrefixedFacetValueConverter implements FacetValueConverter {

    private FieldValuePrefixAdder fieldValuePrefixAdder;
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    @Override
    public Object convert(String value, XSettings xsettings) {
        return fieldValuePrefixAdder.extractFieldValue(value);
    }

    
    @Override
    public boolean handles(XSettings xsettings) {
        Preconditions.checkNotNull(xsettings);
        
        return !StringUtils.isEmpty(xsettings.getFieldValuePrefix());
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setFieldValuePrefixAdder(FieldValuePrefixAdder fieldValuePrefixAdder) {
        this.fieldValuePrefixAdder = fieldValuePrefixAdder;
    }

}
