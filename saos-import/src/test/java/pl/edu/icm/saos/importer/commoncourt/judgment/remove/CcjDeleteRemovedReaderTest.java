package pl.edu.icm.saos.importer.commoncourt.judgment.remove;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.item.ExecutionContext;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class CcjDeleteRemovedReaderTest {

    @InjectMocks
    private CcjDeleteRemovedReader ccjDeleteRemovedReader = new CcjDeleteRemovedReader();
    
    @Mock
    private CcRemovedJudgmentsFinder ccRemovedJudgmentsFinder;
    
    @Mock
    private JudgmentRepository judgmentRepository;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void read() throws Exception {
        
        // given
        
        Judgment judgment1 = mock(Judgment.class);
        Judgment judgment2 = mock(Judgment.class);
        Judgment judgment3 = mock(Judgment.class);
        
        when(ccRemovedJudgmentsFinder.findCcRemovedJudgmentSourceIds()).thenReturn(Lists.newArrayList(1L, 2L, 3L));
        when(judgmentRepository.findOne(1L)).thenReturn(judgment1);
        when(judgmentRepository.findOne(2L)).thenReturn(judgment2);
        when(judgmentRepository.findOne(3L)).thenReturn(judgment3);
        
        ccjDeleteRemovedReader.open(mock(ExecutionContext.class));
        
        
        // execute
        
        Judgment retJudgment1 = ccjDeleteRemovedReader.read();
        Judgment retJudgment2 = ccjDeleteRemovedReader.read();
        Judgment retJudgment3 = ccjDeleteRemovedReader.read();
        Judgment retJudgment4 = ccjDeleteRemovedReader.read();
        
        
        // assert
        
        assertTrue(retJudgment1 == judgment1);
        assertTrue(retJudgment2 == judgment2);
        assertTrue(retJudgment3 == judgment3);
        assertNull(retJudgment4);
    }
    
}
