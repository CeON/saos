package pl.edu.icm.saos.importer.notapi.common;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceJudgmentRepository;

/**
 * @author madryk
 */
public class NotApiImportDownloadStepExecutionListenerTest {

    private NotApiImportDownloadStepExecutionListener stepExecutionListener = new NotApiImportDownloadStepExecutionListener();
    
    private RawSourceJudgmentRepository rawSourceJudgmentRepository = mock(RawSourceJudgmentRepository.class);
    
    
    @Before
    public void setUp() {
        stepExecutionListener.setRawJudgmentRepository(rawSourceJudgmentRepository);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void stepBefore() {
        // given
        stepExecutionListener.setRawJudgmentClass(RawSourceScJudgment.class);
        
        // when
        stepExecutionListener.beforeStep(new StepExecution("stepName", new JobExecution(1L)));
        
        // then
        verify(rawSourceJudgmentRepository, times(1)).deleteAllWithClass(RawSourceScJudgment.class);
        verify(rawSourceJudgmentRepository, times(1)).flush();
        verifyNoMoreInteractions(rawSourceJudgmentRepository);
    }
}
