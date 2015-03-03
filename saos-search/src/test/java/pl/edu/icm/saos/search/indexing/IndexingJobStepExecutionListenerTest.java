package pl.edu.icm.saos.search.indexing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.StepExecution;

/**
 * @author madryk
 */
public class IndexingJobStepExecutionListenerTest {

    private IndexingJobStepExecutionListener indexingJobStepExecutionListener = new IndexingJobStepExecutionListener();
    
    private JudgmentIndexDeleter judgmentIndexDeleter = mock(JudgmentIndexDeleter.class);
    
    
    private StepExecution stepExecution = mock(StepExecution.class);
    
    
    @Before
    public void setUp() {
        indexingJobStepExecutionListener.setJudgmentIndexDeleter(judgmentIndexDeleter);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void beforeStep() throws SolrServerException, IOException {
        // execute
        indexingJobStepExecutionListener.beforeStep(stepExecution);
        // assert
        verify(judgmentIndexDeleter).deleteFromIndexWithoutCorrespondingJudgmentInDb();
    }
}
