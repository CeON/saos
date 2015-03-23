package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import static org.junit.Assert.assertFalse;
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
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.importer.notapi.common.content.transaction.FilesOperationsTransaction;

import com.google.common.io.Files;

/**
 * @author madryk
 */
public class FilesOperationsTransactionTest {

    private FilesOperationsTransaction filesOperationsTransaction;
    
    private File destinationDir;
    
    @Before
    public void setUp() throws IOException {
        destinationDir = Files.createTempDir();
        createFilesStructure(destinationDir);
        
        filesOperationsTransaction = new FilesOperationsTransaction(destinationDir);
    }
    
    @After
    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(destinationDir);
        File tmpDir = Whitebox.getInternalState(filesOperationsTransaction, "deletedTmpDirectory");
        FileUtils.deleteDirectory(tmpDir);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void addFile() throws IOException {
        // given
        InputStream is = IOUtils.toInputStream("content of file");
        
        // execute
        filesOperationsTransaction.addFile(is, "a/file.txt");
        
        // assert
        assertFileInDestDirectory("a/file.txt", "content of file");
    }
    
    @Test
    public void addFile_AFTER_COMMIT() throws IOException {
        // given
        InputStream is = IOUtils.toInputStream("content of file");
        
        // execute
        filesOperationsTransaction.addFile(is, "a/file.txt");
        filesOperationsTransaction.commit();
        
        // assert
        assertFileInDestDirectory("a/file.txt", "content of file");
    }
    
    @Test
    public void addFile_AFTER_ROLLBACK() throws IOException {
        // given
        InputStream is = IOUtils.toInputStream("content of file");
        
        // execute
        filesOperationsTransaction.addFile(is, "a/file.txt");
        filesOperationsTransaction.rollback();
        
        // assert
        assertNoFileInDestDirectory("a/file.txt");
    }
    
    @Test
    public void overwriteFile() throws IOException {
        // given
        InputStream is = IOUtils.toInputStream("content of file");
        
        // execute
        filesOperationsTransaction.overwriteFile(is, "a/file.txt", "a/ab/1.txt");
        
        // assert
        assertFileInDeletedTmpDirectory("a/ab/1.txt", "");
        
        assertFileInDestDirectory("a/file.txt", "content of file");
        assertNoFileInDestDirectory("a/ab/1.txt");
    }
    
    @Test
    public void overwriteFile_AFTER_COMMIT() throws IOException {
        // given
        InputStream is = IOUtils.toInputStream("content of file");
        
        // execute
        filesOperationsTransaction.overwriteFile(is, "a/file.txt", "a/ab/1.txt");
        filesOperationsTransaction.commit();
        
        // assert
        assertFileInDestDirectory("a/file.txt", "content of file");
        assertNoFileInDestDirectory("a/ab/1.txt");
    }
    
    @Test
    public void overwriteFile_AFTER_ROLLBACK() throws IOException {
        // given
        InputStream is = IOUtils.toInputStream("content of file");
        
        // execute
        filesOperationsTransaction.overwriteFile(is, "a/file.txt", "a/ab/1.txt");
        filesOperationsTransaction.rollback();
        
        // assert
        assertNoFileInDestDirectory("a/file.txt");
        assertFileInDestDirectory("a/ab/1.txt", "");
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertFileInDeletedTmpDirectory(String path, String content) {
        File tmpDir = Whitebox.getInternalState(filesOperationsTransaction, "deletedTmpDirectory");
        
        File expectedFile = new File(tmpDir, path);
        assertTrue(expectedFile.exists());
        
        assertFileContent(expectedFile, content);
    }
    
    private void assertFileInDestDirectory(String path, String content) {
        File expectedFile = new File(destinationDir, path);
        assertTrue(expectedFile.exists());
        
        assertFileContent(expectedFile, content);
    }
    
    private void assertNoFileInDestDirectory(String path) {
        File expectedFile = new File(destinationDir, path);
        assertFalse(expectedFile.exists());
    }
    
    private void assertFileContent(File file, String expectedContent) {
        InputStream actualContentStream = null;
        InputStream expectedContentStream = null;
        try {
            actualContentStream = new FileInputStream(file);
            expectedContentStream = IOUtils.toInputStream(expectedContent);
        
            assertTrue(IOUtils.contentEquals(actualContentStream, expectedContentStream));
        } catch (IOException e) {
            IOUtils.closeQuietly(actualContentStream);
            IOUtils.closeQuietly(expectedContentStream);
        }
    }
    
    
    private void createFilesStructure(File destinationDir) throws IOException {
        File dirA = new File(destinationDir, "a");
        dirA.mkdir();
        File dirAB = new File(dirA, "ab");
        dirAB.mkdirs();
        
        File file = new File(dirAB, "1.txt");
        file.createNewFile();
    }
}
