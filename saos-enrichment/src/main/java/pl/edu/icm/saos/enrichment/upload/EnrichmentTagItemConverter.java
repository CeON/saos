package pl.edu.icm.saos.enrichment.upload;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTemp;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("enrichmentTagItemConverter")
public class EnrichmentTagItemConverter {

    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Converts {@link EnrichmentTagItem} into {@link EnrichmentTagTemp}
     */
    public EnrichmentTagTemp convertEnrichmentTagItem(EnrichmentTagItem enrichmentTagItem) {
        
        Preconditions.checkNotNull(enrichmentTagItem);
        
        EnrichmentTagTemp enrichmentTagTemp = new EnrichmentTagTemp();
        
        enrichmentTagTemp.setJudgmentId(enrichmentTagItem.getJudgmentId());
        enrichmentTagTemp.setTagType(enrichmentTagItem.getTagType());
        enrichmentTagTemp.setValue(enrichmentTagItem.getValue());
        
        return enrichmentTagTemp;
        
    }
    
}
