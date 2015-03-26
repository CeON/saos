package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

/**
 * Manager of {@link ContentFileTransactionContext} objects.
 * 
 * @author madryk
 */
@Service
public class ContentFileTransactionManager {
    
    private String contentDirectoryPath;
    
    private List<ContentFileTransactionContext> transactionContexts = Lists.newArrayList();
    
    private ContentFileCommitRollbackService contentFileCommitRollbackService;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Creates new {@link ContentFileTransactionContext} object and returns it.
     * It also adds returned context to list of managed contexts of this service.
     */
    public synchronized ContentFileTransactionContext openTransaction() {
        File tmpDirectory = Files.createTempDir();
        
        ContentFileTransactionContext transactionContext = new ContentFileTransactionContext(new File(contentDirectoryPath), tmpDirectory);
        
        transactionContexts.add(transactionContext);
        
        return transactionContext;
    }
    
    
    /**
     * Approves changes made to files saved in {@link ContentFileTransactionContext}.
     * It also removes passed transaction context from managed contexts list.
     */
    public synchronized void commit(ContentFileTransactionContext transactionContext) {
        Preconditions.checkNotNull(transactionContext);
        
        if (!transactionContexts.contains(transactionContext)) {
            throw new ContentFileTransactionException("Transaction context is not managed by this transaction manager");
        }
        
        try {
            contentFileCommitRollbackService.commit(transactionContext);
            closeTransaction(transactionContext);
        } catch (IOException e) {
            throw new ContentFileTransactionException(e);
        }
    }
    
    /**
     * Withdraws changes made to files saved in {@link ContentFileTransactionContext}.
     * It also removes passed transaction context from managed context list.
     */
    public synchronized void rollback(ContentFileTransactionContext transactionContext) {
        Preconditions.checkNotNull(transactionContext);
        
        if (!transactionContexts.contains(transactionContext)) {
            throw new ContentFileTransactionException("Transaction context is not managed by this transaction manager");
        }
        
        try {
            contentFileCommitRollbackService.rollback(transactionContext);
            closeTransaction(transactionContext);
        } catch (IOException e) {
            throw new ContentFileTransactionException(e);
        }
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void closeTransaction(ContentFileTransactionContext transactionContext) throws IOException {
        
        transactionContexts.remove(transactionContext);
        File tmpDirectory = transactionContext.getDeletedTmpDirectory();
        
        FileUtils.deleteDirectory(tmpDirectory);
    }

    
    //------------------------ SETTERS --------------------------
    
    @Value("${judgments.content.dir}")
    public void setContentDirectoryPath(String contentDirectoryPath) {
        this.contentDirectoryPath = contentDirectoryPath;
    }

    @Autowired
    public void setContentFileCommitRollbackService(
            ContentFileCommitRollbackService contentFileCommitRollbackService) {
        this.contentFileCommitRollbackService = contentFileCommitRollbackService;
    }
    
    
}
