package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.IOException;
import java.util.Map;

import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

/**
 * @author madryk
 */
public class FilesOperationsTransactionManagerTest {

    private FilesOperationsTransactionManager filesOperationsTransactionManager = new FilesOperationsTransactionManager();
    
    private String transaction1Id = "id1";
    private FilesOperationsTransaction transaction1 = mock(FilesOperationsTransaction.class);
    
    private String transaction2Id = "id2";
    private FilesOperationsTransaction transaction2 = mock(FilesOperationsTransaction.class);
    
    @Before
    public void setUp() {
        Map<String, FilesOperationsTransaction> transactionsMap = Maps.newHashMap();
        transactionsMap.put(transaction1Id, transaction1);
        transactionsMap.put(transaction2Id, transaction2);
        
        Whitebox.setInternalState(filesOperationsTransactionManager, Map.class, transactionsMap);
        filesOperationsTransactionManager.setContentDirectoryPath("");
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void openTransaction() {
        // execute
        String transactionId = filesOperationsTransactionManager.openTransaction();
        
        // assert
        assertNotNull(transactionId);
        FilesOperationsTransaction transaction = filesOperationsTransactionManager.fetchTransaction(transactionId);
        assertFalse(transaction == transaction1);
        assertFalse(transaction == transaction2);
    }
    
    @Test
    public void fetchTransaction() {
        // execute
        FilesOperationsTransaction retTransaction = filesOperationsTransactionManager.fetchTransaction("id2");
        
        // assert
        assertTrue(retTransaction == transaction2);
    }
    
    @Test
    public void commit() throws IOException {
        // execute
        filesOperationsTransactionManager.commit("id2");
        
        // assert
        verify(transaction2).commit();
        verifyNoMoreInteractions(transaction1);
        verifyZeroInteractions(transaction1);
    }
    
    @Test
    public void rollback() throws IOException {
        // execute
        filesOperationsTransactionManager.rollback("id2");
        
        // assert
        verify(transaction2).rollback();
        verifyNoMoreInteractions(transaction1);
        verifyZeroInteractions(transaction1);
    }
}
