package pl.edu.icm.saos.common.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.node.NullNode;

/**
 * @author Łukasz Dumiszewski
 */
@Service
public class JsonFormatter {
    
    private JsonFactory jsonFactory;
    
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
    
    public String formatObject(Object object) throws JsonGenerationException {
        
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            JsonGenerator generator = jsonFactory.createGenerator(outputStream);
            generator.writeObject(object);
            
            return outputStream.toString();
            
        } catch (IOException e) {
            throw new JsonGenerationException(e);
        }
        
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setJsonFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }
    
}
