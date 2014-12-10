package pl.edu.icm.saos.api.enricher;

import static org.junit.Assert.assertEquals;

import javax.validation.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.validation.CommonValidator;

import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author Łukasz Dumiszewski
 */

public class EnrichmentTagItemParserTest {

    
    private JsonItemParser<EnrichmentTagItem> enrichmentTagItemParser = new JsonItemParser<>(EnrichmentTagItem.class);
    
    @Mock
    private CommonValidator commonValidator;
    
    
    private static String jsonContent = "{judgmentId:'123', /* comment should be passed */ "+
            "tagType:'SIMILAR_JUDGMENTS', "+
            "value: {" +
                      "key1:'value1'," +
                      "key2:{innerKey1:'innerValue1'}" +
                    "}"+
                  "}";
           
    

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        jsonContent = JsonNormalizer.normalizeJson(jsonContent);
        
        enrichmentTagItemParser.setJsonFactory(new MappingJsonFactory());
        
        enrichmentTagItemParser.setCommonValidator(commonValidator);
        
    }
    
    
    @Test
    public void parse() {
        
        // execute
        
        EnrichmentTagItem enrichmentTagItem = enrichmentTagItemParser.parse(jsonContent);
        
        
        // assert
        
        assertEquals(123, enrichmentTagItem.getJudgmentId());
        assertEquals("SIMILAR_JUDGMENTS", enrichmentTagItem.getTagType());   
        assertEquals("{" +
                      "\"key1\":\"value1\"," +
                      "\"key2\":{\"innerKey1\":\"innerValue1\"}" +
                    "}", enrichmentTagItem.getValue());
    }
    
    
    @Test(expected=ValidationException.class)
    public void parse_JudgeNameNull() {
        
        // given
        
        Mockito.doThrow(ValidationException.class).when(commonValidator).validateEx(Mockito.any(EnrichmentTagItem.class));
        
        // execute
        
        enrichmentTagItemParser.parse(jsonContent);
        
    }
    
    
    
}
