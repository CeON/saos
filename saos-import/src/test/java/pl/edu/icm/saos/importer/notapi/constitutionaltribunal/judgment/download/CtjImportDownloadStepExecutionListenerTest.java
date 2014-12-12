package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.download;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import pl.edu.icm.saos.persistence.repository.RawSourceCtJudgmentRepository;

/**
 * @author madryk
 */
public class CtjImportDownloadStepExecutionListenerTest {

    private CtjImportDownloadStepExecutionListener stepExecutionListener = new CtjImportDownloadStepExecutionListener();
    
    private RawSourceCtJudgmentRepository rawSourceCtJudgmentRepository = mock(RawSourceCtJudgmentRepository.class);
    
    
    @Before
    public void setUp() {
        stepExecutionListener.setRawSourceCtJudgmentRepository(rawSourceCtJudgmentRepository);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void stepBefore() {
        
        stepExecutionListener.beforeStep(new StepExecution("stepName", new JobExecution(1L)));
        
        verify(rawSourceCtJudgmentRepository, times(1)).deleteAll();
        verify(rawSourceCtJudgmentRepository, times(1)).flush();
        verifyNoMoreInteractions(rawSourceCtJudgmentRepository);
    }
}
