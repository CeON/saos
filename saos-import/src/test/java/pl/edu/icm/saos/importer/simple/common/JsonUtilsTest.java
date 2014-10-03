package pl.edu.icm.saos.importer.simple.common;

import static org.junit.Assert.assertEquals;

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

public class JsonUtilsTest {

    
    JsonFactory jsonFactory = new MappingJsonFactory();
            
    private JsonUtils jsonUtils = new JsonUtils();
    
    
    @Before
    public void before() throws JsonParseException, IOException {
    }
    
    
    @Test
    public void nextNode_SingleElement() throws JsonParseException, IOException {
        
        // Given
        String content = "{\"bre\":\"sss\", \"arr\":[\"1112\", \"abc\"]}";
        JsonParser jsonParser = jsonFactory.createParser(content);
        
        // Execute
        String nodeStr = jsonUtils.nextNode(jsonParser);

        // Assert
        assertEquals(content.replaceAll("\\s", ""), nodeStr);
    }
    
    
    @Test
    public void nextNode_ArrayOfElements() throws JsonParseException, IOException {
        
        // Given
        String element1 = "{\"bre\":\"sss\", \"arr\":[\"1112\", \"abc\"]}";
        String element2 = "{\"bre\":\"sss\", \"arr\":[\"112\", \"abc\"], \"dd\":\"dddd\"}";
        String content = "[" + element1 +", " + element2 + "]";
        
        JsonParser jsonParser = jsonFactory.createParser(content);
        
        
        // Execute
        String nodeStr = jsonUtils.nextNode(jsonParser);
        
        // Assert
        assertEquals(element1.replaceAll("\\s", ""), nodeStr);

        
        
        // Execute 2. time
        nodeStr = jsonUtils.nextNode(jsonParser);
        
        // Assert
        assertEquals(element2.replaceAll("\\s", ""), nodeStr);
    }
    
    
}
