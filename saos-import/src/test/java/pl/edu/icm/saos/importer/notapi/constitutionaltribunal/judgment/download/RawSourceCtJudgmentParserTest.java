package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment.Source;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class RawSourceCtJudgmentParserTest {

    private RawSourceCtJudgmentParser rawSourceCtJudgmentParser = new  RawSourceCtJudgmentParser();
    
    @Mock
    private JsonStringParser<SourceCtJudgment> sourceCtJudgmentParser;
    
    
    @Before
    public void before() {
        rawSourceCtJudgmentParser.setSourceCtJudgmentParser(sourceCtJudgmentParser);
    }
    
    @Test
    public void parseRawSourceJudgment() throws JsonParseException, IOException {
        
        // given
        
        String jsonContent = "jjdskdj kdsj dskjds kdj";
        SourceCtJudgment sourceJudgment = new SourceCtJudgment();
        Source source = new Source();
        source.setSourceJudgmentId("1112223334444");
        sourceJudgment.setSource(source);
        when(sourceCtJudgmentParser.parseAndValidate(eq(jsonContent))).thenReturn(sourceJudgment);
        
        
        // execute
        
        RawSourceCtJudgment rawSourceJudgment = rawSourceCtJudgmentParser.parseRawSourceJudgment(jsonContent);
    
    
        // assert
        assertEquals(jsonContent, rawSourceJudgment.getJsonContent());
        assertEquals(source.getSourceJudgmentId(), rawSourceJudgment.getSourceId());
        assertNull(rawSourceJudgment.getProcessingDate());
        assertFalse(rawSourceJudgment.isProcessed());
        
    }
    
}
