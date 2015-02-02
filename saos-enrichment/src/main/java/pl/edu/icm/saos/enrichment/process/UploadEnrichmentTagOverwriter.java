package pl.edu.icm.saos.enrichment.process;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.UploadEnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;

/**
 * A service for overwriting old enrichment tags ({@link EnrichmentTag}) with newly uploaded ones ({@link UploadEnrichmentTag})
 * @author Łukasz Dumiszewski
 */
@Service("uploadEnrichmentTagOverwriter")
public class UploadEnrichmentTagOverwriter {

    
    private UploadEnrichmentTagRepository uploadEnrichmentTagRepository;
    
    private EnrichmentTagRepository enrichmentTagRepository;
    
    private UploadEnrichmentTagCopier uploadEnrichmentTagCopier;
   
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns true if the uploaded tags ({@link UploadEnrichmentTag}) are newer than production tags ({@link EnrichmentTag})<br/> 
     * Can be used in a scheduled service that processes the uploaded enrichment tags.
     */
    public boolean shouldEnrichmentTagsBeOverwritten() {
        
        DateTime maxUploadEnrichmentTagCreationDate = uploadEnrichmentTagRepository.findMaxCreationDate();
        
        
        if (maxUploadEnrichmentTagCreationDate != null) {
            
            DateTime maxEnrichmentTagCreationDate = enrichmentTagRepository.findMaxCreationDate();
                
            if  (maxEnrichmentTagCreationDate == null || maxUploadEnrichmentTagCreationDate.isAfter(maxEnrichmentTagCreationDate.getMillis())) {
            
                return true;
            }
        }
        
        return false;
    }
    
    
    /**
     * Overwrites enrichment tags {@link EnrichmentTag} with uploaded ones {@link UploadEnrichmentTag}
     */
    @Transactional
    public void overwriteEnrichmentTags() {
        
        enrichmentTagRepository.deleteAllInBatch();

        uploadEnrichmentTagCopier.copyUploadedEnrichmentTags();
    }
    
    
    
    //------------------------ SETTERS --------------------------

    
    @Autowired
    public void setUploadEnrichmentTagRepository(UploadEnrichmentTagRepository uploadEnrichmentTagRepository) {
        this.uploadEnrichmentTagRepository = uploadEnrichmentTagRepository;
    }

    @Autowired
    public void setEnrichmentTagRepository(EnrichmentTagRepository enrichmentTagRepository) {
        this.enrichmentTagRepository = enrichmentTagRepository;
    }

    @Autowired
    public void setUploadEnrichmentTagCopier(UploadEnrichmentTagCopier uploadEnrichmentTagCopier) {
        this.uploadEnrichmentTagCopier = uploadEnrichmentTagCopier;
    }

    
}
