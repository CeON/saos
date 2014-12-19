package pl.edu.icm.saos.common.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.google.common.base.Preconditions;

/**
 * Json utilities
 * @author Łukasz Dumiszewski
 */
public class JsonUtils {

    
    /**  
     * Returns the next token or null if there is no more tokens or the next token is not a json object or array ({@link #isJsonObject(JsonToken)}).
     * 
     * 
     * @throws IllegalArgumentException if the {@link JsonParser#getCodec()} == null. You can use {@link MappingJsonFactory} for creating
     * parser with default codec
     * @throws JsonParseException if the nextToken cannot be parsed
     * @throws IOException if I/O error occured
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
     * Formats the current token. Invokes {@link JsonParser#readValueAsTree()}<br/>
     * Returns null if {@link JsonParser#readValueAsTree()} returns {@link NullNode}
     * @throws JsonParseException if the current token is not a valid json
     * @throws IOException in case of I/O Error
     */
    public String formatCurrentTokenTree(JsonParser jsonParser) throws IOException {

        TreeNode node = jsonParser.readValueAsTree();
        
        if (node instanceof NullNode) {
            return null;
        }
        
        return node.toString();
    
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
    

