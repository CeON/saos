package pl.edu.icm.saos.enrichment.upload;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("enrichmentTagItemConverter")
public class EnrichmentTagItemConverter {

    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Converts {@link EnrichmentTagItem} into {@link EnrichmentTag}
     */
    public EnrichmentTag convertEnrichmentTagItem(EnrichmentTagItem enrichmentTagItem) {
        
        Preconditions.checkNotNull(enrichmentTagItem);
        
        EnrichmentTag enrichmentTag = new EnrichmentTag();
        
        enrichmentTag.setJudgmentId(enrichmentTagItem.getJudgmentId());
        enrichmentTag.setTagType(enrichmentTagItem.getTagType());
        enrichmentTag.setValue(enrichmentTagItem.getValue());
        
        return enrichmentTag;
        
    }
    
}
