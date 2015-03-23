package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

/**
 * Manager of {@link FilesOperationsTransaction} objects.
 * 
 * @author madryk
 */
@Service
public class FilesOperationsTransactionManager {
    
    private String contentDirectoryPath;
    
    private Map<String, FilesOperationsTransaction> filesOperationsTransactions = Maps.newHashMap();
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Creates new {@link FilesOperationsTransaction} object and returns its id.
     */
    public synchronized String openTransaction() {
        String transactionId = UUID.randomUUID().toString();
        
        FilesOperationsTransaction transaction = new FilesOperationsTransaction(new File(contentDirectoryPath));
        
        filesOperationsTransactions.put(transactionId, transaction);
        
        return transactionId;
    }
    
    /**
     * Returns {@link FilesOperationsTransaction} with id same as passed argument.
     */
    public synchronized FilesOperationsTransaction fetchTransaction(String transactionId) {
        return filesOperationsTransactions.get(transactionId);
    }
    
    
    /**
     * Approves changes made in {@link FilesOperationsTransaction} with id same as passed argument.
     */
    public synchronized void commit(String transactionId) {
        FilesOperationsTransaction transaction = filesOperationsTransactions.get(transactionId);
        try {
            transaction.commit();
        } catch (IOException e) {
            throw new FilesOperationsTransactionException(e);
        }
    }
    
    /**
     * Withdraws changes made in {@link FilesOperationsTransaction} with id same as passed argument.
     */
    public synchronized void rollback(String transactionId) {
        FilesOperationsTransaction transaction = filesOperationsTransactions.get(transactionId);
        try {
            transaction.rollback();
        } catch (IOException e) {
            throw new FilesOperationsTransactionException(e);
        }
    }

    @Value("${import.judgments.content.dir}")
    public void setContentDirectoryPath(String contentDirectoryPath) {
        this.contentDirectoryPath = contentDirectoryPath;
    }
    
    
}
