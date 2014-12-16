package pl.edu.icm.saos.enrichment.upload;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * @author Łukasz Dumiszewski
 */

public class EnrichmentTagItemUploadProcessorTest {

    
    private EnrichmentTagItemUploadProcessor enrichmentTagItemUploadProcessor = new EnrichmentTagItemUploadProcessor();
    
    
    @Mock private EnrichmentTagItemConverter enrichmentTagItemConverter;
    
    @Mock private EnrichmentTagRepository enrichmentTagRepository;
    
    
    @Before
    public void before() {
        
        MockitoAnnotations.initMocks(this);
        
        enrichmentTagItemUploadProcessor.setEnrichmentTagItemConverter(enrichmentTagItemConverter);
        
        enrichmentTagItemUploadProcessor.setEnrichmentTagRepository(enrichmentTagRepository);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void processEnrichmentTagItem() {
        
        // given
        
        EnrichmentTagItem enrichmentTagItem = mock(EnrichmentTagItem.class);
        EnrichmentTag enrichmentTag = mock(EnrichmentTag.class);
        
        when(enrichmentTagItemConverter.convertEnrichmentTagItem(enrichmentTagItem)).thenReturn(enrichmentTag);
        
        
        // execute
        
        enrichmentTagItemUploadProcessor.processEnrichmentTagItem(enrichmentTagItem);
        
        
        // verify
        
        ArgumentCaptor<EnrichmentTag> savedEnrichmentTag = ArgumentCaptor.forClass(EnrichmentTag.class);
        verify(enrichmentTagRepository).saveAndFlush(savedEnrichmentTag.capture());
        assertTrue(enrichmentTag == savedEnrichmentTag.getValue());

    }
    
}
