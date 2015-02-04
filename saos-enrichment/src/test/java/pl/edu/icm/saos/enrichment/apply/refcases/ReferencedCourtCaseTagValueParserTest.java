package pl.edu.icm.saos.enrichment.apply.refcases;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.validation.CommonValidator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author Łukasz Dumiszewski
 */

public class ReferencedCourtCaseTagValueParserTest {

    
    private final String jsonTagValue =  JsonNormalizer.normalizeJson("[ " +
                                         "{" +
                                             "caseNumber: 'XVI1 Ko 439/96', " +
                                         "   judgmentIds : []" +
                                         "}, " +  
                                         "{" +
                                             "caseNumber: 'II AKa 115/99', " +
                                             "judgmentIds : [83218] " +
                                         "}"+
                                         "]");
    
    private JsonStringParser<ReferencedCourtCasesTagValueItem[]> jsonStringParser = new JsonStringParser<>(ReferencedCourtCasesTagValueItem[].class);
    
    
    @Before
    public void before() {

        jsonStringParser.setJsonFactory(new MappingJsonFactory());
        
        jsonStringParser.setCommonValidator(mock(CommonValidator.class));
    
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void parseAndValidate() throws JsonParseException {
        
        // execute
        ReferencedCourtCasesTagValueItem[] referencedCourtCasesTagValueItems = jsonStringParser.parseAndValidate(jsonTagValue);
        
        // assert
        assertNotNull(referencedCourtCasesTagValueItems);
    }
    
    
}
