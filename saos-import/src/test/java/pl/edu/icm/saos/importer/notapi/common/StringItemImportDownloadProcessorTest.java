package pl.edu.icm.saos.importer.notapi.common;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.importer.notapi.common.StringItemImportDownloadProcessor;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download.RawSourceScJudgmentParser;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public class StringItemImportDownloadProcessorTest {

    private StringItemImportDownloadProcessor<RawSourceScJudgment> scjImportProcessor = new StringItemImportDownloadProcessor<RawSourceScJudgment>();
    
    private RawSourceScJudgmentParser simpleRawSourceScJudgmentParser = Mockito.mock(RawSourceScJudgmentParser.class);
    
    
    @Before
    public void before() {
        scjImportProcessor.setRawSourceJudgmentParser(simpleRawSourceScJudgmentParser);
    }
    
    @Test
    public void process() throws Exception {
        
        String content = "ddsdsdsdskjdjskdjs";
        RawSourceScJudgment rJudgment = new RawSourceScJudgment();
        rJudgment.setJsonContent(content);
        
        Mockito.when(simpleRawSourceScJudgmentParser.createRawSourceJudgment(Mockito.eq(content))).thenReturn(rJudgment);
        
        RawSourceScJudgment retRJudgment = scjImportProcessor.process(content);
        
        assertTrue(rJudgment == retRJudgment);
        
    }
    
}
