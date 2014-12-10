package pl.edu.icm.saos.common.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

/**
 * Json deserializer from json object to json String
 * @author Łukasz Dumiszewski
 */

public final class NodeToStringDeserializer extends StdScalarDeserializer<String> {

    
    private static final long serialVersionUID = 1L;

    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public NodeToStringDeserializer() { super(String.class); }

    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
            JsonProcessingException {
        
        return JsonUtils.nextNode(jp);
                
        
    }
    
}