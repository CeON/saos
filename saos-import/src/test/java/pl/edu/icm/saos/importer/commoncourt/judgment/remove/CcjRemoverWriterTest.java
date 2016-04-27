package pl.edu.icm.saos.importer.commoncourt.judgment.remove;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.model.RemovedJudgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.RemovedJudgmentRepository;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class CcjRemoverWriterTest {

    @InjectMocks
    private CcjRemoverWriter ccjRemoverWriter = new CcjRemoverWriter();
    
    
    @Mock
    private JudgmentRepository judgmentRepository;
    
    @Mock
    private RemovedJudgmentRepository removedJudgmentRepository;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void write() throws Exception {
        
        // given
        
        RemovedJudgment judgmentToRemove1 = createRemovedJudgment(1L, "AA1");
        RemovedJudgment judgmentToRemove2 = createRemovedJudgment(2L, "AA2");
        
        
        // execute
        
        ccjRemoverWriter.write(Lists.newArrayList(judgmentToRemove1, judgmentToRemove2));
        
        
        // assert
        
        verify(judgmentRepository).delete(Lists.newArrayList(1L, 2L));
        verify(removedJudgmentRepository).save(Lists.newArrayList(judgmentToRemove1, judgmentToRemove2));
        
        verifyNoMoreInteractions(judgmentRepository, removedJudgmentRepository);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private RemovedJudgment createRemovedJudgment(long judgmentId, String sourceJudgmentId) {
        RemovedJudgment removedJudgment = new RemovedJudgment();
        
        removedJudgment.setRemovedJudgmentId(judgmentId);
        removedJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        removedJudgment.getSourceInfo().setSourceJudgmentId(sourceJudgmentId);
        
        return removedJudgment;
    }
}
