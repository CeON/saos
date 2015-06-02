package pl.edu.icm.saos.enrichment.hash;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.item.ExecutionContext;

import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateEnrichmentHashReaderTest {

    @InjectMocks
    private UpdateEnrichmentHashReader updateEnrichmentHashReader = new UpdateEnrichmentHashReader();
    
    @Mock
    private JudgmentRepository judgmentRepository;
    
    @Mock
    private JudgmentEnrichmentTagsFetcher judgmentEnrichmentTagsFetcher;
    
    @Before
    public void setUp() {
        updateEnrichmentHashReader.setJudgmentsEnrichmentTagsPageSize(2);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void read() throws Exception {
        // given
        JudgmentEnrichmentTags judgmentEnrichmentTags1 = new JudgmentEnrichmentTags(1L);
        JudgmentEnrichmentTags judgmentEnrichmentTags2 = new JudgmentEnrichmentTags(2L);
        JudgmentEnrichmentTags judgmentEnrichmentTags3 = new JudgmentEnrichmentTags(3L);
        
        when(judgmentRepository.findAllIds()).thenReturn(Lists.newArrayList(1L, 2L, 3L));
        when(judgmentEnrichmentTagsFetcher.fetchEnrichmentTagsForJudgments(Lists.newArrayList(1L, 2L)))
            .thenReturn(Lists.newArrayList(judgmentEnrichmentTags1, judgmentEnrichmentTags2));
        when(judgmentEnrichmentTagsFetcher.fetchEnrichmentTagsForJudgments(Lists.newArrayList(3L)))
            .thenReturn(Lists.newArrayList(judgmentEnrichmentTags3));
        updateEnrichmentHashReader.open(mock(ExecutionContext.class));
        
        // execute
        JudgmentEnrichmentTags retJudgmentEnrichmentTags1 = updateEnrichmentHashReader.read();
        JudgmentEnrichmentTags retJudgmentEnrichmentTags2 = updateEnrichmentHashReader.read();
        JudgmentEnrichmentTags retJudgmentEnrichmentTags3 = updateEnrichmentHashReader.read();
        JudgmentEnrichmentTags retJudgmentEnrichmentTags4 = updateEnrichmentHashReader.read();
        
        // assert
        assertTrue(judgmentEnrichmentTags1 == retJudgmentEnrichmentTags1);
        assertTrue(judgmentEnrichmentTags2 == retJudgmentEnrichmentTags2);
        assertTrue(judgmentEnrichmentTags3 == retJudgmentEnrichmentTags3);
        assertNull(retJudgmentEnrichmentTags4);
        
    }
    
}
