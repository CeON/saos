package pl.edu.icm.saos.search.indexing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.apache.solr.client.solrj.SolrServer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
public class ReindexJobStepExecutionListenerTest {

    private ReindexJobStepExecutionListener reindexJobStepExecutionListener = new ReindexJobStepExecutionListener();
    
    private JudgmentRepository judgmentRepository = mock(JudgmentRepository.class);
    
    private SolrServer judgmentSolrServer = mock(SolrServer.class);
    
    
    @Before
    public void setUp() {
        reindexJobStepExecutionListener.setJudgmentRepository(judgmentRepository);
        reindexJobStepExecutionListener.setSolrJudgmentsServer(judgmentSolrServer);
    }
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void beforeStep_ALL_JUDGMENTS() throws Exception {
        // execute
        reindexJobStepExecutionListener.beforeStep(new StepExecution("stepName", new JobExecution(1L)));
        
        // assert
        verify(judgmentRepository, times(1)).markAsNotIndexedBySourceCode(null);
        verify(judgmentSolrServer).deleteByQuery("*:*");
        verifyNoMoreInteractions(judgmentRepository, judgmentSolrServer);
        
    }
    
    @Test
    public void beforeStep_ONLY_COMMON_COURT_JUDGMENTS() throws Exception {
        // given
        reindexJobStepExecutionListener.setSourceCode(SourceCode.COMMON_COURT.name());
        
        // execute
        reindexJobStepExecutionListener.beforeStep(new StepExecution("stepName", new JobExecution(1L)));
        
        // assert
        verify(judgmentRepository, times(1)).markAsNotIndexedBySourceCode(SourceCode.COMMON_COURT);
        verifyNoMoreInteractions(judgmentRepository);
        verifyZeroInteractions(judgmentSolrServer);
    }

}
