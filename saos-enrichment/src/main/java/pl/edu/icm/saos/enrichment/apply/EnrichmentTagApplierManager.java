package pl.edu.icm.saos.enrichment.apply;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;

import com.google.common.collect.Lists;

/**
 * Contains collection of {@link EnrichmentTagApplier} beans and dispenses an appropriate one on request
 * ({@link #getEnrichmentTagApplier(String)}).  
 * 
 * @author Łukasz Dumiszewski
 */
@Service("enrichmentTagApplierManager")
public class EnrichmentTagApplierManager {

    
    private List<EnrichmentTagApplier> enrichmentTagAppliers = Lists.newArrayList();

    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns an {@link EnrichmentTagApplier} (from the {@link #setEnrichmentTagAppliers(List)}) 
     * that handles the passed enrichmentTagType (see {@link EnrichmentTagTypes}), or null if no applier
     * for the given tag type can be found.
     * <br/><br/>
     * It the {@link #setEnrichmentTagAppliers(List)} contains more than one applier then the first found one is returned.
     */
    public EnrichmentTagApplier getEnrichmentTagApplier(String enrichmentTagType) {
        
        for (EnrichmentTagApplier enrichmentTagApplier : enrichmentTagAppliers) {
            
            if (enrichmentTagApplier.handlesEnrichmentTagType(enrichmentTagType)) {
                return enrichmentTagApplier;
            }
            
        }
        
        return null;
        
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setEnrichmentTagAppliers(List<EnrichmentTagApplier> enrichmentTagAppliers) {
        this.enrichmentTagAppliers = enrichmentTagAppliers;
    }
    
}
