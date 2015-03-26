package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

/**
 * @author madryk
 */
public class ContentFileTransactionManagerTest {

    private ContentFileTransactionManager filesOperationsTransactionManager = new ContentFileTransactionManager();
    
    private ContentFileCommitRollbackService contentFileCommitRollbackService = mock(ContentFileCommitRollbackService.class); 
    
    
    private ContentFileTransactionContext context1;
    
    private ContentFileTransactionContext context2;
    
    private String contentDirectoryPath = "/content/directory/path";
    
    @Before
    public void setUp() {
        filesOperationsTransactionManager.setContentFileCommitRollbackService(contentFileCommitRollbackService);
        filesOperationsTransactionManager.setContentDirectoryPath(contentDirectoryPath);
        
        context1 = filesOperationsTransactionManager.openTransaction();
        context2 = filesOperationsTransactionManager.openTransaction();
        
    }
    
    @After
    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(context1.getDeletedTmpDirectory());
        FileUtils.deleteDirectory(context2.getDeletedTmpDirectory());
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void openTransaction() throws IOException {
        // execute
        ContentFileTransactionContext context = filesOperationsTransactionManager.openTransaction();
        
        // assert
        assertNotNull(context);
        assertEquals(contentDirectoryPath, context.getContentDirectory().getPath());
        assertTrue(context.getDeletedTmpDirectory().exists());
        assertManagedTransactionContexts(context1, context2, context);
        
        FileUtils.deleteDirectory(context.getDeletedTmpDirectory());
    }
    
    
    @Test
    public void commit() throws IOException {
        // execute
        filesOperationsTransactionManager.commit(context2);
        
        // assert
        verify(contentFileCommitRollbackService).commit(context2);
        verifyNoMoreInteractions(contentFileCommitRollbackService);
        assertManagedTransactionContexts(context1);
    }
    
    @Test(expected = NullPointerException.class)
    public void commit_NULL_CONTEXT() {
        // execute
        filesOperationsTransactionManager.commit(null);
    }
    
    @Test(expected = ContentFileTransactionException.class)
    public void commit_NOT_MANAGED_CONTEXT() {
        // given
        ContentFileTransactionContext context = mock(ContentFileTransactionContext.class);
        
        // execute
        filesOperationsTransactionManager.commit(context);
    }
    
    
    @Test
    public void rollback() throws IOException {
        // execute
        filesOperationsTransactionManager.rollback(context1);
        
        // assert
        verify(contentFileCommitRollbackService).rollback(context1);
        verifyNoMoreInteractions(contentFileCommitRollbackService);
        assertManagedTransactionContexts(context2);
    }
    
    @Test(expected = NullPointerException.class)
    public void rollback_NULL_CONTEXT() {
        // execute
        filesOperationsTransactionManager.rollback(null);
    }
    
    @Test(expected = ContentFileTransactionException.class)
    public void rollback_NOT_MANAGED_CONTEXT() {
        // given
        ContentFileTransactionContext context = mock(ContentFileTransactionContext.class);
        
        // execute
        filesOperationsTransactionManager.rollback(context);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertManagedTransactionContexts(ContentFileTransactionContext ... expectedContexts) {
        List<ContentFileTransactionContext> managedContexts = Whitebox.getInternalState(filesOperationsTransactionManager, "transactionContexts");
        
        assertThat(managedContexts, contains(expectedContexts));
    }
}
