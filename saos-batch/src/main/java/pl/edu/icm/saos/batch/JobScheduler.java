package pl.edu.icm.saos.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Łukasz Dumiszewski
 */
@Service
public class JobScheduler {
    
    private static Logger log = LoggerFactory.getLogger(JobScheduler.class);
    
    
    @Autowired
    private JobLauncher jobLauncher;
   
    @Autowired
    public Job ccJudgmentImportJob;
    
    @Autowired
    private JobForcingExecutor jobExecutor;
    
    
    
    //------------------------ LOGIC --------------------------
 
    
    @Scheduled(cron="${import.commonCourt.judgments.cron}")
    public void importCcJudgments() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        
        log.debug("Judgment import has started");
        
        JobExecution execution = jobExecutor.forceStartNewJob(ccJudgmentImportJob);
        
        log.debug("Judgment import has finished, exit status: {}", execution.getStatus());
   
    }
    
    
}
