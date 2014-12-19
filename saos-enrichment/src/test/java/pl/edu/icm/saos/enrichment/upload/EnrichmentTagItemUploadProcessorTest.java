package pl.edu.icm.saos.enrichment.upload;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.validation.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * @author Łukasz Dumiszewski
 */

public class EnrichmentTagItemUploadProcessorTest {

    
    private EnrichmentTagItemUploadProcessor enrichmentTagItemUploadProcessor = new EnrichmentTagItemUploadProcessor();
    
    
    @Mock private EnrichmentTagItemConverter enrichmentTagItemConverter;
    
    @Mock private EnrichmentTagRepository enrichmentTagRepository;
    
    @Mock private CommonValidator commonValidator;
    
    
    
    @Before
    public void before() {
        
        MockitoAnnotations.initMocks(this);
        
        enrichmentTagItemUploadProcessor.setEnrichmentTagItemConverter(enrichmentTagItemConverter);
        
        enrichmentTagItemUploadProcessor.setEnrichmentTagRepository(enrichmentTagRepository);
        
        enrichmentTagItemUploadProcessor.setCommonValidator(commonValidator);
        
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
        
        ArgumentCaptor<EnrichmentTagItem> enrichmentTagItemArg = ArgumentCaptor.forClass(EnrichmentTagItem.class);
        verify(commonValidator).validateEx(enrichmentTagItemArg.capture());
        assertTrue(enrichmentTagItem == enrichmentTagItemArg.getValue());
        
        ArgumentCaptor<EnrichmentTag> enrichmentTagArg = ArgumentCaptor.forClass(EnrichmentTag.class);
        verify(enrichmentTagRepository).saveAndFlush(enrichmentTagArg.capture());
        assertTrue(enrichmentTag == enrichmentTagArg.getValue());
        
        

    }
    
    
    @Test(expected=ValidationException.class)
    public void processEnrichmentTagItem_ValidationError() throws Exception {
        
        // given
        
        EnrichmentTagItem enrichmentTagItem = mock(EnrichmentTagItem.class);
        
        Mockito.doThrow(ValidationException.class).when(commonValidator).validateEx(enrichmentTagItem);
        
        
        // execute
        
        enrichmentTagItemUploadProcessor.processEnrichmentTagItem(enrichmentTagItem);
          
    }
    
    
    
}
