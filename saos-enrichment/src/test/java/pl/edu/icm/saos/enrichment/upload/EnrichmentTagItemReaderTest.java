package pl.edu.icm.saos.enrichment.upload;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.common.json.JsonUtilService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

/**
 * @author Łukasz Dumiszewski
 */

public class EnrichmentTagItemReaderTest {

    private EnrichmentTagItemReader enrichmentTagItemReader = new EnrichmentTagItemReader();
    
    @Mock private JsonUtilService jsonUtilService;
    
    @Mock private JsonItemParser<EnrichmentTagItem> enrichmentTagItemParser;
    
    @Mock private JsonParser jsonParser;
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
        enrichmentTagItemReader.setEnrichmentTagItemParser(enrichmentTagItemParser);
        
        enrichmentTagItemReader.setJsonUtilService(jsonUtilService);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test
    public void nextEnrichmentTagItem_null() throws JsonParseException, IllegalStateException, IOException {
        
        // given
        
        when(jsonUtilService.nextNode(jsonParser)).thenReturn(null);
        
        
        // execute
        
        EnrichmentTagItem enrichmentTagItem = enrichmentTagItemReader.nextEnrichmentTagItem(jsonParser);
        
        
        // assert
        
        assertNull(enrichmentTagItem);
        verifyZeroInteractions(enrichmentTagItemParser);
        
        
    }
    
    
    
    @Test
    public void nextEnrichmentTagItem() throws JsonParseException, IllegalStateException, IOException {
        
        // given
        
        String jsonNode = "JSON NODE";
        when(jsonUtilService.nextNode(jsonParser)).thenReturn(jsonNode);
        
        EnrichmentTagItem enrichmentTagItem = Mockito.mock(EnrichmentTagItem.class);
        when(enrichmentTagItemParser.parse(jsonNode)).thenReturn(enrichmentTagItem);
        
        // execute
        
        EnrichmentTagItem retEnrichmentTagItem = enrichmentTagItemReader.nextEnrichmentTagItem(jsonParser);
        
        
        // assert
        
        assertTrue(retEnrichmentTagItem == enrichmentTagItem);
        verifyZeroInteractions(enrichmentTagItem);
        
        
    }
    
    
}
