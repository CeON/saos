package pl.edu.icm.saos.batch;

import java.util.Date;
import java.util.Map;

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
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

/**
 * @author Łukasz Dumiszewski
 */

@Service("jobForcingExecutor")
public class JobForcingExecutor {
    
    @Autowired
    private JobLauncher jobLauncher;
   
    /**
     * Forces executing the given job by adding the current time as a job parameter
     */
    public JobExecution forceStartNewJob(Job job)  throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        Map<String, JobParameter> params = Maps.newHashMap();
        params.put("startDate", new JobParameter(new Date()));
        JobExecution execution = jobLauncher.run(job, new JobParameters(params));
        return execution;
    }
}
