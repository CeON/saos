package pl.edu.icm.saos.enrichment.process;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.batch.core.JobForcingExecutor;
import pl.edu.icm.saos.common.batch.JobName;
import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;

/**
 * A service for processing {@link UploadEnrichmentTag}s
 * @author Łukasz Dumiszewski
 */
@Service("uploadEnrichmentTagProcessor")
public class UploadEnrichmentTagProcessor {

    private UploadEnrichmentTagOverwriter uploadEnrichmentTagOverwriter;
    
    private JobForcingExecutor jobExecutor;
    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Processes the uploaded enrichment tags ({@link UploadedEnrichmentTag}) <br/> 
     * Uses {@link UploadEnrichmentTagOverwriter#overwriteEnrichmentTags()} internally
     * 
     */
    public void processUploadedEnrichmentTags() {
        
        uploadEnrichmentTagOverwriter.overwriteEnrichmentTags();
        
        runTagPostUploadProcessingJob();
        
    }

    
    //------------------------ PRIVATE --------------------------
    
    private void runTagPostUploadProcessingJob() {
        try {
            jobExecutor.forceStartNewJob(JobName.TAG_POST_UPLOAD_PROCESSING);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException
                | NoSuchJobException e) {
            throw new RuntimeException(e);
        }
    }
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setUploadEnrichmentTagOverwriter(UploadEnrichmentTagOverwriter uploadEnrichmentTagOverwriter) {
        this.uploadEnrichmentTagOverwriter = uploadEnrichmentTagOverwriter;
    }

    @Autowired
    public void setJobExecutor(JobForcingExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }
    
}
