package pl.edu.icm.saos.common.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.google.common.base.Preconditions;

/**
 * Json utilities
 * @author Łukasz Dumiszewski
 */
public final class JsonUtils {

    
    private JsonUtils() {
        throw new IllegalArgumentException("may not be instantiated");
    }
    
    /**  
     * TODO: This method is a kind of strange, see: https://github.com/CeON/saos/issues/377
     * 
     * Returns next node as string. The next node is a node that is an array element or the whole object
     * in case there is one element only (not array of elements).<br/>
     * Returns null if there is no next node, for example when the traversing of array has reached an end.
     * <br/>   
     * 
     * @throws IllegalArgumentException if the {@link JsonParser#getCodec()} == null. You can use {@link MappingJsonFactory} for creating
     * parser with default codec
     * @throws JsonParseException if the nextNode cannot be parsed
     * @throws IOException if I/O error occured
     * 
     * */
    public static String nextNode(JsonParser jp) throws IOException {
        
        Preconditions.checkArgument(jp.getCodec() != null);
        
        JsonToken current = jp.nextToken();
        if (current == JsonToken.START_ARRAY) {
           jp.nextToken();
        } else if (current == JsonToken.END_ARRAY) {
            return null;
        }
        
        JsonNode jsonNode = jp.readValueAsTree();
        return jsonNode == null? null: jsonNode.toString();
            
    }
    
}
