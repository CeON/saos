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

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment.Source;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class RawSourceCtJudgmentFactoryTest {

    private RawSourceCtJudgmentFactory rawSourceCtJudgmentFactory = new  RawSourceCtJudgmentFactory();
    
    @Mock
    private JsonItemParser<SourceCtJudgment> sourceCtJudgmentParser;
    
    
    @Before
    public void before() {
        rawSourceCtJudgmentFactory.setSourceCtJudgmentParser(sourceCtJudgmentParser);
    }
    
    @Test
    public void createRawSourceCtJudgment() throws JsonParseException, IOException {
        
        // given
        
        String jsonContent = "jjdskdj kdsj dskjds kdj";
        SourceCtJudgment sourceJudgment = new SourceCtJudgment();
        Source source = new Source();
        source.setSourceJudgmentId("1112223334444");
        sourceJudgment.setSource(source);
        when(sourceCtJudgmentParser.parse(eq(jsonContent))).thenReturn(sourceJudgment);
        
        
        // execute
        
        RawSourceCtJudgment rawSourceJudgment = rawSourceCtJudgmentFactory.createRawSourceCtJudgment(jsonContent);
    
    
        // assert
        assertEquals(jsonContent, rawSourceJudgment.getJsonContent());
        assertEquals(source.getSourceJudgmentId(), rawSourceJudgment.getSourceId());
        assertNull(rawSourceJudgment.getProcessingDate());
        assertFalse(rawSourceJudgment.isProcessed());
        
    }
    
}
