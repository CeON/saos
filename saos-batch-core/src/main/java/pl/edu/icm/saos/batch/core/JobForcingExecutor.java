package pl.edu.icm.saos.batch.core;

import java.util.Date;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.batch.JobName;

import com.google.common.collect.Maps;

/**
 * @author Łukasz Dumiszewski
 * @author madryk
 */
@Service("jobForcingExecutor")
public class JobForcingExecutor {
    
    private JobLauncher jobLauncher;
    
    private JobLocator  jobLocator;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    @Autowired
    public JobForcingExecutor(JobLauncher jobLauncher, JobLocator jobLocator) {
        this.jobLauncher = jobLauncher;
        this.jobLocator = jobLocator;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Forces executing the given job by adding the current time as a job parameter
     */
    public JobExecution forceStartNewJob(Job job)  throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        Map<String, JobParameter> params = Maps.newHashMap();
        params.put("startDate", new JobParameter(new Date()));
        JobExecution execution = jobLauncher.run(job, new JobParameters(params));
        return execution;
    }
    
    /**
     * Forces executing job with given name by adding the current time as a job parameter
     */
    public JobExecution forceStartNewJob(JobName jobName)  throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobException {
        
        Job job = jobLocator.getJob(jobName.name());
        
        return forceStartNewJob(job);
    }
}
