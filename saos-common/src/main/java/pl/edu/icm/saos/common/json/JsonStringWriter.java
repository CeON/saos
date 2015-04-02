package pl.edu.icm.saos.common.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Writer of objects to json strings
 * 
 * @author madryk
 * @param <T> - type of objects that are supported
 */
@Service
public class JsonStringWriter<T> {
    
    private JsonFactory jsonFactory;
    
    
    //------------------------ LOGIC --------------------------
    
    public String write(T object) throws JsonGenerationException {
        
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
