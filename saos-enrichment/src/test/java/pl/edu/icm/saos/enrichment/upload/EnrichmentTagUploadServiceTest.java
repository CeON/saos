package pl.edu.icm.saos.enrichment.upload;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_INVALID_JSON_FORMAT;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_IO;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_SAME_TAG_ALREADY_UPLOADED;

import java.io.IOException;
import java.io.InputStream;

import javax.validation.ValidationException;

import org.apache.commons.fileupload.util.LimitedInputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;

import pl.edu.icm.saos.common.json.JsonObjectIterator;
import pl.edu.icm.saos.common.service.ServiceException;
import pl.edu.icm.saos.common.testcommon.ServiceExceptionMatcher;
import pl.edu.icm.saos.persistence.enrichment.UploadEnrichmentTagRepository;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.googlecode.catchexception.CatchException;
import com.googlecode.catchexception.apis.CatchExceptionAssertJ;

/**
 * @author Łukasz Dumiszewski
 */

public class EnrichmentTagUploadServiceTest {

    
    
    private EnrichmentTagUploadService enrichmentTagUploadService = new EnrichmentTagUploadService();
    
    
    @Mock private JsonFactory jsonFactory;
    
    @Mock private JsonObjectIterator jsonObjectIterator;
    
    @Mock private EnrichmentTagItemUploadProcessor enrichmentTagItemUploadProcessor;
    
    @Mock private JsonParser jsonParser;
    
    @Mock private UploadEnrichmentTagRepository uploadEnrichmentTagRepository;
    
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
        enrichmentTagUploadService.setJsonFactory(jsonFactory);
        
        enrichmentTagUploadService.setJsonObjectIterator(jsonObjectIterator);
        
        enrichmentTagUploadService.setEnrichmentTagItemUploadProcessor(enrichmentTagItemUploadProcessor);
        
        enrichmentTagUploadService.setUploadEnrichmentTagRepository(uploadEnrichmentTagRepository);
        
    }
    
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test
    public void uploadEnrichmentTags() throws Exception {
        
        // given
        
        InputStream inputStream = mock(InputStream.class);
        EnrichmentTagItem enrichmentTagItem = mock(EnrichmentTagItem.class);
        
        when(jsonFactory.createParser(Mockito.any(InputStream.class))).thenReturn(jsonParser);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonObjectIterator.nextJsonObject(jsonParser, EnrichmentTagItem.class)).thenReturn(enrichmentTagItem, (EnrichmentTagItem)null);
        
        
        // execute
        
        enrichmentTagUploadService.uploadEnrichmentTags(inputStream);
        
        
        // verify
        
        InOrder inOrder = Mockito.inOrder(uploadEnrichmentTagRepository, jsonFactory, jsonObjectIterator, enrichmentTagItemUploadProcessor);
        inOrder.verify(uploadEnrichmentTagRepository).truncate();
        
        ArgumentCaptor<InputStream> inputStreamArg = ArgumentCaptor.forClass(InputStream.class);
        inOrder.verify(jsonFactory).createParser(inputStreamArg.capture());
        assertTrue(inputStreamArg.getValue() instanceof LimitedInputStream);
        
        inOrder.verify(jsonObjectIterator).nextJsonObject(jsonParser, EnrichmentTagItem.class);
        
        ArgumentCaptor<EnrichmentTagItem> tagItemToProcessArg = ArgumentCaptor.forClass(EnrichmentTagItem.class);
        inOrder.verify(enrichmentTagItemUploadProcessor).processEnrichmentTagItem(tagItemToProcessArg.capture());
        assertTrue(enrichmentTagItem == tagItemToProcessArg.getValue());

        inOrder.verify(jsonObjectIterator).nextJsonObject(jsonParser, EnrichmentTagItem.class);
        
        verifyNoMoreInteractions(uploadEnrichmentTagRepository, jsonFactory, jsonObjectIterator, enrichmentTagItemUploadProcessor);
        
        
               
    }
    
    
    
    
    @Test
    public void uploadEnrichmentTags_NoTagArray() throws Exception {
        
        // given
        
        InputStream inputStream = mock(InputStream.class);
        
        when(jsonFactory.createParser(Mockito.any(InputStream.class))).thenReturn(jsonParser);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        
        
        // execute & assert
        
        
        CatchExceptionAssertJ.when(enrichmentTagUploadService).uploadEnrichmentTags(inputStream);
        
        assertExceptionMainMessage(ERROR_INVALID_JSON_FORMAT);
                
    }
    

    @Test
    public void uploadEnrichmentTags_IOError() throws Exception {
        
        // given
        
        InputStream inputStream = mock(InputStream.class);
        
        Mockito.doThrow(IOException.class).when(jsonFactory).createParser(Mockito.any(InputStream.class));
        
        
        // execute & assert
        
        CatchExceptionAssertJ.when(enrichmentTagUploadService).uploadEnrichmentTags(inputStream);
        
        assertExceptionMainMessage(ERROR_IO);
          
    }
    
    
    @Test
    public void uploadEnrichmentTags_DataIntegrityViolationError() throws Exception {
        
        // given
        
        InputStream inputStream = mock(InputStream.class);
        EnrichmentTagItem enrichmentTagItem = mock(EnrichmentTagItem.class);
        
        when(jsonFactory.createParser(Mockito.any(InputStream.class))).thenReturn(jsonParser);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonObjectIterator.nextJsonObject(jsonParser, EnrichmentTagItem.class)).thenReturn(enrichmentTagItem, (EnrichmentTagItem)null);
        Mockito.doThrow(DataIntegrityViolationException.class).when(enrichmentTagItemUploadProcessor).processEnrichmentTagItem(enrichmentTagItem);;
        
        
        
        // execute & assert
        
        CatchExceptionAssertJ.when(enrichmentTagUploadService).uploadEnrichmentTags(inputStream);
        
        assertExceptionMainMessage(ERROR_SAME_TAG_ALREADY_UPLOADED);
          
    }
    
    
    
    @Test
    public void uploadEnrichmentTags_ValidationError() throws Exception {
        
        // given
        
        InputStream inputStream = mock(InputStream.class);
        EnrichmentTagItem enrichmentTagItem = mock(EnrichmentTagItem.class);
        
        when(jsonFactory.createParser(Mockito.any(InputStream.class))).thenReturn(jsonParser);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonObjectIterator.nextJsonObject(jsonParser, EnrichmentTagItem.class)).thenReturn(enrichmentTagItem, (EnrichmentTagItem)null);
            
        Mockito.doThrow(ValidationException.class).when(enrichmentTagItemUploadProcessor).processEnrichmentTagItem(enrichmentTagItem);
        
        
        // execute & assert
        
        CatchExceptionAssertJ.when(enrichmentTagUploadService).uploadEnrichmentTags(inputStream);
        
        assertExceptionMainMessage(EnrichmentTagUploadResponseMessages.ERROR_INVALID_DATA);
          
    }
    
    
    
    @Test
    public void uploadEnrichmentTags_ParseError() throws Exception {
        
        // given
        
        InputStream inputStream = mock(InputStream.class);
        
        Mockito.doThrow(JsonParseException.class).when(jsonFactory).createParser(Mockito.any(InputStream.class));
        
        
        // execute & assert
        
        CatchExceptionAssertJ.when(enrichmentTagUploadService).uploadEnrichmentTags(inputStream);
        
        assertExceptionMainMessage(ERROR_INVALID_JSON_FORMAT);
          
    }



    //------------------------ PRIVATE --------------------------
    
    private void assertExceptionMainMessage(String expectedMainMessage) {
        
        CatchExceptionAssertJ.then(CatchException.caughtException())
                             .isExactlyInstanceOf(ServiceException.class);
        assertThat((ServiceException) CatchException.caughtException(), ServiceExceptionMatcher.hasMainMessage(expectedMainMessage));
    }
}
