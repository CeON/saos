package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download.RawSourceScJudgmentFactory;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgmentParser;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment.Source;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class RawSourceScJudgmentFactoryTest {

    private RawSourceScJudgmentFactory simpleRawSourceScJudgmentFactory = new RawSourceScJudgmentFactory();
    
    private SourceScJudgmentParser sourceScJudgmentParser = Mockito.mock(SourceScJudgmentParser.class);
    
    
    @Before
    public void before() {
        simpleRawSourceScJudgmentFactory.setSourceScJudgmentParser(sourceScJudgmentParser);
    }
    
    
    @Test
    public void createSimpleRawSourceScJudgment() throws JsonParseException, IOException {
        
        // given
        
        String jsonContent = "jjdskdj kdsj dskjds kdj";
        SourceScJudgment sourceJudgment = new SourceScJudgment();
        sourceJudgment.setSupremeCourtChambers(Lists.newArrayList("Chamber 1","Chamber 2"));
        Source source = new Source();
        source.setSourceJudgmentId("1112223334444");
        sourceJudgment.setSource(source);
        Mockito.when(sourceScJudgmentParser.parse(Mockito.eq(jsonContent))).thenReturn(sourceJudgment);
        
        
        // execute
        
        RawSourceScJudgment rawSourceJudgment = simpleRawSourceScJudgmentFactory.createRawSourceScJudgment(jsonContent);
    
    
        // assert
        assertEquals(jsonContent, rawSourceJudgment.getJsonContent());
        assertEquals(source.getSourceJudgmentId(), rawSourceJudgment.getSourceId());
        assertEquals(true, rawSourceJudgment.isMultiChambers());
        assertNull(rawSourceJudgment.getProcessingDate());
        assertFalse(rawSourceJudgment.isProcessed());
        
    }
    
    
}
