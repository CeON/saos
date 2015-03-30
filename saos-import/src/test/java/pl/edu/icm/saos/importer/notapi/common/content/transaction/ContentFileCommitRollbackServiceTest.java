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

import com.google.common.io.Files;

/**
 * @author madryk
 */
public class ContentFileCommitRollbackServiceTest {

    private ContentFileCommitRollbackService contentFileCommitRollbackService = new ContentFileCommitRollbackService();
    
    private File contentDir;
    
    private File deletedTmpDir;
    
    @Before
    public void setUp() {
        contentDir = Files.createTempDir();
        deletedTmpDir = Files.createTempDir();
    }
    
    @After
    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(contentDir);
        FileUtils.deleteDirectory(deletedTmpDir);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void commit_NULL_CONTEXT() throws IOException {
        // execute
        contentFileCommitRollbackService.commit(null);
    }
    
    @Test
    public void commit_ADDED_FILES() throws IOException {
        // given
        ContentFileTransactionContext context = new ContentFileTransactionContext(contentDir, deletedTmpDir);
        
        context.addAddedFile("file1.txt");
        context.addAddedFile("file2.txt");
        
        File file1 = createFile(contentDir, "file1.txt", "content1");
        File file2 = createFile(contentDir, "file2.txt", "content2");
        
        // execute
        contentFileCommitRollbackService.commit(context);
        
        // assert
        assertFile(file1, "content1");
        assertFile(file2, "content2");
        assertFalse(deletedTmpDir.exists());
    }
    
    @Test
    public void commit_DELETED_FILES() throws IOException {
        // given
        ContentFileTransactionContext context = new ContentFileTransactionContext(contentDir, deletedTmpDir);
        
        File file1 = createFile(deletedTmpDir, "file1.txt", "content1");
        File file2 = createFile(deletedTmpDir, "file2.txt", "content2");
        
        // execute
        contentFileCommitRollbackService.commit(context);
        
        // assert
        assertFalse(file1.exists());
        assertFalse(file2.exists());
        assertFalse(deletedTmpDir.exists());
    }
    
    @Test(expected = NullPointerException.class)
    public void rollback_NULL_CONTEXT() throws IOException {
        contentFileCommitRollbackService.rollback(null);
    }
    
    @Test
    public void rollback_ADDED_FILES() throws IOException {
        // given
        ContentFileTransactionContext context = new ContentFileTransactionContext(contentDir, deletedTmpDir);
        
        context.addAddedFile("file1.txt");
        context.addAddedFile("file2.txt");
        
        File file1 = createFile(contentDir, "file1.txt", "content1");
        File file2 = createFile(contentDir, "file2.txt", "content2");
        file1.createNewFile();
        file2.createNewFile();
        
        // execute
        contentFileCommitRollbackService.rollback(context);
        
        // assert
        assertFalse(file1.exists());
        assertFalse(file2.exists());
        assertFalse(deletedTmpDir.exists());
    }
    
    @Test
    public void rollback_DELETED_FILES() throws IOException {
        // given
        ContentFileTransactionContext context = new ContentFileTransactionContext(contentDir, deletedTmpDir);
        
        File file1 = createFile(deletedTmpDir, "file1.txt", "content1");
        File file2 = createFile(deletedTmpDir, "file2.txt", "content2");
        
        // execute
        contentFileCommitRollbackService.rollback(context);
        
        // assert
        assertFile(new File(contentDir, "file1.txt"), "content1");
        assertFile(new File(contentDir, "file2.txt"), "content2");
        assertFalse(file1.exists());
        assertFalse(file2.exists());
        assertFalse(deletedTmpDir.exists());
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private File createFile(File dir, String filename, String content) throws IOException {
        File file = new File(dir, filename);
        try (InputStream inputStream = IOUtils.toInputStream(content)) {
            FileUtils.copyInputStreamToFile(inputStream, file);
        }
        
        return file;
    }
    
    private void assertFile(File actualFile, String content) throws IOException {
        assertTrue(actualFile.exists());
        try (
                InputStream actualContentStream = new FileInputStream(actualFile);
                InputStream expectedContentStream = IOUtils.toInputStream(content);) {
            
            assertTrue(IOUtils.contentEquals(actualContentStream, expectedContentStream));
        }
    }
}
