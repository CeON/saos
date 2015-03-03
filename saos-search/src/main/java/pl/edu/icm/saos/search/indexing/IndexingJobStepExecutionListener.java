package pl.edu.icm.saos.search.indexing;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author madryk
 */
@Service
public class IndexingJobStepExecutionListener implements StepExecutionListener {
    
    private final static Logger log = LoggerFactory.getLogger(IndexingJobStepExecutionListener.class);
    
    
    private JudgmentIndexDeleter judgmentIndexDeleter;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        
        
        try {
            log.info("Deleting judgments from index without corresponding judgment in database");
            
            judgmentIndexDeleter.deleteFromIndexWithoutCorrespondingJudgmentInDb();
            
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
        
    }

    
    //------------------------ SETTERS --------------------------
    
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

    @Autowired
    public void setJudgmentIndexDeleter(JudgmentIndexDeleter judgmentIndexDeleter) {
        this.judgmentIndexDeleter = judgmentIndexDeleter;
    }


}
