package pl.edu.icm.saos.enrichment.hash;

import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.persistence.enrichment.JudgmentEnrichmentHashRepository;
import pl.edu.icm.saos.persistence.enrichment.model.JudgmentEnrichmentHash;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateEnrichmentHashWriterTest {

    private UpdateEnrichmentHashWriter updateEnrichmentHashWriter = new UpdateEnrichmentHashWriter();
    
    @Mock
    private JudgmentEnrichmentHashRepository judgmentEnrichmentHashRepository;
    
    
    public void setUp() {
        updateEnrichmentHashWriter.setJudgmentEnrichmentHashRepository(judgmentEnrichmentHashRepository);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void write() {
        // given
        JudgmentEnrichmentHash hash1 = new JudgmentEnrichmentHash();
        JudgmentEnrichmentHash hash2 = new JudgmentEnrichmentHash();
        
        List<JudgmentEnrichmentHash> hashes = Lists.newArrayList(hash1, hash2);
        
        // execute
        judgmentEnrichmentHashRepository.save(hashes);
        
        // assert
        verify(judgmentEnrichmentHashRepository).save(hashes);
    }
    
}
