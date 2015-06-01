package pl.edu.icm.saos.enrichment.hash;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.JudgmentEnrichmentHashRepository;

/**
 * @author madryk
 */
@Service
public class MarkProcessedJobExecutionListener implements JobExecutionListener {

    @Autowired
    private JudgmentEnrichmentHashRepository judgmentEnrichmentHashRepository;
    
    @Override
    public void beforeJob(JobExecution jobExecution) {
        
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            judgmentEnrichmentHashRepository.markAllAsProcessed();
        }
    }

    
}
