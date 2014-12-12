package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.download;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;

/**
 * @author madryk
 */
public class CtjImportDownloadProcessorTest {

    private CtjImportDownloadProcessor ctjImportProcessor = new CtjImportDownloadProcessor();
    
    private RawSourceCtJudgmentFactory rawSourceCtJudgmentFactory = mock(RawSourceCtJudgmentFactory.class);
    
    
    @Before
    public void before() {
        ctjImportProcessor.setRawSourceCtJudgmentFactory(rawSourceCtJudgmentFactory);
    }
    
    
    @Test
    public void process() throws Exception {
        
        String content = "ddsdsdsdskjdjskdjs";
        RawSourceCtJudgment rJudgment = new RawSourceCtJudgment();
        rJudgment.setJsonContent(content);
        
        when(rawSourceCtJudgmentFactory.createRawSourceCtJudgment(eq(content))).thenReturn(rJudgment);
        
        RawSourceCtJudgment retRJudgment = ctjImportProcessor.process(content);
        
        assertTrue(rJudgment == retRJudgment);
        
    }
}
