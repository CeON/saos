package pl.edu.icm.saos.search.analysis.solr.result;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XSettings;

/**
 * 
 * A simple {@link FacetValueConverter} just returning passed facet value.
 *
 * @author Łukasz Dumiszewski
 */
@Service("simpleFacetValueConverter")
public class FacetSimpleValueConverter implements FacetValueConverter {

    
    
    //------------------------ LOGIC --------------------------
    
    
    @Override
    public Object convert(String value, XSettings xsettings) {
        return value;
    }

    
    @Override
    public boolean handles(XSettings xsettings) {
        return true;
    }

}
