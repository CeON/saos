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

public class JsonUtilsTest {

    
    JsonFactory jsonFactory = new MappingJsonFactory();
            
        
    @Before
    public void before() throws JsonParseException, IOException {
    }
    
    
    @Test
    public void nextNode_SingleElement() throws JsonParseException, IOException {
        
        // Given
        String content = normalizeJson("{bre: 'sss', arr:['1112', 'abc']}");
        
        JsonParser jsonParser = jsonFactory.createParser(content);
        
        
        // Execute
        String nodeStr = JsonUtils.nextNode(jsonParser);

        // Assert
        assertEquals(content.replaceAll("\\s", ""), nodeStr);
    }
    
    
    @Test
    public void nextNode_ArrayOfElements() throws JsonParseException, IOException {
        
        // Given
        String element1 = normalizeJson("{bre: 'sss', arr: ['1112', 'abc']}");
        String element2 = normalizeJson("{bre: 'sss', arr: ['1112', 'abc'], dd: 'ssssxx'}");
        
        String content = "[" + element1 +", " + element2 + "]";
        
        JsonParser jsonParser = jsonFactory.createParser(content);
        
        
        // Execute
        String nodeStr = JsonUtils.nextNode(jsonParser);
        
        // Assert
        assertEquals(element1.replaceAll("\\s", ""), nodeStr);

        
        
        // Execute 2. time
        nodeStr = JsonUtils.nextNode(jsonParser);
        
        // Assert
        assertEquals(element2.replaceAll("\\s", ""), nodeStr);
        
        
        // Execute 3. time
        nodeStr = JsonUtils.nextNode(jsonParser);
        
        // Assert
        assertNull(nodeStr);
        
        
     
    }
    
    
}
