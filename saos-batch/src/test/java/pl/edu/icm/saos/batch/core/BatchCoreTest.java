package pl.edu.icm.saos.batch.core;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Łukasz Dumiszewski
 */

public class BatchCoreTest extends BatchTestSupport {

    @Autowired
    private JobLauncher jobLauncher;
    
    @Autowired
    private Job testJob;
    
    @Test
    public void runJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        JobExecution jobExecution = jobLauncher.run(testJob, new JobParameters());
        Assert.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        
    }
}
