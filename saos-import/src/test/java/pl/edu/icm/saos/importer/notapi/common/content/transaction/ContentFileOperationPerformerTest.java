package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Files;

/**
 * @author madryk
 */
public class ContentFileOperationPerformerTest {

    private ContentFileOperationPerformer contentFileOperationPerformer = new ContentFileOperationPerformer();
    
    private File contentDir;
    
    private File deletedTmpDir;
    
    private ContentFileTransactionContext context;
    
    private InputStream is;
    
    @Before
    public void setUp() {
        contentDir = Files.createTempDir();
        deletedTmpDir = Files.createTempDir();
        
        context = new ContentFileTransactionContext(contentDir, deletedTmpDir);
        is = IOUtils.toInputStream("content of file");
    }
    
    @After
    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(contentDir);
        FileUtils.deleteDirectory(deletedTmpDir);
        is.close();
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void addFile_NULL_CONTEXT() throws IOException {
        // execute
        contentFileOperationPerformer.addFile(null, is, "file.txt");
    }
    
    @Test(expected = NullPointerException.class)
    public void addFile_NULL_STREAM() throws IOException {
        // execute
        contentFileOperationPerformer.addFile(context, null, "file.txt");
    }
    
    @Test(expected = NullPointerException.class)
    public void addFile_NULL_PATH() throws IOException {
        // execute
        contentFileOperationPerformer.addFile(context, is, null);
    }
    
    @Test
    public void addFile() throws IOException {
        // execute
        contentFileOperationPerformer.addFile(context, is, "file.txt");
        
        // assert
        assertFileInContentDirectory("file.txt", "content of file");
        
        assertEquals(1, context.getAddedFiles().size());
        assertEquals("file.txt", context.getAddedFiles().peek());
    }
    
    
    
    @Test(expected = NullPointerException.class)
    public void overwriteFile_NULL_CONTEXT() throws IOException {
        // execute
        contentFileOperationPerformer.overwriteFile(null, is, "file.txt", "oldFile.txt");
    }
    
    @Test(expected = NullPointerException.class)
    public void overwriteFile_NULL_STREAM() throws IOException {
        // execute
        contentFileOperationPerformer.overwriteFile(context, null, "file.txt", "oldFile.txt");
    }
    
    @Test(expected = NullPointerException.class)
    public void overwriteFile_NULL_PATH() throws IOException {
        // execute
        contentFileOperationPerformer.overwriteFile(context, is, null, "oldFile.txt");
    }
    
    @Test(expected = NullPointerException.class)
    public void overwriteFile_NULL_OLD_PATH() throws IOException {
        // execute
        contentFileOperationPerformer.overwriteFile(context, is, "file.txt", null);
    }
    
    
    
    @Test
    public void overwriteFile() throws IOException {
        // given
        ContentFileTransactionContext context = new ContentFileTransactionContext(contentDir, deletedTmpDir);
        InputStream is = IOUtils.toInputStream("content of file");
        
        File oldFile = new File(contentDir, "oldFile.txt");
        oldFile.createNewFile();
        
        // execute
        contentFileOperationPerformer.overwriteFile(context, is, "file.txt", "oldFile.txt");
        
        // assert
        assertFileInContentDirectory("file.txt", "content of file");
        assertFileInDeletedTmpDirectory("oldFile.txt", "");
        
        assertEquals(1, context.getAddedFiles().size());
        assertEquals("file.txt", context.getAddedFiles().peek());
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertFileInContentDirectory(String path, String content) throws IOException {
        File expectedFile = new File(contentDir, path);
        assertTrue(expectedFile.exists());
        
        assertFileContent(expectedFile, content);
    }
    
    private void assertFileInDeletedTmpDirectory(String path, String content) throws IOException {
        File expectedFile = new File(deletedTmpDir, path);
        assertTrue(expectedFile.exists());
        
        assertFileContent(expectedFile, content);
    }
    
    private void assertFileContent(File file, String expectedContent) throws IOException {
        try (
                InputStream actualContentStream = new FileInputStream(file);
                InputStream expectedContentStream = IOUtils.toInputStream(expectedContent);) {
            
            assertTrue(IOUtils.contentEquals(actualContentStream, expectedContentStream));
        }
    }
}
