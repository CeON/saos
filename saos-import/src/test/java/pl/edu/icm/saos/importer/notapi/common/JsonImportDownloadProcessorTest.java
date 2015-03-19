package pl.edu.icm.saos.importer.notapi.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.Source;
import pl.edu.icm.saos.importer.notapi.common.content.ContentSourceFileFinder;
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
    
    @Mock
    private ContentSourceFileFinder contentSourceFileFinder;
    
    private String downloadedContentDir = "some/downloaded/content/path";
    
    
    @Before
    public void before() {
        scjImportProcessor.setSourceJudgmentParser(sourceScJudgmentParser);
        scjImportProcessor.setContentSourceFileFinder(contentSourceFileFinder);
        scjImportProcessor.setDownloadedContentDir(downloadedContentDir);
    }
    
    @Test
    public void process() throws Exception {
        
        // given
        
        String content = "ddsdsdsdskjdjskdjs";
        
        SourceScJudgment sourceScJudgment = new SourceScJudgment();
        Source source = new Source();
        source.setSourceJudgmentId("1112223334444");
        sourceScJudgment.setSource(source);
        
        File importFile = new File("judgment.json");
        File contentFile = new File("judgment.zip");
        
        
        Mockito.when(sourceScJudgmentParser.parseAndValidate(Mockito.eq(content))).thenReturn(sourceScJudgment);
        
        Mockito.when(contentSourceFileFinder.findContentFile(new File(downloadedContentDir), importFile)).thenReturn(contentFile);
        
        
        // execute
        
        RawSourceScJudgment retRJudgment = scjImportProcessor.process(new JsonJudgmentItem(content, new File("judgment.json")));
        
        
        // assert
        
        assertEquals(content, retRJudgment.getJsonContent());
        assertEquals(source.getSourceJudgmentId(), retRJudgment.getSourceId());
        assertEquals(contentFile.getName(), retRJudgment.getJudgmentContentFilename());
        assertNull(retRJudgment.getProcessingDate());
        assertFalse(retRJudgment.isProcessed());
        
    }
    
}
