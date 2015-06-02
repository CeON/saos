package pl.edu.icm.saos.persistence.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import pl.edu.icm.saos.persistence.enrichment.model.JudgmentEnrichmentHash;

/**
 * @author madryk
 */
public class JudgmentEnrichmentHashTest {
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void updateHash_PROCESSED_NO_CHANGE() {
        // given
        JudgmentEnrichmentHash judgmentEnrichmentHash = createJudgmentEnrichmentHash(2L, "oldHash", "hash", true);
        
        // execute
        judgmentEnrichmentHash.updateHash("hash");
        
        // assert
        assertEquals("hash", judgmentEnrichmentHash.getOldHash());
        assertEquals("hash", judgmentEnrichmentHash.getHash());
        assertTrue(judgmentEnrichmentHash.isProcessed());
    }
    
    @Test
    public void updateHash_PROCESSED_WITH_CHANGE() {
        // given
        JudgmentEnrichmentHash judgmentEnrichmentHash = createJudgmentEnrichmentHash(2L, "oldHash", "hash", true);
        
        // execute
        judgmentEnrichmentHash.updateHash("newHash");
        
        // assert
        assertEquals("hash", judgmentEnrichmentHash.getOldHash());
        assertEquals("newHash", judgmentEnrichmentHash.getHash());
        assertFalse(judgmentEnrichmentHash.isProcessed());
    }
    
    @Test
    public void updateHash_NOT_PROCESSED() {
        // given
        JudgmentEnrichmentHash judgmentEnrichmentHash = createJudgmentEnrichmentHash(2L, "oldHash", "hash", false);
        
        // execute
        judgmentEnrichmentHash.updateHash("newHash");
        
        // assert
        assertEquals("oldHash", judgmentEnrichmentHash.getOldHash());
        assertEquals("newHash", judgmentEnrichmentHash.getHash());
        assertFalse(judgmentEnrichmentHash.isProcessed());
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private JudgmentEnrichmentHash createJudgmentEnrichmentHash(long judgmentId, String oldHash, String hash, boolean processed) {
        JudgmentEnrichmentHash judgmentEnrichmentHash = new JudgmentEnrichmentHash();
        
        judgmentEnrichmentHash.setJudgmentId(judgmentId);
        ReflectionTestUtils.setField(judgmentEnrichmentHash, "oldHash", oldHash);
        ReflectionTestUtils.setField(judgmentEnrichmentHash, "hash", hash);
        ReflectionTestUtils.setField(judgmentEnrichmentHash, "processed", processed);
        
        return judgmentEnrichmentHash;
    }
}
