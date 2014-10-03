package pl.edu.icm.saos.importer.simple.common;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * Json utilities
 * @author Łukasz Dumiszewski
 */
@Service("jsonUtils")
public class JsonUtils {

    
    /**  
     * Returns next node as string. The next node is a node that is an array element or the whole object
     * in case there is one element only (not array of elements). 
     * @throws IllegalStateException if the jsonParser has no codec; you can use {@link MappingJsonFactory} for creating
     * parser with default codec
     * 
     * */
    public String nextNode(JsonParser jp) throws JsonParseException, IOException, IllegalStateException {
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
