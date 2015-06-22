package pl.edu.icm.saos.enrichment.process;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import pl.edu.icm.saos.batch.core.JobForcingExecutor;
import pl.edu.icm.saos.common.batch.JobName;

/**
 * @author Łukasz Dumiszewski
 */

public class UploadEnrichmentTagProcessorTest {

    
    private UploadEnrichmentTagProcessor uploadEnrichmentTagProcessor = new UploadEnrichmentTagProcessor();
    
    @Mock private UploadEnrichmentTagOverwriter uploadEnrichmentTagOverwriter;
    
    @Mock private JobForcingExecutor jobForcingExecutor;
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
        uploadEnrichmentTagProcessor.setUploadEnrichmentTagOverwriter(uploadEnrichmentTagOverwriter);
        uploadEnrichmentTagProcessor.setJobExecutor(jobForcingExecutor);
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void processUploadedEnrichmentTags_enrichmentTagsShouldBeOverwritten() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobException {
        
        // given
        
        when(uploadEnrichmentTagOverwriter.shouldEnrichmentTagsBeOverwritten()).thenReturn(true);
        
        
        // execute
        
        uploadEnrichmentTagProcessor.processUploadedEnrichmentTags();
        
        
        // assert
        
        verify(uploadEnrichmentTagOverwriter).overwriteEnrichmentTags();
        verify(jobForcingExecutor).forceStartNewJob(JobName.TAG_POST_UPLOAD_PROCESSING);
        verifyNoMoreInteractions(uploadEnrichmentTagOverwriter, jobForcingExecutor);
    }
    
    
    
    
}
