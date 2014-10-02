package pl.edu.icm.saos.importer.simple.supremecourt.judgment.download;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.importer.SimpleRawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public class ScjImportDownloadProcessorTest {

    private ScjImportDownloadProcessor scjImportProcessor = new ScjImportDownloadProcessor();
    
    
    
    @Test
    public void process() {
        
        String content = "ddsdsdsdskjdjskdjs";
        
        SimpleRawSourceScJudgment rJudgment = scjImportProcessor.process(content);
        
        assertEquals(content, rJudgment.getJsonContent());
        
    }
    
}
