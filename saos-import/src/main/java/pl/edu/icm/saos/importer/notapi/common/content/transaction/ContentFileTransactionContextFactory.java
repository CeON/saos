package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;

/**
 * Factory of {@link ContentFileTransactionContext} objects.
 * 
 * @author madryk
 */
@Service
public class ContentFileTransactionContextFactory {
    
    private String contentDirectoryPath;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Creates new {@link ContentFileTransactionContext} object and returns it.
     */
    public ContentFileTransactionContext createTransactionContext() {
        File tmpDirectory = Files.createTempDir();
        
        ContentFileTransactionContext transactionContext = new ContentFileTransactionContext(new File(contentDirectoryPath), tmpDirectory);
        
        return transactionContext;
    }

    
    //------------------------ SETTERS --------------------------
    
    @Value("${judgments.content.dir}")
    public void setContentDirectoryPath(String contentDirectoryPath) {
        this.contentDirectoryPath = contentDirectoryPath;
    }
    
}
