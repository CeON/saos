package pl.edu.icm.saos.common.json;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;

import javax.validation.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import pl.edu.icm.saos.common.validation.CommonValidator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;

/**
 * @author Łukasz Dumiszewski
 */

public class JsonItemParserTest {

    
    private JsonItemParser<String> jsonItemParser = new JsonItemParser<>(String.class);
 
    @Mock private CommonValidator commonValidator;
    
    @Mock private JsonFactory jsonFactory;
    
    @Mock private JsonParser jsonParser;
    
    
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
        jsonItemParser.setCommonValidator(commonValidator);
        
        jsonItemParser.setJsonFactory(jsonFactory);
        
        
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void parseAndValidate() throws IOException {
        
        // given
        
        String jsonContent = "232ejfjkdjf";
        String parseResult = "233efjdjkfvdkjf";
        when(jsonFactory.createParser(jsonContent)).thenReturn(jsonParser);
        when(jsonParser.readValueAs(String.class)).thenReturn(parseResult);
        
        
        // execute
        
        String retValue = jsonItemParser.parseAndValidate(jsonContent);
        
        
        // assert
        
        assertEquals(retValue, parseResult);
        verify(jsonParser).enable(Feature.ALLOW_COMMENTS);
        verify(commonValidator).validateEx(parseResult);
        
    }
    
    
    @Test(expected = ValidationException.class)
    public void parseAndValidate_ValidationException() throws IOException {
        
        // given
        
        String jsonContent = "232ejfjkdjf";
        String parseResult = "233efjdjkfvdkjf";
        when(jsonFactory.createParser(jsonContent)).thenReturn(jsonParser);
        when(jsonParser.readValueAs(String.class)).thenReturn(parseResult);
        Mockito.doThrow(ValidationException.class).when(commonValidator).validateEx(parseResult);
        
        // execute
        
        jsonItemParser.parseAndValidate(jsonContent);
        
       
    }
    
    
    
    @SuppressWarnings("unchecked")
    @Test(expected = JsonParseException.class)
    public void parseAndValidate_JsonParseException() throws IOException {
        
        // given
        
        String jsonContent = "232ejfjkdjf";
        when(jsonFactory.createParser(jsonContent)).thenReturn(jsonParser);
        when(jsonParser.readValueAs(String.class)).thenThrow(IOException.class);
        
        
        // execute
        
        jsonItemParser.parseAndValidate(jsonContent);
        
       
    }
    
}
