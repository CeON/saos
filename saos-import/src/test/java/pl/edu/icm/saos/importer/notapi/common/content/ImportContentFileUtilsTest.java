package pl.edu.icm.saos.importer.notapi.common.content;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;

import com.google.common.io.Files;

/**
 * @author madryk
 */
public class ImportContentFileUtilsTest {

    private ImportContentFileUtils importContentFileUtils = new ImportContentFileUtils();
    
    private ContentSourceFileFinder contentSourceFileFinder = mock(ContentSourceFileFinder.class);
    
    private String contentFilesBasePath;
    
    private File contentFilesBaseDir;
    
    
    @Before
    public void setUp() {
        importContentFileUtils.setContentFilesBaseDir(contentFilesBasePath);
        importContentFileUtils.setContentSourceFileFinder(contentSourceFileFinder);
        
        contentFilesBaseDir = Files.createTempDir();
        contentFilesBasePath = contentFilesBaseDir.getPath();
        
        importContentFileUtils.setContentFilesBaseDir(contentFilesBasePath);
    }
    
    @After
    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(contentFilesBaseDir);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void deleteAllContentFiles() throws IOException {
        // given
        File ccJudgmentsDir = new File(contentFilesBaseDir, "RawSourceCcJudgment");
        ccJudgmentsDir.mkdir();
        
        File contentFile1 = new File(ccJudgmentsDir, "file1.txt");
        File contentFile2 = new File(ccJudgmentsDir, "file2.txt");
        
        contentFile1.createNewFile();
        contentFile2.createNewFile();
        
        
        // execute
        importContentFileUtils.deleteAllContentFiles(RawSourceCcJudgment.class);
        
        
        // assert
        assertFalse(ccJudgmentsDir.exists());
    }
    
    @Test
    public void locateContentFile() throws IOException {
        // given
        File ccJudgmentsDir = new File(contentFilesBaseDir, "RawSourceCcJudgment");
        ccJudgmentsDir.mkdir();
        
        File importFile = new File("abc.json");
        File contentFile = new File(ccJudgmentsDir, "abc.zip");
        contentFile.createNewFile();
        
        when(contentSourceFileFinder.findContentFile(ccJudgmentsDir, importFile)).thenReturn(contentFile);
        
        
        // execute
        File retContentFile = importContentFileUtils.locateContentFile(importFile, RawSourceCcJudgment.class);
        
        
        // assert
        assertEquals(contentFile, retContentFile);
    }
    
    @Test
    public void copyContentFile() throws IOException {
        // given
        File sourceContentFile = File.createTempFile("contentFile", ".zip");
        
        
        // execute
        importContentFileUtils.copyContentFile(sourceContentFile, RawSourceCcJudgment.class);
        
        // assert
        File ccJudgmentsDir = new File(contentFilesBaseDir, "RawSourceCcJudgment");
        File expectedContentFile = new File(ccJudgmentsDir, sourceContentFile.getName());
        
        assertTrue(ccJudgmentsDir.exists());
        assertTrue(expectedContentFile.exists());
        
        sourceContentFile.delete();
    }
}
