package pl.edu.icm.saos.enrichment.process;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service("uploadEnrichmentTagProcessor")
public class UploadEnrichmentTagProcessor {

    private UploadEnrichmentTagOverwriter uploadEnrichmentTagOverwriter;
    
    
    
    //------------------------ LOGIC --------------------------
    
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
