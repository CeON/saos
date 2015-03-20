package pl.edu.icm.saos.webapp.analysis.request.converter;

import pl.edu.icm.saos.search.analysis.request.XRange;
import pl.edu.icm.saos.webapp.analysis.request.UixRange;

/**
 * 
 * A {@link UixRange} to {@link XRange} converter
 * 
 * @author Łukasz Dumiszewski
 */

public interface UixRangeConverter {

    
    /**
     * Returns true if this converter handles converting objects of the passed {@link UixRange} class 
     */
    public boolean handles(Class<? extends UixRange> uixRangeClass);
    
    /**
     * Converts the given {@link UixRange} into {@link XRange} 
     */
    public XRange convert(UixRange uixRange);
    
}
