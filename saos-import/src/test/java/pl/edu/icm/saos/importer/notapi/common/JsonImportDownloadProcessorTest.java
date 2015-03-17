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
import pl.edu.icm.saos.importer.notapi.common.content.ImportContentFileUtils;
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
    private ImportContentFileUtils importContentFileUtils;
    
    
    @Before
    public void before() {
        scjImportProcessor.setSourceJudgmentParser(sourceScJudgmentParser);
        scjImportProcessor.setImportContentFileUtils(importContentFileUtils);
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
        
        Mockito.when(importContentFileUtils.locateContentFile(importFile, RawSourceScJudgment.class)).thenReturn(contentFile);
        
        
        // execute
        
        RawSourceScJudgment retRJudgment = scjImportProcessor.process(new JsonJudgmentNode(content, new File("judgment.json")));
        
        
        // assert
        
        assertEquals(content, retRJudgment.getJsonContent());
        assertEquals(source.getSourceJudgmentId(), retRJudgment.getSourceId());
        assertEquals(contentFile.getPath(), retRJudgment.getJudgmentContentPath());
        assertNull(retRJudgment.getProcessingDate());
        assertFalse(retRJudgment.isProcessed());
        
    }
    
}
