package pl.edu.icm.saos.enrichment.upload;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.InputStream;

import org.apache.commons.fileupload.util.LimitedInputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

/**
 * @author Łukasz Dumiszewski
 */

public class EnrichmentTagUploadServiceTest {

    
    
    private EnrichmentTagUploadService enrichmentTagUploadService = new EnrichmentTagUploadService();
    
    
    @Mock private JsonFactory jsonFactory;
    
    @Mock private EnrichmentTagItemReader enrichmentTagItemReader;
    
    @Mock private EnrichmentTagItemUploadProcessor enrichmentTagItemUploadProcessor;
    
    @Mock private JsonParser jsonParser;
    
    
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
        enrichmentTagUploadService.setJsonFactory(jsonFactory);
        
        enrichmentTagUploadService.setEnrichmentTagItemReader(enrichmentTagItemReader);
        
        enrichmentTagUploadService.setEnrichmentTagItemUploadProcessor(enrichmentTagItemUploadProcessor);
        
    }
    
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test
    public void uploadEnrichmentTags() throws Exception {
        
        // given
        
        InputStream inputStream = mock(InputStream.class);
        EnrichmentTagItem enrichmentTagItem = mock(EnrichmentTagItem.class);
        
        when(jsonFactory.createParser(Mockito.any(InputStream.class))).thenReturn(jsonParser);
        when(enrichmentTagItemReader.nextEnrichmentTagItem(jsonParser)).thenReturn(enrichmentTagItem, (EnrichmentTagItem)null);
        
        
        // execute
        
        enrichmentTagUploadService.uploadEnrichmentTags(inputStream);
        
        
        // verify
        
        ArgumentCaptor<EnrichmentTagItem> tagItemToProcessArg = ArgumentCaptor.forClass(EnrichmentTagItem.class);
        verify(enrichmentTagItemUploadProcessor).processEnrichmentTagItem(tagItemToProcessArg.capture());
        assertTrue(enrichmentTagItem == tagItemToProcessArg.getValue());

        verifyNoMoreInteractions(enrichmentTagItemUploadProcessor);
        
        
        ArgumentCaptor<InputStream> inputStreamArg = ArgumentCaptor.forClass(InputStream.class);
        verify(jsonFactory).createParser(inputStreamArg.capture());
        assertTrue(inputStreamArg.getValue() instanceof LimitedInputStream);

        
        
    }
    
}
