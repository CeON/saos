package pl.edu.icm.saos.common.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static pl.edu.icm.saos.common.json.JsonNormalizer.normalizeJson;

import java.io.IOException;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author Łukasz Dumiszewski
 */

public class JsonObjectIteratorTest {

    
    private JsonFactory jsonFactory = new MappingJsonFactory();
    
    private JsonObjectIterator jsonObjectIterator = new JsonObjectIterator();
    
        
    @Before
    public void before() throws JsonParseException, IOException {
    }
    
    
    @Test
    public void nextObject_ClassObject() throws JsonParseException, IOException {
        
        // Given
        String content = normalizeJson("{name: 'sss', somelist:['1112', 'abc']}");
        
        JsonParser jsonParser = jsonFactory.createParser(content);
        
        
        // Execute
        JsonItem jsonItem = jsonObjectIterator.nextJsonObject(jsonParser, JsonItem.class);

        // Assert
        assertNotNull(jsonItem);
        assertEquals("sss", jsonItem.getName());
        assertThat(jsonItem.getSomelist(), Matchers.contains("1112", "abc"));
        
        
        // Execute 2
        assertNull(jsonObjectIterator.nextJsonObject(jsonParser, JsonItem.class));
    }
    
    
    @Test
    public void nextJsonObject_ArrayOfElements() throws JsonParseException, IOException {
        
        // Given
        String element1 = normalizeJson("{bre: 'sss', arr: ['1112', 'abc']}");
        String element2 = normalizeJson("{bre: 'sss', arr: ['1112', 'abc'], dd: 'ssssxx'}");
        
        String content = "[" + element1 +"," + element2 + "]";
        
        JsonParser jsonParser = jsonFactory.createParser(content);
        
        
        // Execute
        JsonToken jsonToken = jsonObjectIterator.nextJsonObject(jsonParser);
        
        // Assert
        assertEquals(content, jsonParser.readValueAsTree().toString());

        
        
        // Execute 2. time
        jsonToken = jsonObjectIterator.nextJsonObject(jsonParser);
        
        // Assert
        assertNull(jsonToken);
        
        
     
    }
    
    @Test
    public void nextJsonObject_SimpleToken() throws JsonParseException, IOException {
        
        // Given
        String element1 = normalizeJson("{bre: 'sss', arr: ['1112', 'abc']}");
        String element2 = normalizeJson("{bre: 'sss', arr: ['1112', 'abc'], dd: 'ssssxx'}");
        
        String content = "[" + element1 +", " + element2 + "]";
        
        JsonParser jsonParser = jsonFactory.createParser(content);
        
        jsonParser.nextToken(); // array
        jsonParser.nextToken(); // object - element1
        
        // Execute
        JsonToken jsonToken = jsonObjectIterator.nextJsonObject(jsonParser);  // fieldName (of element1) is a simple token not object
        
        // Assert
        assertNull(jsonToken);
        
        
     
    }
    
    
    
    @Test
    public void nextJsonObject_SingleElement() throws JsonParseException, IOException {
        
        // Given
        String content = normalizeJson("{bre: 'sss', arr:['1112', 'abc']}");
        
        JsonParser jsonParser = jsonFactory.createParser(content);
        
        
        // Execute
        JsonToken jsonToken = jsonObjectIterator.nextJsonObject(jsonParser);

        // Assert
        assertNotNull(jsonToken);
        assertEquals(content, jsonParser.readValueAsTree().toString());
        
        
        // Execute 2
        assertNull(jsonObjectIterator.nextJsonObject(jsonParser));
    }
    
    
    
    @Test
    public void isJsonObject_jsonObject() throws JsonParseException, IOException {
        
        // Given
        
        String element1 = normalizeJson("{bre: 'sss', arr: ['1112', 'abc']}");
        
        JsonParser jsonParser = jsonFactory.createParser(element1);
        
        JsonToken jsonToken = jsonParser.nextToken();
        
        // Execute & Assert
        
        assertTrue(jsonObjectIterator.isJsonObject(jsonToken)); 
        
     
    }
    
    
    @Test
    public void isJsonObject_jsonArray() throws JsonParseException, IOException {
        
        // Given
        
        String element1 = normalizeJson("[{bre: 'sss', arr: ['1112', 'abc']}]");
        
        JsonParser jsonParser = jsonFactory.createParser(element1);
        
        JsonToken jsonToken = jsonParser.nextToken();
        
        // Execute & Assert
        
        assertTrue(jsonObjectIterator.isJsonObject(jsonToken)); 
        
     
    }
    

    @Test
    public void isJsonObject_simpleToken() throws JsonParseException, IOException {
        
        // Given
        
        String element1 = normalizeJson("{bre: 'sss', arr: ['1112', 'abc']}");
        
        JsonParser jsonParser = jsonFactory.createParser(element1);

        jsonParser.nextToken();
        JsonToken jsonToken = jsonParser.nextToken();
        
        // Execute & Assert
        
        assertFalse(jsonObjectIterator.isJsonObject(jsonToken)); 
        
     
    }
    
    
    
    public static class JsonItem {
        
        private String name;
        private List<String> somelist;
        
        
        //------------------------ GETTERS --------------------------
        
        public String getName() {
            return name;
        }
        
        public List<String> getSomelist() {
            return somelist;
        }
        
        
        //------------------------ SETTERS --------------------------
        
        public void setName(String name) {
            this.name = name;
        }
        
        public void setSomelist(List<String> somelist) {
            this.somelist = somelist;
        }
        
    }

    
}
