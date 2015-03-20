package pl.edu.icm.saos.webapp.analysis.request;

import pl.edu.icm.saos.search.analysis.request.XField;

/**
 * 
 * A contract for classes that create specific {@link UixRange} objects with default (for UI)
 * attributes.
 * 
 * @author Łukasz Dumiszewski
 */

public interface UixRangeCreator {

    /**
     * Returns true if the given creator can create {@link UixRange} for the specified xfield 
     */
    public boolean handles(XField xfield);
    
    /**
     * 
     * Creates a {@link UixRange} with default attributes.
     * 
     */
    public UixRange createRange();
    
    
}
