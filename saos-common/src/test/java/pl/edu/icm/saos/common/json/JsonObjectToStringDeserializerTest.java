package pl.edu.icm.saos.common.json;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;

/**
 * @author Łukasz Dumiszewski
 */

public class JsonObjectToStringDeserializerTest {

    
    private JsonObjectToStringDeserializer deserializer = new JsonObjectToStringDeserializer();
    
    @Mock private JsonParser jsonParser; 
    
    @Mock private DeserializationContext dContext;

    
    @Before
    public void before() {
        
        initMocks(this);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = JsonParseException.class)
    public void deserialize_NotJsonObject() throws JsonProcessingException, IOException {
        
        // given
        
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        
        
        // execute
        
        deserializer.deserialize(jsonParser, dContext);
       
    }
    

    
    @Test
    public void deserialize() throws JsonProcessingException, IOException {
        
        // given
        
        TreeNode deserializedTreeNode = Mockito.mock(TreeNode.class);
        
        String deserializedValue = "DESERIALIZED";
        
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.readValueAsTree()).thenReturn(deserializedTreeNode);
        when(deserializedTreeNode.toString()).thenReturn(deserializedValue);
        
        
        // execute
        
        String deserializedRetValue = deserializer.deserialize(jsonParser, dContext);
        
        
        // assert
        
        assertEquals(deserializedValue, deserializedRetValue);
    
    }

}
