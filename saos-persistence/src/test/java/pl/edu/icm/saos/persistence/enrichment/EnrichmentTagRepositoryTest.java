package pl.edu.icm.saos.persistence.enrichment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;


/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class EnrichmentTagRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
   
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void saveAndFind() {
        
        // given
        
        EnrichmentTag enrichmentTag = createEnrichmentTag(EnrichmentTagTypes.REFERENCED_CASE_NUMBERS, "{\"caseNumbers\": [\"123\", \"234\"]}");
        
        
        // execute
        
        enrichmentTagRepository.save(enrichmentTag);
        
        EnrichmentTag dbEnrichmentTag = enrichmentTagRepository.findOne(enrichmentTag.getId());
        
        
        // assert
                
        assertTrue(enrichmentTag != dbEnrichmentTag);
        assertEquals(enrichmentTag, dbEnrichmentTag);
        

    }
    
    
    @Test(expected=DataIntegrityViolationException.class)
    public void save_InvalidJsonValue() {
        
        // given
        
        EnrichmentTag enrichmentTag = createEnrichmentTag(EnrichmentTagTypes.REFERENCED_CASE_NUMBERS, "{key:\"fff\"");
        
        // execute
        
        enrichmentTagRepository.save(enrichmentTag);
        
        
    }
    
    
    @Test
    public void findMaxCreationDate() {
        
        // given
        
        EnrichmentTag enrichmentTag1 = createEnrichmentTag(EnrichmentTagTypes.REFERENCED_CASE_NUMBERS, "{\"key\":\"fff\"}", 1, new DateTime(2015, 01, 01, 14, 35));
        enrichmentTagRepository.save(enrichmentTag1);
        
        EnrichmentTag enrichmentTag2 = createEnrichmentTag(EnrichmentTagTypes.REFERENCED_REGULATIONS, "{\"key\":\"fff\"}", 1, new DateTime(2015, 01, 01, 14, 36));
        enrichmentTagRepository.save(enrichmentTag2);
        
        EnrichmentTag enrichmentTag3 = createEnrichmentTag(EnrichmentTagTypes.REFERENCED_CASE_NUMBERS, "{\"key\":\"fax\"}", 2, new DateTime(2014, 01, 01, 17, 36));
        enrichmentTagRepository.save(enrichmentTag3);
        
        
        // execute
        
        DateTime maxCreationDate = enrichmentTagRepository.findMaxCreationDate();
        
        
        // assert
        
        assertEquals(maxCreationDate, enrichmentTag2.getCreationDate());
        
    }
   
    
    @Test
    public void findAllByJudgmentId() {
        
        // given
        
        EnrichmentTag enrichmentTag1 = createEnrichmentTag(EnrichmentTagTypes.REFERENCED_CASE_NUMBERS, "{\"key\":\"fff\"}", 1, new DateTime(2015, 01, 01, 14, 35));
        enrichmentTagRepository.save(enrichmentTag1);
        
        EnrichmentTag enrichmentTag2 = createEnrichmentTag(EnrichmentTagTypes.REFERENCED_REGULATIONS, "{\"key\":\"fff\"}", 1, new DateTime(2015, 01, 01, 14, 36));
        enrichmentTagRepository.save(enrichmentTag2);
        
        EnrichmentTag enrichmentTag3 = createEnrichmentTag(EnrichmentTagTypes.REFERENCED_CASE_NUMBERS, "{\"key\":\"fax\"}", 2, new DateTime(2014, 01, 01, 17, 36));
        enrichmentTagRepository.save(enrichmentTag3);
        
        
        // execute
        
        List<EnrichmentTag> enrichmentTags = enrichmentTagRepository.findAllByJudgmentId(1);
        
        
        // assert
        
        assertThat(enrichmentTags, containsInAnyOrder(enrichmentTag1, enrichmentTag2));
        
    }
   
    
    //------------------------ PRIVATE --------------------------
 
    private EnrichmentTag createEnrichmentTag(String enrichmentTagType, String enrichmentTagValue) {
        EnrichmentTag enrichmentTag = new EnrichmentTag();
        enrichmentTag.setTagType(enrichmentTagType);
        enrichmentTag.setValue(enrichmentTagValue);
        return enrichmentTag;
    }
    
    private EnrichmentTag createEnrichmentTag(String enrichmentTagType, String enrichmentTagValue, int judgmentId, DateTime creationDate) {
        EnrichmentTag enrichmentTag = createEnrichmentTag(enrichmentTagType, enrichmentTagValue);
        enrichmentTag.setJudgmentId(judgmentId);
        Whitebox.setInternalState(enrichmentTag, "creationDate", creationDate);
        return enrichmentTag;
    }
 
}
