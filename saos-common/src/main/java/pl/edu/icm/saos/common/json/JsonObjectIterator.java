package pl.edu.icm.saos.common.json;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.google.common.base.Preconditions;

/**
 * Contains methods facilitating iterating over the json content. 
 * 
 * @author Łukasz Dumiszewski
 */
@Service
public class JsonObjectIterator {

    
    /**  
     * Returns the next token or null if there is no more tokens or the next token is not a json object or array ({@link #isJsonObject(JsonToken)}).
     * 
     * 
     * @throws IllegalArgumentException if the {@link JsonParser#getCodec()} == null. You can use {@link MappingJsonFactory} for creating
     * parser with default codec
     * @throws JsonParseException if the nextToken cannot be parsed
     * @throws IOException if I/O error occurred
     * 
     * */
    public JsonToken nextJsonObject(JsonParser jsonParser) throws JsonParseException, IOException {
        
        Preconditions.checkArgument(jsonParser.getCodec() != null);
        
        JsonToken token = jsonParser.nextToken();
        
        if (isJsonObject(token)) {
        
            return token;
        
        } else {
            
            return null;
            
        }
            
    }
    
    /**
     * Returns the next json object as object of the given objectClass.
     * <ul>Invokes:
     * <li>{@link #nextJsonObject(JsonParser)}</li>
     * <li>{@link JsonParser#readValueAs(Class)}</li>
     * </ul>
     * Returns null if {@link #nextJsonObject(JsonParser)} returns null.
     *
     */
    public <T> T nextJsonObject(JsonParser jsonParser, Class<T> objectClass) throws IOException {
        
        JsonToken token = nextJsonObject(jsonParser);
        
        if (token == null) {
            
            return null;
        
        }
        
        return jsonParser.readValueAs(objectClass);
    
    }
    
      
    
    /**
     * Does the given jsonToken indicate the start of json object or array?
     */
    public boolean isJsonObject(JsonToken jsonToken) {
        
        if (JsonToken.START_OBJECT.equals(jsonToken) || JsonToken.START_ARRAY.equals(jsonToken)) {
            
            return true;
            
        }
        
        return false;
    }

}
    

