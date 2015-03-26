package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

/**
 * Service responsible for commits and rollbacks operations
 * on {@link ContentFileTransactionContext}
 * 
 * @author madryk
 */
@Service
public class ContentFileCommitRollbackService {

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Performs commit of operations defined in {@link ContentFileTransactionContext}
     */
    public void commit(ContentFileTransactionContext context) throws IOException {
        Preconditions.checkNotNull(context);
        
        File deletedTmpDirectory = context.getDeletedTmpDirectory();
        
        FileUtils.deleteDirectory(deletedTmpDirectory);
    }
    
    /**
     * Performs rollback of operations defined in {@link ContentFileTransactionContext}
     */
    public void rollback(ContentFileTransactionContext context) throws IOException {
        Preconditions.checkNotNull(context);
        
        File contentDirectory = context.getContentDirectory();
        File deletedTmpDirectory = context.getDeletedTmpDirectory();
        
        while(!context.isAddedFilesQueueEmpty()) {
            String addedFilePath = context.pollAddedFileFromQueue();
            File addedFile = new File(contentDirectory, addedFilePath);
            addedFile.delete();
        }
        
        FileUtils.copyDirectory(deletedTmpDirectory, contentDirectory);
        FileUtils.deleteDirectory(deletedTmpDirectory);
    }
}
