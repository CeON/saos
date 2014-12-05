package pl.edu.icm.saos.search.indexing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
public class ResetJudgmentIndexFlagStepExecutionListenerTest {

    private ResetJudgmentIndexFlagStepExecutionListener resetJudgmentIndexFlagStepExecutionListener = new ResetJudgmentIndexFlagStepExecutionListener();
    
    private JudgmentRepository judgmentRepository = mock(JudgmentRepository.class);
    
    
    @Before
    public void setUp() {
        resetJudgmentIndexFlagStepExecutionListener.setJudgmentRepository(judgmentRepository);
    }
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void beforeStep_ALL_JUDGMENTS() throws Exception {
        
        resetJudgmentIndexFlagStepExecutionListener.beforeStep(new StepExecution("stepName", new JobExecution(1L)));
        
        verify(judgmentRepository, times(1)).markAllAsNotIndexed();
        
    }
    
    @Test
    public void beforeStep_ONLY_COMMON_COURT_JUDGMENTS() throws Exception {
        resetJudgmentIndexFlagStepExecutionListener.setSourceCode(SourceCode.COMMON_COURT.name());
        
        resetJudgmentIndexFlagStepExecutionListener.beforeStep(new StepExecution("stepName", new JobExecution(1L)));
        
        verify(judgmentRepository, times(1)).markAsNotIndexedBySourceCode(SourceCode.COMMON_COURT);
    }

}
