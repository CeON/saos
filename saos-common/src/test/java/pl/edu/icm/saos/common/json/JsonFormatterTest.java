package pl.edu.icm.saos.common.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static pl.edu.icm.saos.common.json.JsonNormalizer.normalizeJson;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author Łukasz Dumiszewski
 */

public class JsonFormatterTest {

    
    private JsonFactory jsonFactory = new MappingJsonFactory();
    
    private JsonFormatter jsonFormatter = new JsonFormatter();
    
        
    @Before
    public void before() throws JsonParseException, IOException {
    }
    
    
       
    @Test
    public void formatCurrentTokenTree() throws JsonParseException, IOException {
        
        // Given
        
        String element1 = normalizeJson("{bre: 'sss', arr: ['1112', 'abc']}");
        
        JsonParser jsonParser = jsonFactory.createParser(element1);
        
        jsonParser.nextToken();
        
        // Execute
        
        String formattedJsonToken = jsonFormatter.formatCurrentTokenTree(jsonParser); 
        
        // Assert
        
        assertEquals(element1, formattedJsonToken);
        
        
     
    }
    
    
    @Test
    public void formatCurrentTokenTree_NullNodeAsNull() throws JsonParseException, IOException {
        
        // Given
        
        String element1 = normalizeJson("{bre: 'sss'}");
        
        JsonParser jsonParser = jsonFactory.createParser(element1);
        
        jsonParser.nextToken();
        jsonParser.nextToken();
        jsonParser.nextToken();
        jsonParser.nextToken();
        
        // Execute & Assert
        
        assertNull(jsonFormatter.formatCurrentTokenTree(jsonParser)); 
        
        
     
    }
    
   
   

    
}
