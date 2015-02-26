package pl.edu.icm.saos.persistence.enrichment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory;
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
        
        // execute
        
        EnrichmentTag enrichmentTag = createAndSaveEnrichmentTag(1, EnrichmentTagTypes.REFERENCED_COURT_CASES, "{\"caseNumbers\": [\"123\", \"234\"]}");
        
        enrichmentTagRepository.save(enrichmentTag);
        
        EnrichmentTag dbEnrichmentTag = enrichmentTagRepository.findOne(enrichmentTag.getId());
        
        
        // assert
                
        assertTrue(enrichmentTag != dbEnrichmentTag);
        assertEquals(enrichmentTag, dbEnrichmentTag);
        

    }
    
    
    @Test(expected=DataIntegrityViolationException.class)
    public void save_InvalidJsonValue() {

        // given
        EnrichmentTag enrichmentTag = new EnrichmentTag();
        enrichmentTag.setJudgmentId(1);
        enrichmentTag.setTagType(EnrichmentTagTypes.REFERENCED_COURT_CASES);
        enrichmentTag.setValue("{key:\"fff\"");
        
        // execute
        enrichmentTagRepository.save(enrichmentTag);
        
    }
    
    
    @Test
    public void findMaxCreationDate() {
        
        // given
        
        createAndSaveEnrichmentTag(1, EnrichmentTagTypes.REFERENCED_COURT_CASES, "{\"key\":\"fff\"}", new DateTime(2015, 01, 01, 14, 35));
        
        EnrichmentTag enrichmentTag2 = createAndSaveEnrichmentTag(1, EnrichmentTagTypes.REFERENCED_REGULATIONS, "{\"key\":\"fff\"}", new DateTime(2015, 01, 01, 14, 36));
        
        createAndSaveEnrichmentTag(2, EnrichmentTagTypes.REFERENCED_COURT_CASES, "{\"key\":\"fax\"}", new DateTime(2014, 01, 01, 17, 36));
        
        
        // execute
        
        DateTime maxCreationDate = enrichmentTagRepository.findMaxCreationDate();
        
        
        // assert
        
        assertEquals(maxCreationDate, enrichmentTag2.getCreationDate());
        
    }
   
    
    @Test
    public void findAllByJudgmentId() {
        
        // given
        
        EnrichmentTag enrichmentTag1 = createAndSaveEnrichmentTag(1, EnrichmentTagTypes.REFERENCED_COURT_CASES, "{\"key\":\"fff\"}", new DateTime(2015, 01, 01, 14, 35));
        EnrichmentTag enrichmentTag2 = createAndSaveEnrichmentTag(1, EnrichmentTagTypes.REFERENCED_REGULATIONS, "{\"key\":\"fff\"}", new DateTime(2015, 01, 01, 14, 36));
        createAndSaveEnrichmentTag(2, EnrichmentTagTypes.REFERENCED_COURT_CASES, "{\"key\":\"fax\"}", new DateTime(2014, 01, 01, 17, 36));
        
        
        // execute
        
        List<EnrichmentTag> enrichmentTags = enrichmentTagRepository.findAllByJudgmentId(1);
        
        
        // assert
        
        assertThat(enrichmentTags, containsInAnyOrder(enrichmentTag1, enrichmentTag2));
        
    }
   
    
    @Test
    public void findAllByJudgmentIds() {
        
        // given
        
        EnrichmentTag enrichmentTag1 = createAndSaveEnrichmentTag(1, EnrichmentTagTypes.REFERENCED_COURT_CASES, "{\"key\":\"fff\"}");
        EnrichmentTag enrichmentTag2 = createAndSaveEnrichmentTag(1, EnrichmentTagTypes.REFERENCED_REGULATIONS, "{\"key\":\"fff\"}");
        EnrichmentTag enrichmentTag3 = createAndSaveEnrichmentTag(2, EnrichmentTagTypes.REFERENCED_COURT_CASES, "{\"key\":\"fax\"}");
        createAndSaveEnrichmentTag(3, EnrichmentTagTypes.REFERENCED_COURT_CASES, "{\"key\":\"fax\"}");
        
        // execute
        
        List<EnrichmentTag> enrichmentTags = enrichmentTagRepository.findAllByJudgmentIds(Lists.newArrayList(1l, 2l));
        
        
        // assert
        
        assertThat(enrichmentTags, containsInAnyOrder(enrichmentTag1, enrichmentTag2, enrichmentTag3));
        
    }
    
    @Test
    public void deleteAllByJudgmentId() {
        // given
        createAndSaveEnrichmentTag(1, EnrichmentTagTypes.REFERENCED_COURT_CASES, "{\"key\":\"fff\"}");
        createAndSaveEnrichmentTag(1, EnrichmentTagTypes.REFERENCED_REGULATIONS, "{\"key\":\"fff\"}");
        EnrichmentTag enrichmentTag3 = createAndSaveEnrichmentTag(2, EnrichmentTagTypes.REFERENCED_COURT_CASES, "{\"key\":\"fax\"}");
        EnrichmentTag enrichmentTag4 = createAndSaveEnrichmentTag(3, EnrichmentTagTypes.REFERENCED_COURT_CASES, "{\"key\":\"fax\"}");
        
        // execute
        enrichmentTagRepository.deleteAllByJudgmentId(1);
        
        // assert
        assertThat(enrichmentTagRepository.findAll(), containsInAnyOrder(enrichmentTag3, enrichmentTag4));
    }
   
    
    //------------------------ PRIVATE --------------------------
 
    private EnrichmentTag createAndSaveEnrichmentTag(long judgmentId, String enrichmentTagType, String enrichmentTagValue, DateTime creationDate) {
        EnrichmentTag enrichmentTag = createAndSaveEnrichmentTag(judgmentId, enrichmentTagType, enrichmentTagValue);
        Whitebox.setInternalState(enrichmentTag, "creationDate", creationDate);
        enrichmentTagRepository.save(enrichmentTag);
        return enrichmentTag;
    }
    
    private EnrichmentTag createAndSaveEnrichmentTag(long judgmentId, String enrichmentTagType, String enrichmentTagValue) {
        EnrichmentTag enrichmentTag = TestInMemoryEnrichmentTagFactory.createEnrichmentTag(judgmentId, enrichmentTagType, enrichmentTagValue);
        enrichmentTagRepository.save(enrichmentTag);
        return enrichmentTag;
    }
 
}
