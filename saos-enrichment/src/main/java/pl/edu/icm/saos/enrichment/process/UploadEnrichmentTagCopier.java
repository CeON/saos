package pl.edu.icm.saos.enrichment.process;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;

/**
 * @author Łukasz Dumiszewski
 */
@Service("uploadEnrichmentTagCopier")
public class UploadEnrichmentTagCopier {

    
    private EntityManager entityManager;
    

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Copies uploaded enrichment tags {@link UploadEnrichmentTag} to production tags {@link EnrichmentTag} <br/>
     * Omits tags that are assigned to judgments that do not exist anymore - it is possible that certain judgments for which
     * the SAOS Enricher had generated tags were removed later (but before the upload of the tags).
     */
    public void copyUploadedEnrichmentTags() {

        Query query = entityManager.createQuery("insert into " + EnrichmentTag.class.getName() + " enrichementTag "
                        + " (select uploadEnrichmentTag from " + UploadEnrichmentTag.class + " uploadEnrichmentTag"
                                + " where exists (select judgment from Judgment judgment where judgment.id = uploadEnrichmentTag.judgmentId))");
        
        query.executeUpdate();
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
}
