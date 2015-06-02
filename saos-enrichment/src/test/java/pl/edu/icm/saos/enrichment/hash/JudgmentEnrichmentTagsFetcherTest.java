package pl.edu.icm.saos.enrichment.hash;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory.createEnrichmentTag;
import static pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory.createReferencedCourtCasesTag;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.enrichment.EnrichmentTestSupport;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class JudgmentEnrichmentTagsFetcherTest extends EnrichmentTestSupport {

    @Autowired
    private JudgmentEnrichmentTagsFetcher judgmentEnrichmentTagsFetcher;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;
    
    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    private TestObjectContext testObjectContext;
    
    
    private EnrichmentTag ccjRefCourtCasesTag;
    private EnrichmentTag scjRefCourtCasesTag;
    private EnrichmentTag ctjTag;
    
    @Before
    public void setUp() {
        
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        
        
        ccjRefCourtCasesTag = createReferencedCourtCasesTag(testObjectContext.getCcJudgmentId(), testObjectContext.getCtJudgment());
        scjRefCourtCasesTag = createReferencedCourtCasesTag(testObjectContext.getScJudgmentId(), testObjectContext.getCtJudgment(), testObjectContext.getNacJudgment());
        
        ctjTag = createEnrichmentTag(testObjectContext.getCtJudgmentId(), "SOME_TAG_TYPE", "{key:'value'}");
        
        enrichmentTagRepository.save(Lists.newArrayList(ccjRefCourtCasesTag, scjRefCourtCasesTag, ctjTag));
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void fetchEnrichmentTagsForJudgments() {
        
        // given
        long notExistingJudgmentId = testObjectContext.getCcJudgmentId() + testObjectContext.getScJudgmentId() + testObjectContext.getCtJudgmentId() + testObjectContext.getNacJudgmentId();
        
        // execute
        List<JudgmentEnrichmentTags> judgmentsEnrichmentTags = judgmentEnrichmentTagsFetcher.fetchEnrichmentTagsForJudgments(
                Lists.newArrayList(testObjectContext.getCtJudgmentId(), testObjectContext.getNacJudgmentId(), notExistingJudgmentId));
        
        // assert
        assertEquals(3, judgmentsEnrichmentTags.size());
        
        JudgmentEnrichmentTags ctjEnrichmentTags = getJudgmentEnrichmentTags(judgmentsEnrichmentTags, testObjectContext.getCtJudgmentId());
        assertNotNull(ctjEnrichmentTags);
        assertThat(ctjEnrichmentTags.getEnrichmentTags(), containsInAnyOrder(ccjRefCourtCasesTag, scjRefCourtCasesTag, ctjTag));
        
        
        JudgmentEnrichmentTags nacjEnrichmentTags = getJudgmentEnrichmentTags(judgmentsEnrichmentTags, testObjectContext.getNacJudgmentId());
        assertNotNull(nacjEnrichmentTags);
        assertThat(nacjEnrichmentTags.getEnrichmentTags(), containsInAnyOrder(scjRefCourtCasesTag));
        
        
        JudgmentEnrichmentTags notExistingJudgmentEnrichmentTags = getJudgmentEnrichmentTags(judgmentsEnrichmentTags, notExistingJudgmentId);
        assertNotNull(notExistingJudgmentEnrichmentTags);
        assertEquals(0, notExistingJudgmentEnrichmentTags.getEnrichmentTags().size());
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private JudgmentEnrichmentTags getJudgmentEnrichmentTags(List<JudgmentEnrichmentTags> judgmentsEnrichmentTags, long judgmentId) {
        return judgmentsEnrichmentTags.stream()
                .filter(judgmentTags -> judgmentTags.getJudgmentId() == judgmentId)
                .findFirst().orElse(null);
    }
}
