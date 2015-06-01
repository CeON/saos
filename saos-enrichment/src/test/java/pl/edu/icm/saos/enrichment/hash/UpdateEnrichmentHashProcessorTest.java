package pl.edu.icm.saos.enrichment.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import pl.edu.icm.saos.persistence.enrichment.JudgmentEnrichmentHashRepository;
import pl.edu.icm.saos.persistence.enrichment.model.JudgmentEnrichmentHash;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateEnrichmentHashProcessorTest {

    @InjectMocks
    private UpdateEnrichmentHashProcessor updateEnrichmentHashProcessor;
    
    @Mock
    private JudgmentEnrichmentTagsHashCalculator judgmentEnrichmentTagsHashCalculator;
    
    @Mock
    private JudgmentEnrichmentHashRepository judgmentEnrichmentHashRepository;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void process_NEW_HASH() throws Exception {
        // given
        JudgmentEnrichmentTags judgmentEnrichmentTags = new JudgmentEnrichmentTags(2L);
        
        when(judgmentEnrichmentTagsHashCalculator.calculateHash(judgmentEnrichmentTags)).thenReturn("hash");
        when(judgmentEnrichmentHashRepository.findByJudgmentId(2L)).thenReturn(null);
        
        // execute
        JudgmentEnrichmentHash retJudgmentEnrichmentHash = updateEnrichmentHashProcessor.process(judgmentEnrichmentTags);
        
        // assert
        assertEquals(2L, retJudgmentEnrichmentHash.getJudgmentId());
        assertEquals(null, retJudgmentEnrichmentHash.getOldHash());
        assertEquals("hash", retJudgmentEnrichmentHash.getHash());
        assertFalse(retJudgmentEnrichmentHash.isProcessed());
    }
    
    @Test
    public void process_PROCESSED_NO_CHANGE() throws Exception {
        // given
        JudgmentEnrichmentTags judgmentEnrichmentTags = new JudgmentEnrichmentTags(2L);
        
        JudgmentEnrichmentHash judgmentEnrichmentHash = createJudgmentEnrichmentHash(2L, "oldHash", "hash", true);
        
        
        when(judgmentEnrichmentTagsHashCalculator.calculateHash(judgmentEnrichmentTags)).thenReturn("hash");
        when(judgmentEnrichmentHashRepository.findByJudgmentId(2L)).thenReturn(judgmentEnrichmentHash);
        
        // execute
        JudgmentEnrichmentHash retJudgmentEnrichmentHash = updateEnrichmentHashProcessor.process(judgmentEnrichmentTags);
        
        // assert
        assertEquals(2L, retJudgmentEnrichmentHash.getJudgmentId());
        assertEquals("hash", retJudgmentEnrichmentHash.getOldHash());
        assertEquals("hash", retJudgmentEnrichmentHash.getHash());
        assertTrue(retJudgmentEnrichmentHash.isProcessed());
    }
    
    @Test
    public void process_PROCESSED_WITH_CHANGE() throws Exception {
        // given
        JudgmentEnrichmentTags judgmentEnrichmentTags = new JudgmentEnrichmentTags(2L);
        
        JudgmentEnrichmentHash judgmentEnrichmentHash = createJudgmentEnrichmentHash(2L, "oldHash", "hash", true);
        
        
        when(judgmentEnrichmentTagsHashCalculator.calculateHash(judgmentEnrichmentTags)).thenReturn("newHash");
        when(judgmentEnrichmentHashRepository.findByJudgmentId(2L)).thenReturn(judgmentEnrichmentHash);
        
        // execute
        JudgmentEnrichmentHash retJudgmentEnrichmentHash = updateEnrichmentHashProcessor.process(judgmentEnrichmentTags);
        
        // assert
        assertEquals(2L, retJudgmentEnrichmentHash.getJudgmentId());
        assertEquals("hash", retJudgmentEnrichmentHash.getOldHash());
        assertEquals("newHash", retJudgmentEnrichmentHash.getHash());
        assertFalse(retJudgmentEnrichmentHash.isProcessed());
    }
    
    @Test
    public void process_NOT_PROCESSED() throws Exception {
        // given
        JudgmentEnrichmentTags judgmentEnrichmentTags = new JudgmentEnrichmentTags(2L);
        
        JudgmentEnrichmentHash judgmentEnrichmentHash = createJudgmentEnrichmentHash(2L, "oldHash", "hash", false);
        
        
        when(judgmentEnrichmentTagsHashCalculator.calculateHash(judgmentEnrichmentTags)).thenReturn("newHash");
        when(judgmentEnrichmentHashRepository.findByJudgmentId(2L)).thenReturn(judgmentEnrichmentHash);
        
        // execute
        JudgmentEnrichmentHash retJudgmentEnrichmentHash = updateEnrichmentHashProcessor.process(judgmentEnrichmentTags);
        
        // assert
        assertEquals(2L, retJudgmentEnrichmentHash.getJudgmentId());
        assertEquals("oldHash", retJudgmentEnrichmentHash.getOldHash());
        assertEquals("newHash", retJudgmentEnrichmentHash.getHash());
        assertFalse(retJudgmentEnrichmentHash.isProcessed());
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
