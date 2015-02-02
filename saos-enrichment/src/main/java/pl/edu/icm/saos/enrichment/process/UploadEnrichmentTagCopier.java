package pl.edu.icm.saos.enrichment.process;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;

/**
 * A service containing methods for copying uploaded enrichment tags ({@link UploadEnrichmentTag}) to production enrichment tags ({@link EnrichmentTag}) 
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
    @Transactional
    public void copyUploadedEnrichmentTags() {
    	Query query = entityManager.createNativeQuery("insert into enrichment_tag(id, creation_date, ver, judgment_id, tag_type, value)"   
                        + " (select nextval('seq_enrichment_tag'), creation_date, 0, uploadTag.judgment_id, uploadTag.tag_type, uploadTag.value from upload_enrichment_tag uploadTag"
                                + " where exists (select judgment from judgment judgment where judgment.id = uploadTag.judgment_id))");
        
        query.executeUpdate();
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
}
