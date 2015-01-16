package pl.edu.icm.saos.batch.core;

import static org.junit.Assert.assertEquals;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

/**
 * @author Łukasz Dumiszewski
 */

public final class JobExecutionAssertUtils {


    //------------------------ CONSTRUCTORS --------------------------
    
    private JobExecutionAssertUtils() {
        throw new IllegalStateException("may not be instantiated");
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public static void assertJobExecution(JobExecution execution, int skipCount, int writeCount) {
        StepExecution stepExecution = getFirstStepExecution(execution);
        assertEquals(skipCount, stepExecution.getProcessSkipCount());
        assertEquals(writeCount, stepExecution.getWriteCount());
    }

    
    //------------------------ PRIVATE --------------------------
    
    private static StepExecution getFirstStepExecution(JobExecution execution) {
        for (StepExecution stepExecution : execution.getStepExecutions()) {
            return stepExecution;
        }
        return null;
    }
        
}
