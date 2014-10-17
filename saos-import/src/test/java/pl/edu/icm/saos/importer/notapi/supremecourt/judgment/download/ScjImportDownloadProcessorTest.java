package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download.ScjImportDownloadProcessor;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download.RawSourceScJudgmentFactory;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public class ScjImportDownloadProcessorTest {

    private ScjImportDownloadProcessor scjImportProcessor = new ScjImportDownloadProcessor();
    
    private RawSourceScJudgmentFactory simpleRawSourceScJudgmentFactory = Mockito.mock(RawSourceScJudgmentFactory.class);
    
    
    @Before
    public void before() {
        scjImportProcessor.setSimpleRawSourceScJudgmentFactory(simpleRawSourceScJudgmentFactory);
    }
    
    @Test
    public void process() throws Exception {
        
        String content = "ddsdsdsdskjdjskdjs";
        RawSourceScJudgment rJudgment = new RawSourceScJudgment();
        rJudgment.setJsonContent(content);
        
        Mockito.when(simpleRawSourceScJudgmentFactory.createRawSourceScJudgment(Mockito.eq(content))).thenReturn(rJudgment);
        
        RawSourceScJudgment retRJudgment = scjImportProcessor.process(content);
        
        assertTrue(rJudgment == retRJudgment);
        
    }
    
}
