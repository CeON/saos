package pl.edu.icm.saos.enrichment.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagTempRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTemp;

/**
 * @author Łukasz Dumiszewski
 */

@Service("enrichmentTagItemUploadProcessor")
public class EnrichmentTagItemUploadProcessor {


    private EnrichmentTagItemConverter enrichmentTagItemConverter;
    
    private EnrichmentTagTempRepository enrichmentTagTempRepository;
    
    private CommonValidator commonValidator;
    
    
    //------------------------ LOGIC --------------------------

    /**
     * Converts the passed enrichmentTagItem into {@link EnrichmentTagTemp} and saves it. 
     * @throws DataIntegrityViolationException if any of the datasource constraints has been violated
     */
    public void processEnrichmentTagItem(EnrichmentTagItem enrichmentTagItem) {
        
        commonValidator.validateEx(enrichmentTagItem);

        EnrichmentTagTemp enrichmentTagTemp = enrichmentTagItemConverter.convertEnrichmentTagItem(enrichmentTagItem);
        
        enrichmentTagTempRepository.saveAndFlush(enrichmentTagTemp);
        
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setEnrichmentTagItemConverter(EnrichmentTagItemConverter enrichmentTagItemConverter) {
        this.enrichmentTagItemConverter = enrichmentTagItemConverter;
    }

    @Autowired
    public void setEnrichmentTagTempRepository(EnrichmentTagTempRepository enrichmentTagTempRepository) {
        this.enrichmentTagTempRepository = enrichmentTagTempRepository;
    }

    @Autowired
    public void setCommonValidator(CommonValidator commonValidator) {
        this.commonValidator = commonValidator;
    }
    
}
