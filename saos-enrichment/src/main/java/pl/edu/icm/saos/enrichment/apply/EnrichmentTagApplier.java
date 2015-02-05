package pl.edu.icm.saos.enrichment.apply;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * A contract for classes enriching {@link Judgment} with data generated from {@link EnrichmentTag}. <br/>
 * An implementation of this interface will deal with a specific type of enrichment tags (see: {@link EnrichmentTagTypes}).
 * 
 * @author Łukasz Dumiszewski
 */

public interface EnrichmentTagApplier {

    
    /**
     * Generates and adds to the given judgment data from the passed enrichment tag.
     */
    public void applyEnrichmentTag(Judgment judgment, EnrichmentTag enrichmentTag);
    
    
    /**
     * Tells if this applier handles the given tag type, see: {@link EnrichmentTagTypes}
     */
    public boolean handlesEnrichmentTagType(String enrichmentTagType);
    
}
