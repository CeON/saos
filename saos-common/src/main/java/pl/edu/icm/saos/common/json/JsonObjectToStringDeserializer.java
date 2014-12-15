package pl.edu.icm.saos.common.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

/**
 * Json deserializer from json object to json String
 * @author Łukasz Dumiszewski
 */

public final class JsonObjectToStringDeserializer extends StdScalarDeserializer<String> {

    
    private static final long serialVersionUID = 1L;

    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JsonObjectToStringDeserializer() { super(String.class); }

    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
            JsonProcessingException {
        
        if (!JsonToken.START_OBJECT.equals(jp.getCurrentToken()) 
                && !JsonToken.START_ARRAY.equals(jp.getCurrentToken())) {
            throw new JsonParseException("tag value is not a json object or array", jp.getCurrentLocation());
        }
        
        return jp.readValueAsTree().toString();
                
        
    }
    
}