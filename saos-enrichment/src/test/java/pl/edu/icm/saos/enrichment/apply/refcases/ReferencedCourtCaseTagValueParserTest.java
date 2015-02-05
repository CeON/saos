package pl.edu.icm.saos.enrichment.apply.refcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.validation.CommonValidator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.google.common.collect.Lists;

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
        
        List<ReferencedCourtCasesTagValueItem> tagValueItems = Lists.newArrayList(referencedCourtCasesTagValueItems);
        assertEquals(1, tagValueItems.stream().filter(item->item.getCaseNumber().equals("XVI1 Ko 439/96") && item.getJudgmentIds().isEmpty()).count());
        assertEquals(1, tagValueItems.stream().filter(item->item.getCaseNumber().equals("II AKa 115/99") && item.getJudgmentIds().size()==1 && item.getJudgmentIds().contains(83218l)).count());
        
    }
    
    
}
