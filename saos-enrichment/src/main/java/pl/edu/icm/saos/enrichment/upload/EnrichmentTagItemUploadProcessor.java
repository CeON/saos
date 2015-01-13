package pl.edu.icm.saos.enrichment.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.persistence.enrichment.UploadEnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;

/**
 * @author Łukasz Dumiszewski
 */

@Service("enrichmentTagItemUploadProcessor")
public class EnrichmentTagItemUploadProcessor {


    private EnrichmentTagItemConverter enrichmentTagItemConverter;
    
    private UploadEnrichmentTagRepository uploadEnrichmentTagRepository;
    
    private CommonValidator commonValidator;
    
    
    //------------------------ LOGIC --------------------------

    /**
     * Converts the passed enrichmentTagItem into {@link UploadEnrichmentTag} and saves it. 
     * @throws DataIntegrityViolationException if any of the datasource constraints has been violated
     */
    public void processEnrichmentTagItem(EnrichmentTagItem enrichmentTagItem) {
        
        commonValidator.validateEx(enrichmentTagItem);

        UploadEnrichmentTag uploadEnrichmentTag = enrichmentTagItemConverter.convertEnrichmentTagItem(enrichmentTagItem);
        
        uploadEnrichmentTagRepository.saveAndFlush(uploadEnrichmentTag);
        
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setEnrichmentTagItemConverter(EnrichmentTagItemConverter enrichmentTagItemConverter) {
        this.enrichmentTagItemConverter = enrichmentTagItemConverter;
    }

    @Autowired
    public void setUploadEnrichmentTagRepository(UploadEnrichmentTagRepository uploadEnrichmentTagRepository) {
        this.uploadEnrichmentTagRepository = uploadEnrichmentTagRepository;
    }

    @Autowired
    public void setCommonValidator(CommonValidator commonValidator) {
        this.commonValidator = commonValidator;
    }
    
}
