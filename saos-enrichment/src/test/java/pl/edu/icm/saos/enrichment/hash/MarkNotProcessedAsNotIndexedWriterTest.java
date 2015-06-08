package pl.edu.icm.saos.enrichment.hash;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class MarkNotProcessedAsNotIndexedWriterTest {

    @InjectMocks
    private MarkChangedTagJudgmentsAsNotIndexedWriter markChangedTagJudgmentsAsNotIndexedWriter;
    
    @Mock
    private JudgmentRepository judgmentRepository;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void write() throws Exception {
        // execute
        markChangedTagJudgmentsAsNotIndexedWriter.write(Lists.newArrayList(2L, 3L));
        
        // assert
        verify(judgmentRepository).markAsNotIndexed(Lists.newArrayList(2L, 3L));
        verifyNoMoreInteractions(judgmentRepository);
    }
    
}
