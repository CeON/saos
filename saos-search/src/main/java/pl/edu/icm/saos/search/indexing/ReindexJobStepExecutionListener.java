package pl.edu.icm.saos.search.indexing;

import java.io.IOException;

import org.apache.commons.lang3.EnumUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * Resets all judgments indexed flag before step execution.
 * It can use job parameter <code>sourceCode</code> to limit
 * reseting to judgments with specified {@link SourceCode}.
 * It also deletes judgments from index whose <code>sourceCode</code>
 * is equals to the provided one (all judgments in case of
 * <code>sourceCode</code> equals to null).
 * 
 * @author madryk
 */
@Service
@StepScope
public class ReindexJobStepExecutionListener implements StepExecutionListener {

    @Value("#{jobParameters[sourceCode]}")
    private String sourceCode;
    
    private JudgmentRepository judgmentRepository;
    
    private SolrServer solrJudgmentsServer;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        SourceCode sc = EnumUtils.getEnum(SourceCode.class, sourceCode);
        judgmentRepository.markAsNotIndexedBySourceCode(sc);
        
        String deletingQuery = (sc == null) ? "*:*" : JudgmentIndexField.SOURCE_CODE.getFieldName() + ":" + sc.name();
        
        try {
            solrJudgmentsServer.deleteByQuery(deletingQuery);
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }


    //------------------------ SETTERS --------------------------

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    @Autowired
    @Qualifier("solrJudgmentsServer")
    public void setSolrJudgmentsServer(SolrServer solrJudgmentsServer) {
        this.solrJudgmentsServer = solrJudgmentsServer;
    }

}
