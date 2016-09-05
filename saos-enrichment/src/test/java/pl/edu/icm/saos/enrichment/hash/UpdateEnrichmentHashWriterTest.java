package pl.edu.icm.saos.enrichment.hash;

import static org.mockito.Mockito.inOrder;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.enrichment.JudgmentEnrichmentHashRepository;
import pl.edu.icm.saos.persistence.enrichment.model.JudgmentEnrichmentHash;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateEnrichmentHashWriterTest {

    @InjectMocks
    private UpdateEnrichmentHashWriter updateEnrichmentHashWriter = new UpdateEnrichmentHashWriter();
    
    @Mock
    private JudgmentEnrichmentHashRepository judgmentEnrichmentHashRepository;
    
    @Mock
    private EntityManager entityManager;
    
    
    public void setUp() {
        updateEnrichmentHashWriter.setJudgmentEnrichmentHashRepository(judgmentEnrichmentHashRepository);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void write() throws Exception {
        
        // given
        
        JudgmentEnrichmentHash hash1 = new JudgmentEnrichmentHash();
        JudgmentEnrichmentHash hash2 = new JudgmentEnrichmentHash();
        
        List<JudgmentEnrichmentHash> hashes = Lists.newArrayList(hash1, hash2);
        
        
        // execute
        
        updateEnrichmentHashWriter.write(hashes);
        
        
        // assert
        
        InOrder inOrder = inOrder(judgmentEnrichmentHashRepository, entityManager);
        inOrder.verify(judgmentEnrichmentHashRepository).save(hashes);
        inOrder.verify(judgmentEnrichmentHashRepository).flush();
        inOrder.verify(entityManager).clear();
        
    }
    
}
