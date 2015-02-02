package pl.edu.icm.saos.enrichment.process;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.UploadEnrichmentTagRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class UploadEnrichmentTagOverwriterTest {

    
    private UploadEnrichmentTagOverwriter uploadEnrichmentTagOverwriter = new UploadEnrichmentTagOverwriter();
    
    @Mock private UploadEnrichmentTagRepository uploadEnrichmentTagRepository;
    
    @Mock private EnrichmentTagRepository enrichmentTagRepository;
    
    @Mock private UploadEnrichmentTagCopier uploadEnrichmentTagCopier;
   
    
    @Before
    public void before() {
        
        initMocks(this);
        
        uploadEnrichmentTagOverwriter.setEnrichmentTagRepository(enrichmentTagRepository);
        uploadEnrichmentTagOverwriter.setUploadEnrichmentTagRepository(uploadEnrichmentTagRepository);
        uploadEnrichmentTagOverwriter.setUploadEnrichmentTagCopier(uploadEnrichmentTagCopier);

    }
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void shouldEnrichmentTagsBeOverwritten_false_uploadTagMaxDate_isNull() {
        
        // given
        
        when(uploadEnrichmentTagRepository.findMaxCreationDate()).thenReturn(null);
        
        // execute
        
        boolean shouldEnrichmentTagsBeOverwriten = uploadEnrichmentTagOverwriter.shouldEnrichmentTagsBeOverwritten();
        
        // assert
        
        assertFalse(shouldEnrichmentTagsBeOverwriten);
        verify(enrichmentTagRepository, never()).findMaxCreationDate();
        
    }
    
    
    @Test
    public void shouldEnrichmentTagsBeOverwritten_true_uploadTagMaxDate_isNotNull_tagMaxDate_isNull() {
        
        // given
        
        when(uploadEnrichmentTagRepository.findMaxCreationDate()).thenReturn(new DateTime(2012, 12, 12, 12, 13));
        when(enrichmentTagRepository.findMaxCreationDate()).thenReturn(null);
        
        // execute
        
        boolean shouldEnrichmentTagsBeOverwriten = uploadEnrichmentTagOverwriter.shouldEnrichmentTagsBeOverwritten();
        
        // assert
        
        assertTrue(shouldEnrichmentTagsBeOverwriten);
        
    }
    
    
    @Test
    public void shouldEnrichmentTagsBeOverwritten_true_uploadTagMaxDate_isNotNull_tagMaxDate_isBefore() {
        
        // given
        
        when(uploadEnrichmentTagRepository.findMaxCreationDate()).thenReturn(new DateTime(2012, 12, 12, 12, 13));
        when(enrichmentTagRepository.findMaxCreationDate()).thenReturn(new DateTime(2012, 12, 12, 12, 11, 23));
        
        // execute
        
        boolean shouldEnrichmentTagsBeOverwriten = uploadEnrichmentTagOverwriter.shouldEnrichmentTagsBeOverwritten();
        
        // assert
        
        assertTrue(shouldEnrichmentTagsBeOverwriten);
        
    }
    
    
    @Test
    public void shouldEnrichmentTagsBeOverwritten_true_uploadTagMaxDate_isNotNull_tagMaxDate_isAfter() {
        
        // given
        
        when(uploadEnrichmentTagRepository.findMaxCreationDate()).thenReturn(new DateTime(2012, 12, 12, 12, 13));
        when(enrichmentTagRepository.findMaxCreationDate()).thenReturn(new DateTime(2012, 12, 13, 12, 11, 23));
        
        // execute
        
        boolean shouldEnrichmentTagsBeOverwriten = uploadEnrichmentTagOverwriter.shouldEnrichmentTagsBeOverwritten();
        
        // assert
        
        assertFalse(shouldEnrichmentTagsBeOverwriten);
        
        
    }
    
    
    @Test
    public void overwriteEnrichmentTags() {


        // execute
        
        uploadEnrichmentTagOverwriter.overwriteEnrichmentTags();
        
        // assert
        
        verify(enrichmentTagRepository).deleteAllInBatch();
        verify(uploadEnrichmentTagCopier).copyUploadedEnrichmentTags();
        
        verifyZeroInteractions(uploadEnrichmentTagRepository);
        
        verifyNoMoreInteractions(enrichmentTagRepository, uploadEnrichmentTagCopier);
    }
    
    
    
}
