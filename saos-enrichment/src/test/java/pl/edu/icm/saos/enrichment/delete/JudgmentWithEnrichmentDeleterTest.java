package pl.edu.icm.saos.enrichment.delete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static pl.edu.icm.saos.common.json.JsonNormalizer.normalizeJson;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.enrichment.EnrichmentTestSupport;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class JudgmentWithEnrichmentDeleterTest extends EnrichmentTestSupport {

    @Autowired
    private JudgmentWithEnrichmentDeleter judgmentWithEnrichmentDeleter;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void delete_ID_LIST() {
        // given
        Judgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
        Judgment scJudgment = testPersistenceObjectFactory.createScJudgment();
        Judgment ctJudgment = testPersistenceObjectFactory.createCtJudgment();
        Judgment nacJudgment = testPersistenceObjectFactory.createNacJudgment();
        
        testPersistenceObjectFactory.createEnrichmentTagsForJudgment(ccJudgment.getId());
        EnrichmentTag referenceTag1 = testPersistenceObjectFactory.createReferencedCourtCasesTag(nacJudgment.getId(), ccJudgment);
        EnrichmentTag referenceTag2 = testPersistenceObjectFactory.createReferencedCourtCasesTag(ctJudgment.getId(), ccJudgment);
        
        // execute
        judgmentWithEnrichmentDeleter.delete(Lists.newArrayList(ccJudgment.getId(), scJudgment.getId()));
        
        // assert
        assertFalse(judgmentRepository.exists(ccJudgment.getId()));
        assertFalse(judgmentRepository.exists(scJudgment.getId()));
        assertEquals(0, enrichmentTagRepository.findAllByJudgmentId(ccJudgment.getId()).size());
        assertEnrichmentTagValue(referenceTag1.getId(), normalizeJson("[{'caseNumber':'"+ccJudgment.getCaseNumbers().get(0)+"','judgmentIds':[]}]"));
        assertEnrichmentTagValue(referenceTag2.getId(), normalizeJson("[{'caseNumber':'"+ccJudgment.getCaseNumbers().get(0)+"','judgmentIds':[]}]"));
    }
    
    @Test
    public void delete_SINGLE() {
        // given
        Judgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
        Judgment scJudgment = testPersistenceObjectFactory.createScJudgment();
        
        testPersistenceObjectFactory.createEnrichmentTagsForJudgment(ccJudgment.getId());
        EnrichmentTag referenceTag = testPersistenceObjectFactory.createReferencedCourtCasesTag(scJudgment.getId(), ccJudgment);
        
        // execute
        judgmentWithEnrichmentDeleter.delete(ccJudgment);
        
        // assert
        assertFalse(judgmentRepository.exists(ccJudgment.getId()));
        assertEquals(0, enrichmentTagRepository.findAllByJudgmentId(ccJudgment.getId()).size());
        assertEnrichmentTagValue(referenceTag.getId(), normalizeJson("[{'caseNumber':'"+ccJudgment.getCaseNumbers().get(0)+"','judgmentIds':[]}]"));
    }
    
    @Test
    public void delete_SINGLE_BY_ID() {
        // given
        Judgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
        Judgment scJudgment = testPersistenceObjectFactory.createScJudgment();
        
        testPersistenceObjectFactory.createEnrichmentTagsForJudgment(ccJudgment.getId());
        EnrichmentTag referenceTag = testPersistenceObjectFactory.createReferencedCourtCasesTag(scJudgment.getId(), ccJudgment);
        
        // execute
        judgmentWithEnrichmentDeleter.delete(ccJudgment.getId());
        
        // assert
        assertFalse(judgmentRepository.exists(ccJudgment.getId()));
        assertEquals(0, enrichmentTagRepository.findAllByJudgmentId(ccJudgment.getId()).size());
        assertEnrichmentTagValue(referenceTag.getId(), normalizeJson("[{'caseNumber':'"+ccJudgment.getCaseNumbers().get(0)+"','judgmentIds':[]}]"));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertEnrichmentTagValue(long enrichmentTagId, String json) {
        EnrichmentTag tag = enrichmentTagRepository.findOne(enrichmentTagId);
        
        assertEquals(normalizeJson(json), tag.getValue());
    }
}
