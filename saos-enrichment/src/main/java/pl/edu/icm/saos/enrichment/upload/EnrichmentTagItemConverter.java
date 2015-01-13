package pl.edu.icm.saos.enrichment.upload;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("enrichmentTagItemConverter")
public class EnrichmentTagItemConverter {

    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Converts {@link EnrichmentTagItem} into {@link UploadEnrichmentTag}
     */
    public UploadEnrichmentTag convertEnrichmentTagItem(EnrichmentTagItem enrichmentTagItem) {
        
        Preconditions.checkNotNull(enrichmentTagItem);
        
        UploadEnrichmentTag enrichmentTagTemp = new UploadEnrichmentTag();
        
        enrichmentTagTemp.setJudgmentId(enrichmentTagItem.getJudgmentId());
        enrichmentTagTemp.setTagType(enrichmentTagItem.getTagType());
        enrichmentTagTemp.setValue(enrichmentTagItem.getValue());
        
        return enrichmentTagTemp;
        
    }
    
}
