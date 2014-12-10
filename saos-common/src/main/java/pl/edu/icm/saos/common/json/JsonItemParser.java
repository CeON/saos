package pl.edu.icm.saos.common.json;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.validation.CommonValidator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;

/**
 * @author Łukasz Dumiszewski
 */

public class JsonItemParser<T> {
    
    private final Class<T> type;
    
    private CommonValidator commonValidator;
    private JsonFactory jsonFactory;
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JsonItemParser(Class<T> type) {
        super();
        this.type = type;
    }

    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Parses the given jsonContent into T <br/> <br/>
     * Uses {@link JsonParser} created by {@link JsonFactory#createParser(String)}. The parser has
     * the {@link Feature#ALLOW_COMMENTS} enabled. <br/>
     * Validates the parsed object with {@link CommonValidator#validate(Object)} <br/>
     *  
     */
    public T parse(String jsonContent) throws JsonItemParseException {
        try {
            
            JsonParser jsonParser = jsonFactory.createParser(jsonContent);
            jsonParser.enable(Feature.ALLOW_COMMENTS);
            
            T item = jsonParser.readValueAs(type);
            
            commonValidator.validateEx(item);
            
            return item;
            
        } catch (IOException e) {
            throw new JsonItemParseException(e);
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
