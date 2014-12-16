package pl.edu.icm.saos.enrichment.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * @author Łukasz Dumiszewski
 */

@Service("enrichmentTagItemUploadProcessor")
public class EnrichmentTagItemUploadProcessor {


    private EnrichmentTagItemConverter enrichmentTagItemConverter;
    
    private EnrichmentTagRepository enrichmentTagRepository;
    
    
    //------------------------ LOGIC --------------------------

    /**
     * Converts the passed enrichmentTagItem into {@link EnrichmentTag} and saves it. 
     * @throws DataIntegrityViolationException if any of the datasource constraints has been violated
     */
    public void processEnrichmentTagItem(EnrichmentTagItem enrichmentTagItem) {

        EnrichmentTag enrichmentTag = enrichmentTagItemConverter.convertEnrichmentTagItem(enrichmentTagItem);
        
        enrichmentTagRepository.saveAndFlush(enrichmentTag);
        
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setEnrichmentTagItemConverter(EnrichmentTagItemConverter enrichmentTagItemConverter) {
        this.enrichmentTagItemConverter = enrichmentTagItemConverter;
    }

    @Autowired
    public void setEnrichmentTagRepository(EnrichmentTagRepository enrichmentTagRepository) {
        this.enrichmentTagRepository = enrichmentTagRepository;
    }
    
}
