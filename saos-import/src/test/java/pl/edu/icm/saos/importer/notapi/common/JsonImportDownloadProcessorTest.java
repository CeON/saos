package pl.edu.icm.saos.importer.notapi.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.Source;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonImportDownloadProcessorTest {

    private JsonImportDownloadProcessor<RawSourceScJudgment> scjImportProcessor =
            new JsonImportDownloadProcessor<>(RawSourceScJudgment.class);
    
    @Mock
    private JsonStringParser<SourceScJudgment> sourceScJudgmentParser;
    
    
    @Before
    public void before() {
        scjImportProcessor.setSourceJudgmentParser(sourceScJudgmentParser);
    }
    
    @Test
    public void process() throws Exception {
        
        // given
        
        String content = "ddsdsdsdskjdjskdjs";
        
        SourceScJudgment sourceScJudgment = new SourceScJudgment();
        Source source = new Source();
        source.setSourceJudgmentId("1112223334444");
        sourceScJudgment.setSource(source);
        
        Mockito.when(sourceScJudgmentParser.parseAndValidate(Mockito.eq(content))).thenReturn(sourceScJudgment);
        
        
        // execute
        
        RawSourceScJudgment retRJudgment = scjImportProcessor.process(content);
        
        
        // assert
        
        assertEquals(content, retRJudgment.getJsonContent());
        assertEquals(source.getSourceJudgmentId(), retRJudgment.getSourceId());
        assertNull(retRJudgment.getProcessingDate());
        assertFalse(retRJudgment.isProcessed());
        
    }
    
}
