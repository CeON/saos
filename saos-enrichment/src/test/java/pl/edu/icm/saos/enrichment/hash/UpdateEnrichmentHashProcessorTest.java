package pl.edu.icm.saos.enrichment.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
    public void process_UPDATE_EXISTING_HASH() throws Exception {
        
        // given
        JudgmentEnrichmentTags judgmentEnrichmentTags = new JudgmentEnrichmentTags(2L);
        JudgmentEnrichmentHash judgmentEnrichmentHash = mock(JudgmentEnrichmentHash.class);
        
        when(judgmentEnrichmentTagsHashCalculator.calculateHash(judgmentEnrichmentTags)).thenReturn("hash");
        when(judgmentEnrichmentHashRepository.findByJudgmentId(2L)).thenReturn(judgmentEnrichmentHash);
        
        
        // execute
        JudgmentEnrichmentHash retJudgmentEnrichmentHash = updateEnrichmentHashProcessor.process(judgmentEnrichmentTags);
        
        
        // assert
        assertTrue(retJudgmentEnrichmentHash == judgmentEnrichmentHash);
        verify(judgmentEnrichmentHash).updateHash("hash");
        verifyNoMoreInteractions(judgmentEnrichmentHash);
    }
    
}
