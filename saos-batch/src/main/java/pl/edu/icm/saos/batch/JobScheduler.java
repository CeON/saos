package pl.edu.icm.saos.batch;

import java.util.Date;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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

import pl.edu.icm.saos.importer.commoncourt.download.CcjImportDateFormatter;

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
    private Job judgmentImportJob;
    
    
    @Scheduled(cron="${importJudgments.cron}")
    public void importJudgments() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        
        log.info("Judgment import has started");
        
        Map<String, JobParameter> params = Maps.newHashMap();
        params.put("startDate", new JobParameter(new Date()));
        params.put("customPublicationDateFrom", new JobParameter(new CcjImportDateFormatter().format(new DateTime(2014, 06, 15, 23, 59, DateTimeZone.forID("CET")))));
        JobExecution execution = jobLauncher.run(judgmentImportJob, new JobParameters(params));
        
        log.info("Judgment import has finished, exit status: {}", execution.getStatus());
   
    }

}
