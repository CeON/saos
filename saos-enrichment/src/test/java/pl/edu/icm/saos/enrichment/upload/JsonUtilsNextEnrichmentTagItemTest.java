package pl.edu.icm.saos.enrichment.upload;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.json.JsonObjectIterator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author Łukasz Dumiszewski
 */

public class JsonUtilsNextEnrichmentTagItemTest {

    
    private JsonObjectIterator jsonUtils = new JsonObjectIterator();
    
    private JsonParser jsonParser;
    
    private static String jsonContent = "{judgmentId:'123', /* comment should be passed */ "+
            "tagType:'SIMILAR_JUDGMENTS', "+
            "value: {" +
                      "key1:'value1'," +
                      "key2:{innerKey1:'innerValue1'}" +
                    "}"+
                  "}";
           
    

    @Before
    public void before() throws JsonParseException, IOException {
        MockitoAnnotations.initMocks(this);

        jsonContent = JsonNormalizer.normalizeJson(jsonContent);
        jsonParser = new MappingJsonFactory().createParser(jsonContent);
    }
    
    
    @Test
    public void nextJsonObject() throws IOException {
        
        // execute
        EnrichmentTagItem enrichmentTagItem = jsonUtils.nextJsonObject(jsonParser, EnrichmentTagItem.class);
        
        
        // assert
        
        assertEquals(123, enrichmentTagItem.getJudgmentId());
        assertEquals("SIMILAR_JUDGMENTS", enrichmentTagItem.getTagType());   
        assertEquals("{" +
                      "\"key1\":\"value1\"," +
                      "\"key2\":{\"innerKey1\":\"innerValue1\"}" +
                    "}", enrichmentTagItem.getValue());
    }
    
    
   
    
    
    
    
}
