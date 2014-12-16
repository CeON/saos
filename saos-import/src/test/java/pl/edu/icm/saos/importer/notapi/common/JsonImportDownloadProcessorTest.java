package pl.edu.icm.saos.importer.notapi.common;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadProcessor;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download.RawSourceScJudgmentFactory;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public class JsonImportDownloadProcessorTest {

    private JsonImportDownloadProcessor<RawSourceScJudgment> scjImportProcessor = new JsonImportDownloadProcessor<RawSourceScJudgment>();
    
    private RawSourceScJudgmentFactory simpleRawSourceScJudgmentFactory = Mockito.mock(RawSourceScJudgmentFactory.class);
    
    
    @Before
    public void before() {
        scjImportProcessor.setRawSourceJudgmentFactory(simpleRawSourceScJudgmentFactory);
    }
    
    @Test
    public void process() throws Exception {
        
        String content = "ddsdsdsdskjdjskdjs";
        RawSourceScJudgment rJudgment = new RawSourceScJudgment();
        rJudgment.setJsonContent(content);
        
        Mockito.when(simpleRawSourceScJudgmentFactory.createRawSourceJudgment(Mockito.eq(content))).thenReturn(rJudgment);
        
        RawSourceScJudgment retRJudgment = scjImportProcessor.process(content);
        
        assertTrue(rJudgment == retRJudgment);
        
    }
    
}
