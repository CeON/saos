package pl.edu.icm.saos.importer.notapi.common.content;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.StepExecution;

import pl.edu.icm.saos.importer.notapi.common.ImportFileUtils;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

/**
 * @author madryk
 */
public class ContentDownloadStepExecutionListenerTest {

    private ContentDownloadStepExecutionListener contentDownloadListenter = new ContentDownloadStepExecutionListener();
    
    private ImportFileUtils importFileUtils = mock(ImportFileUtils.class);
    
    private ContentSourceFileFinder importContentFileFinder = mock(ContentSourceFileFinder.class);
    
    private String importPath = "some/path";
    
    private File importContentDir;
    
    private File downloadedContentDir;
    
    
    @Before
    public void setUp() {
        contentDownloadListenter.setImportFileUtils(importFileUtils);
        contentDownloadListenter.setImportContentFileFinder(importContentFileFinder);
        
        importContentDir = Files.createTempDir();
        downloadedContentDir = Files.createTempDir();
        
        contentDownloadListenter.setImportMetadataDir(importPath);
        contentDownloadListenter.setImportContentDir(importContentDir.getPath());
        contentDownloadListenter.setDownloadedContentDir(downloadedContentDir.getPath());
    }
    
    @After
    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(importContentDir);
        FileUtils.deleteDirectory(downloadedContentDir);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void beforeStep() throws IOException {
        
        // given
        File importMetadataFile1 = new File("some/path/1");
        File importMetadataFile2 = new File("some/path/2");
        
        File contentFile1 = new File(importContentDir, "abc.zip");
        File contentFile2 = new File(importContentDir, "123.zip");
        
        contentFile1.createNewFile();
        contentFile2.createNewFile();
        
        File oldDownloadedContentFile = new File(downloadedContentDir, "zxc.zip");
        oldDownloadedContentFile.createNewFile();
        
        when(importFileUtils.listImportFiles(importPath)).thenReturn(Lists.newArrayList(importMetadataFile1, importMetadataFile2));
        
        when(importContentFileFinder.findContentFile(importContentDir, importMetadataFile1)).thenReturn(contentFile1);
        when(importContentFileFinder.findContentFile(importContentDir, importMetadataFile2)).thenReturn(contentFile2);
        
        
        // execute
        contentDownloadListenter.beforeStep(mock(StepExecution.class));
        
        
        // assert
        File downloadedContentFile1 = new File(downloadedContentDir, "abc.zip");
        File downloadedContentFile2 = new File(downloadedContentDir, "123.zip");
        
        assertTrue(downloadedContentFile1.exists());
        assertTrue(downloadedContentFile2.exists());
        assertFalse(oldDownloadedContentFile.exists());
        
        verify(importFileUtils).listImportFiles(importPath);
        
        verify(importContentFileFinder).findContentFile(importContentDir, importMetadataFile1);
        verify(importContentFileFinder).findContentFile(importContentDir, importMetadataFile2);
        
    }
    
}
