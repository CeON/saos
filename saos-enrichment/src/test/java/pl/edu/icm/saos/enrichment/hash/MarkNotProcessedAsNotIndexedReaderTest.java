package pl.edu.icm.saos.enrichment.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.item.ExecutionContext;

import pl.edu.icm.saos.persistence.enrichment.JudgmentEnrichmentHashRepository;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class MarkNotProcessedAsNotIndexedReaderTest {

    @InjectMocks
    private MarkChangedTagJudgmentsAsNotIndexedReader markChangedTagJudgmentsAsNotIndexedReader;
    
    @Mock
    private JudgmentEnrichmentHashRepository judgmentEnrichmentHashRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void read() throws Exception {
        // given
        when(judgmentEnrichmentHashRepository.findAllJudgmentsIdsToProcess()).thenReturn(Lists.newArrayList(2L, 3L));
        markChangedTagJudgmentsAsNotIndexedReader.open(mock(ExecutionContext.class));
        
        // execute
        Long retJudgmentId1 = markChangedTagJudgmentsAsNotIndexedReader.read();
        Long retJudgmentId2 = markChangedTagJudgmentsAsNotIndexedReader.read();
        Long retJudgmentId3 = markChangedTagJudgmentsAsNotIndexedReader.read();
        
        // assert
        assertEquals(Long.valueOf(2L), retJudgmentId1);
        assertEquals(Long.valueOf(3L), retJudgmentId2);
        assertNull(retJudgmentId3);
    }
}
