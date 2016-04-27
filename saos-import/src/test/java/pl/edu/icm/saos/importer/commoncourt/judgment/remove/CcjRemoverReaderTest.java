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
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class CcjRemoverReaderTest {

    @InjectMocks
    private CcjRemoverReader ccjRemoverReader = new CcjRemoverReader();
    
    @Mock
    private SourceCcRemovedJudgmentsFinder sourceRemovedJudgmentsFinder;
    
    @Mock
    private JudgmentRepository judgmentRepository;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void read() throws Exception {
        
        // given
        
        Judgment judgmentAA1 = mock(Judgment.class);
        Judgment judgmentAA2 = mock(Judgment.class);
        Judgment judgmentAA3 = mock(Judgment.class);
        
        when(sourceRemovedJudgmentsFinder.findRemovedJudgments()).thenReturn(Lists.newArrayList("AA1", "AA2", "AA3"));
        when(judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, "AA1")).thenReturn(judgmentAA1);
        when(judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, "AA2")).thenReturn(judgmentAA2);
        when(judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, "AA3")).thenReturn(judgmentAA3);
        
        ccjRemoverReader.open(mock(ExecutionContext.class));
        
        
        // execute
        
        Judgment retJudgment1 = ccjRemoverReader.read();
        Judgment retJudgment2 = ccjRemoverReader.read();
        Judgment retJudgment3 = ccjRemoverReader.read();
        Judgment retJudgment4 = ccjRemoverReader.read();
        
        
        // assert
        
        assertTrue(retJudgment1 == judgmentAA1);
        assertTrue(retJudgment2 == judgmentAA2);
        assertTrue(retJudgment3 == judgmentAA3);
        assertNull(retJudgment4);
    }
    
}
