package pl.edu.icm.saos.enrichment.process;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service for processing {@link UploadEnrichmentTag}s
 * @author Łukasz Dumiszewski
 */
@Service("uploadEnrichmentTagProcessor")
public class UploadEnrichmentTagProcessor {

    private UploadEnrichmentTagOverwriter uploadEnrichmentTagOverwriter;
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Checks whether there are newly uploaded enrichment tags ({@link UploadedEnrichmentTag}) and in case they are
     * overwrites with them the old ones ({@link EnrichmentTag}). <br/><br/>
     * Uses {@link UploadEnrichmentTagOverwriter#shouldEnrichmentTagsBeOverwritten()} and {@link UploadEnrichmentTagOverwriter#overwriteEnrichmentTags()}
     * internally.
     * 
     */
    @Transactional
    public void processUploadedEnrichmentTags() {
        
        if (uploadEnrichmentTagOverwriter.shouldEnrichmentTagsBeOverwritten()) {
            
            uploadEnrichmentTagOverwriter.overwriteEnrichmentTags();
        
        }
        
    }

    
    
    //------------------------ PRIVATE --------------------------
    
    @Autowired
    public void setUploadEnrichmentTagOverwriter(UploadEnrichmentTagOverwriter uploadEnrichmentTagOverwriter) {
        this.uploadEnrichmentTagOverwriter = uploadEnrichmentTagOverwriter;
    }
    
}
