package pl.edu.icm.saos.importer.notapi.common.content;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.StepExecution;

import pl.edu.icm.saos.importer.notapi.common.ImportFileUtils;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class ContentDownloadStepExecutionListenerTest {

    private ContentDownloadStepExecutionListener contentDownloadListenter = new ContentDownloadStepExecutionListener();
    
    private ImportFileUtils importFileUtils = mock(ImportFileUtils.class);
    
    private ImportContentFileUtils importContentFileUtils = mock(ImportContentFileUtils.class);
    
    private ContentSourceFileFinder importContentFileFinder = mock(ContentSourceFileFinder.class);
    
    private String importPath = "some/path";
    
    private String importContentPath = "some/content/path";
    
    
    @Before
    public void setUp() {
        contentDownloadListenter.setImportFileUtils(importFileUtils);
        contentDownloadListenter.setImportContentFileUtils(importContentFileUtils);
        contentDownloadListenter.setImportContentFileFinder(importContentFileFinder);
        
        contentDownloadListenter.setImportDir(importPath);
        contentDownloadListenter.setImportContentDir(importContentPath);
        contentDownloadListenter.setRawJudgmentClass(RawSourceCcJudgment.class);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void beforeStep() {
        
        // given
        File importFile1 = new File("some/path/1");
        File importFile2 = new File("some/path/2");
        
        File importContentDir = new File(importContentPath);
        File contentFile1 = new File("some/content/path/1");
        File contentFile2 = new File("some/content/path/2");
        
        when(importFileUtils.listImportFiles(importPath)).thenReturn(Lists.newArrayList(importFile1, importFile2));
        
        when(importContentFileFinder.findContentFile(importContentDir, importFile1)).thenReturn(contentFile1);
        when(importContentFileFinder.findContentFile(importContentDir, importFile2)).thenReturn(contentFile2);
        
        
        // execute
        contentDownloadListenter.beforeStep(mock(StepExecution.class));
        
        
        // assert
        verify(importContentFileUtils).deleteAllContentFiles(RawSourceCcJudgment.class);
        
        verify(importFileUtils).listImportFiles(importPath);
        
        verify(importContentFileFinder).findContentFile(importContentDir, importFile1);
        verify(importContentFileFinder).findContentFile(importContentDir, importFile2);
        
        verify(importContentFileUtils).copyContentFile(contentFile1, RawSourceCcJudgment.class);
        verify(importContentFileUtils).copyContentFile(contentFile2, RawSourceCcJudgment.class);
        
    }
    
}
