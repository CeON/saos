package pl.edu.icm.saos.enrichment.process;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;

/**
 * A service for processing {@link UploadEnrichmentTag}s
 * @author Łukasz Dumiszewski
 */
@Service("uploadEnrichmentTagProcessor")
public class UploadEnrichmentTagProcessor {

    private UploadEnrichmentTagOverwriter uploadEnrichmentTagOverwriter;
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Processes the uploaded enrichment tags ({@link UploadedEnrichmentTag}) <br/> 
     * Uses {@link UploadEnrichmentTagOverwriter#overwriteEnrichmentTags()} internally
     * 
     */
    @Transactional
    public void processUploadedEnrichmentTags() {
        
        uploadEnrichmentTagOverwriter.overwriteEnrichmentTags();
        
    }

    
    
    //------------------------ PRIVATE --------------------------
    
    @Autowired
    public void setUploadEnrichmentTagOverwriter(UploadEnrichmentTagOverwriter uploadEnrichmentTagOverwriter) {
        this.uploadEnrichmentTagOverwriter = uploadEnrichmentTagOverwriter;
    }
    
}
