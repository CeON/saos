package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
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
        
        File file1 = new File(contentDir, "file1.txt");
        File file2 = new File(contentDir, "file2.txt");
        file1.createNewFile();
        file2.createNewFile();
        
        // execute
        contentFileCommitRollbackService.commit(context);
        
        // assert
        assertTrue(file1.exists());
        assertTrue(file2.exists());
    }
    
    @Test
    public void commit_DELETED_FILES() throws IOException {
        // given
        ContentFileTransactionContext context = new ContentFileTransactionContext(contentDir, deletedTmpDir);
        
        File file1 = new File(deletedTmpDir, "file1.txt");
        File file2 = new File(deletedTmpDir, "file2.txt");
        file1.createNewFile();
        file2.createNewFile();
        
        // execute
        contentFileCommitRollbackService.commit(context);
        
        // assert
        assertFalse(file1.exists());
        assertFalse(file2.exists());
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
        
        File file1 = new File(contentDir, "file1.txt");
        File file2 = new File(contentDir, "file2.txt");
        file1.createNewFile();
        file2.createNewFile();
        
        // execute
        contentFileCommitRollbackService.rollback(context);
        
        // assert
        assertFalse(file1.exists());
        assertFalse(file2.exists());
    }
    
    @Test
    public void rollback_DELETED_FILES() throws IOException {
        // given
        ContentFileTransactionContext context = new ContentFileTransactionContext(contentDir, deletedTmpDir);
        
        File file1 = new File(deletedTmpDir, "file1.txt");
        File file2 = new File(deletedTmpDir, "file2.txt");
        file1.createNewFile();
        file2.createNewFile();
        
        // execute
        contentFileCommitRollbackService.rollback(context);
        
        // assert
        assertTrue(new File(contentDir, "file1.txt").exists());
        assertTrue(new File(contentDir, "file2.txt").exists());
        assertFalse(file1.exists());
        assertFalse(file2.exists());
    }
}
