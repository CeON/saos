package pl.edu.icm.saos.enrichment.process;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Łukasz Dumiszewski
 */

public class UploadEnrichmentTagProcessorTest {

    
    private UploadEnrichmentTagProcessor uploadEnrichmentTagProcessor = new UploadEnrichmentTagProcessor();
    
    @Mock private UploadEnrichmentTagOverwriter uploadEnrichmentTagOverwriter;
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
        uploadEnrichmentTagProcessor.setUploadEnrichmentTagOverwriter(uploadEnrichmentTagOverwriter);
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void processUploadedEnrichmentTags_enrichmentTagsShouldBeOverwritten() {
        
        // given
        
        when(uploadEnrichmentTagOverwriter.shouldEnrichmentTagsBeOverwritten()).thenReturn(true);
        
        
        // execute
        
        uploadEnrichmentTagProcessor.processUploadedEnrichmentTags();
        
        
        // assert
        
        verify(uploadEnrichmentTagOverwriter).shouldEnrichmentTagsBeOverwritten();
        verify(uploadEnrichmentTagOverwriter).overwriteEnrichmentTags();
        verifyNoMoreInteractions(uploadEnrichmentTagOverwriter);
    }
    
    
    @Test
    public void processUploadedEnrichmentTags_enrichmentTagsShouldNotBeOverwritten() {
        
        // given
        
        when(uploadEnrichmentTagOverwriter.shouldEnrichmentTagsBeOverwritten()).thenReturn(false);
        
        
        // execute
        
        uploadEnrichmentTagProcessor.processUploadedEnrichmentTags();
        
        
        // assert
        
        verify(uploadEnrichmentTagOverwriter).shouldEnrichmentTagsBeOverwritten();
        verifyNoMoreInteractions(uploadEnrichmentTagOverwriter);
    }
    
}
