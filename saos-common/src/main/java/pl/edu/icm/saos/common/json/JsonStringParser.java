package pl.edu.icm.saos.common.json;

import java.io.IOException;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.validation.CommonValidator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;

/**
 * @author Łukasz Dumiszewski
 */

public class JsonStringParser<T> {
    
    private final Class<T> type;
    
    private CommonValidator commonValidator;
    private JsonFactory jsonFactory;
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JsonStringParser(Class<T> type) {
        super();
        this.type = type;
    }

    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Parses the given jsonContent into T <br/> <br/>
     * Uses {@link JsonParser} created by {@link JsonFactory#createParser(String)}. The parser has
     * the {@link Feature#ALLOW_COMMENTS} enabled. <br/>
     * Validates the parsed object with {@link CommonValidator#validateEx(Object)} <br/>
     * 
     * @throws JsonParseException if some parsing error occurred
     * @throws ValidationException in case of validation errors
     *  
     */
    public T parseAndValidate(String jsonContent) throws JsonParseException {
        JsonParser jsonParser = null;
        try {
            
            jsonParser = jsonFactory.createParser(jsonContent);
            jsonParser.enable(Feature.ALLOW_COMMENTS);
            
            T item = jsonParser.readValueAs(type);
            
            commonValidator.validateEx(item);
            
            return item;
            
        } catch (IOException e) {
            
            throw new JsonParseException(e.getMessage(), jsonParser!=null?jsonParser.getCurrentLocation():null, e);
        }
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setCommonValidator(CommonValidator commonValidator) {
        this.commonValidator = commonValidator;
    }

    @Autowired
    public void setJsonFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }
}
