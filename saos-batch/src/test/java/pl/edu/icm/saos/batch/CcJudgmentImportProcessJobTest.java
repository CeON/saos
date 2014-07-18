package pl.edu.icm.saos.batch;

import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Łukasz Dumiszewski
 */

public class CcJudgmentImportProcessJobTest extends BatchTestSupport {

    @Autowired
    private Job ccJudgmentImportProcessJob;
    
    @Autowired
    private JobForcingExecutor jobExecutor;
    
    
    @Test
    public void execute() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        //jobExecutor.forceStartNewJob(ccJudgmentImportProcessJob);
    }
}
