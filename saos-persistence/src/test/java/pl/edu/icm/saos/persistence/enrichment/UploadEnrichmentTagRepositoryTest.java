package pl.edu.icm.saos.persistence.enrichment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pl.edu.icm.saos.persistence.enrichment.TestInMemoryUploadEnrichmentTagFactory.createUploadEnrichmentTag;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;


/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class UploadEnrichmentTagRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private UploadEnrichmentTagRepository uploadEnrichmentTagRepository;
    
   
    
    @Before
    public void before() {
        
        
    }

   
    //------------------------ TESTS --------------------------
    
    @Test
    public void saveAndFind() {
        
        // given
        
        UploadEnrichmentTag uploadEnrichmentTag = createUploadEnrichmentTag(EnrichmentTagTypes.REFERENCED_CASE_NUMBERS, "{\"caseNumbers\": [\"123\", \"234\"]}");
        
        
        // execute
        
        uploadEnrichmentTagRepository.save(uploadEnrichmentTag);
        
        UploadEnrichmentTag dbUploadEnrichmentTag = uploadEnrichmentTagRepository.findOne(uploadEnrichmentTag.getId());
        
        
        // assert
                
        assertTrue(uploadEnrichmentTag != dbUploadEnrichmentTag);
        assertEquals(uploadEnrichmentTag, dbUploadEnrichmentTag);
        

    }
    
    
    @Test(expected=DataIntegrityViolationException.class)
    public void save_InvalidJsonValue() {
        
        // given
        
        UploadEnrichmentTag uploadEnrichmentTag = createUploadEnrichmentTag(EnrichmentTagTypes.REFERENCED_CASE_NUMBERS, "{key:\"fff\"");
        
        // execute
        
        uploadEnrichmentTagRepository.save(uploadEnrichmentTag);
        
        
    }
    
    
    @Test
    public void findMaxCreationDate() {
        
        // given
        
        UploadEnrichmentTag uploadEnrichmentTag1 = createUploadEnrichmentTag(EnrichmentTagTypes.REFERENCED_CASE_NUMBERS, "{\"key\":\"fff\"}", 1, new DateTime(2015, 01, 01, 14, 35));
        uploadEnrichmentTagRepository.save(uploadEnrichmentTag1);
        
        UploadEnrichmentTag uploadEnrichmentTag2 = createUploadEnrichmentTag(EnrichmentTagTypes.REFERENCED_REGULATIONS, "{\"key\":\"fff\"}", 1, new DateTime(2015, 01, 01, 14, 36));
        uploadEnrichmentTagRepository.save(uploadEnrichmentTag2);
        
        UploadEnrichmentTag uploadEnrichmentTag3 = createUploadEnrichmentTag(EnrichmentTagTypes.REFERENCED_CASE_NUMBERS, "{\"key\":\"fax\"}", 2, new DateTime(2014, 01, 01, 17, 36));
        uploadEnrichmentTagRepository.save(uploadEnrichmentTag3);
        
        
        // execute
        
        DateTime maxCreationDate = uploadEnrichmentTagRepository.findMaxCreationDate();
        
        
        // assert
        
        assertEquals(maxCreationDate, uploadEnrichmentTag2.getCreationDate());
        
    }
   
    
    
    //------------------------ PRIVATE --------------------------
 
    
 
}
