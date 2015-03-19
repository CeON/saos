package pl.edu.icm.saos.importer.notapi.common.content;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.importer.common.ImportException;

import com.google.common.io.Files;

/**
 * @author madryk
 */
public class ContentSourceFileFinderTest {

    private ContentSourceFileFinder contentSourceFileFinder = new ContentSourceFileFinder();
    
    private File contentDir;
    
    
    @Before
    public void setUp() {
        contentSourceFileFinder.setEligibleFileExtensions(new String[] { "zip", "pdf" });
        
        contentDir = Files.createTempDir();
    }
    
    @After
    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(contentDir);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void findContentFile() throws IOException {
        // given
        File file1 = new File(contentDir, "abc.txt");
        File file2 = new File(contentDir, "abc.zip");
        File file3 = new File(contentDir, "123.zip");
        
        file1.createNewFile();
        file2.createNewFile();
        file3.createNewFile();
        
        // execute
        File file = contentSourceFileFinder.findContentFile(contentDir, new File("abc.tar.gz"));
        
        // assert
        assertEquals(file2.getPath(), file.getPath());
        
    }
    
    @Test(expected = ImportException.class)
    public void findContentFile_NOT_FOUND() throws IOException {
        // given
        File file1 = new File(contentDir, "abc.pdf");
        File file2 = new File(contentDir, "abc.zip");
        
        file1.createNewFile();
        file2.createNewFile();
        
        // execute
        contentSourceFileFinder.findContentFile(contentDir, new File("abc.tar.gz"));
        
    }
    
    @Test(expected = ImportException.class)
    public void findContentFile_MORE_THAN_ONE() throws IOException {
        // given
        File contentDir = Files.createTempDir();
        
        // execute
        contentSourceFileFinder.findContentFile(contentDir, new File("abc.tar.gz"));
        
    }
    
}
