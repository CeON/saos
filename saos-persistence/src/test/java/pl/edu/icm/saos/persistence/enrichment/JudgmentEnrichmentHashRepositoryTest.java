package pl.edu.icm.saos.persistence.enrichment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.enrichment.model.JudgmentEnrichmentHash;


/**
 * @author madryk
 */
@Category(SlowTest.class)
public class JudgmentEnrichmentHashRepositoryTest extends PersistenceTestSupport {

    @Autowired
    private JudgmentEnrichmentHashRepository judgmentEnrichmentHashRepository;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void markAllAsProcessed() {
        // given
        JudgmentEnrichmentHash enrichmentHash1 = createJudgmentEnrichmentHash(1L, true);
        JudgmentEnrichmentHash enrichmentHash2 = createJudgmentEnrichmentHash(2L, false);
        JudgmentEnrichmentHash enrichmentHash3 = createJudgmentEnrichmentHash(3L, false);
        judgmentEnrichmentHashRepository.save(Lists.newArrayList(enrichmentHash1, enrichmentHash2, enrichmentHash3));
        
        // execute
        judgmentEnrichmentHashRepository.markAllAsProcessed();
        
        // assert
        assertTrue(judgmentEnrichmentHashRepository.findOne(enrichmentHash1.getId()).isProcessed());
        assertTrue(judgmentEnrichmentHashRepository.findOne(enrichmentHash2.getId()).isProcessed());
        assertTrue(judgmentEnrichmentHashRepository.findOne(enrichmentHash3.getId()).isProcessed());
    }
    
    @Test
    public void findAllJudgmentsIdsToProcess() {
        // given
        JudgmentEnrichmentHash enrichmentHash1 = createJudgmentEnrichmentHash(1L, true);
        JudgmentEnrichmentHash enrichmentHash2 = createJudgmentEnrichmentHash(2L, false);
        JudgmentEnrichmentHash enrichmentHash3 = createJudgmentEnrichmentHash(3L, false);
        judgmentEnrichmentHashRepository.save(Lists.newArrayList(enrichmentHash1, enrichmentHash2, enrichmentHash3));
        
        // execute
        List<Long> judgmentIds = judgmentEnrichmentHashRepository.findAllJudgmentsIdsToProcess();
        
        // assert
        assertThat(judgmentIds, containsInAnyOrder(2L, 3L));
    }
    
    @Test
    public void findByJudgmentId() {
        // given
        JudgmentEnrichmentHash enrichmentHash1 = createJudgmentEnrichmentHash(1L, false);
        JudgmentEnrichmentHash enrichmentHash2 = createJudgmentEnrichmentHash(2L, false);
        judgmentEnrichmentHashRepository.save(Lists.newArrayList(enrichmentHash1, enrichmentHash2));
        
        // execute
        JudgmentEnrichmentHash retEnrichmentHash = judgmentEnrichmentHashRepository.findByJudgmentId(2L);
        
        // assert
        assertEquals(enrichmentHash2.getId(), retEnrichmentHash.getId());
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private JudgmentEnrichmentHash createJudgmentEnrichmentHash(long judgmentId, boolean processed) {
        JudgmentEnrichmentHash judgmentEnrichmentHash = new JudgmentEnrichmentHash();
        
        judgmentEnrichmentHash.setJudgmentId(judgmentId);
        ReflectionTestUtils.setField(judgmentEnrichmentHash, "processed", processed);
        
        return judgmentEnrichmentHash;
    }
}
