package pl.edu.icm.saos.batch;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

/**
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
    private Job ccJudgmentImportProcessJob;
    
    @Autowired
    private JobForcingExecutor jobExecutor;
    
    
 
    @Scheduled(cron="${import.commonCourt.judgments.cron}")
    public void importCcJudgments() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        
        log.info("Judgment import has started");
        
        Map<String, JobParameter> params = Maps.newHashMap();
        params.put("startDate", new JobParameter(new Date()));
        //params.put("customPublicationDateFrom", new JobParameter(new CcjImportDateFormatter().format(new DateTime(2014, 05, 01, 23, 59, DateTimeZone.forID("Europe/Warsaw")))));
        JobExecution execution = jobLauncher.run(ccJudgmentImportJob, new JobParameters(params));
        
        log.info("Judgment import has finished, exit status: {}", execution.getStatus());
   
    }
    

    @Scheduled(cron="${import.commonCourt.judgments.processRawSourceCcJudgments.cron}")
    public void processRawCcJudgments() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        
        log.info("Judgment import processing has started");
        
        JobExecution execution = jobExecutor.forceStartNewJob(ccJudgmentImportProcessJob);
        
        log.info("Judgment import processing has finished, exit status: {}", execution.getStatus());
   
    }

    @Autowired
    private Job commonCourtImportJob;
    
    
    @Scheduled(cron="${import.commonCourt.courts.cron}")
    public void importCommonCourts() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        
        log.info("Common court import has started");
        
        JobExecution execution = jobExecutor.forceStartNewJob(commonCourtImportJob);
        
        log.info("Common court import has finished, exit status: {}", execution.getStatus());
   
    }

    @Autowired
    private Job ccJudgmentIndexingJob;
    
    @Scheduled(cron="${indexing.judgments.cron}")
    public void indexCommonCourtJudgments() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        
        log.info("Common court judgments indexing has started");
        
        JobExecution execution = jobExecutor.forceStartNewJob(ccJudgmentIndexingJob);
        
        log.info("Common court judgments indexing has finished, exit status: {}", execution.getStatus());
    }
    

}
