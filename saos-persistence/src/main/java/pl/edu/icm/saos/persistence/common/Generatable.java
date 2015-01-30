package pl.edu.icm.saos.persistence.common;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * 
 * Defines the implementing class as one whose objects can be generated from {@link EnrichmentTag}
 * 
 * @author Łukasz Dumiszewski
 */

public interface Generatable {

    /**
     * Says if the object is generated from {@link EnrichmentTag}
     */
    boolean isGenerated();
    
    /**
     * Mark the object as generated from {@link EnrichmentTag}
     */
    void markGenerated();
}
