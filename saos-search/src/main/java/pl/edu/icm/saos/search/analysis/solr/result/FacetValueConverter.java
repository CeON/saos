package pl.edu.icm.saos.search.analysis.solr.result;

import pl.edu.icm.saos.search.analysis.request.XSettings;


/**
 * 
 * A converter of a facet value to object.
 * 
 * @author Łukasz Dumiszewski
 */

public interface FacetValueConverter {

    
    /**
     * Converts facet value into different object
     */
    public Object convert(String value, XSettings xsettings);
    
    /**
     * Returns true if this converter handles facet values defined by the given xsettings
     */
    public boolean handles(XSettings xsettings);
}
