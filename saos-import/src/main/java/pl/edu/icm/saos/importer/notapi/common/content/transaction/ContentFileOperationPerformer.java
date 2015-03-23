package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

/**
 * Handler of adding and overwriting files within {@link ContentFileTransactionContext}
 * 
 * @author madryk
 */
@Service
public class ContentFileOperationPerformer {

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Adds file in transaction context
     */
    public void addFile(ContentFileTransactionContext context, InputStream inputStream, String path) throws IOException {
        
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(path);
        
        File contentDirectory = context.getContentDirectory();
        
        FileUtils.copyInputStreamToFile(inputStream, new File(contentDirectory, path));
        context.addAddedFile(path);
    }
    
    /**
     * Overwrite file in transaction context 
     */
    public void overwriteFile(ContentFileTransactionContext context, InputStream inputStream, String path, String oldPath) throws IOException {
        
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(path);
        Preconditions.checkNotNull(oldPath);
        
        File contentDirectory = context.getContentDirectory();
        File deletedTmpDirectory = context.getDeletedTmpDirectory();
        
        File oldFile = new File(contentDirectory, oldPath);
        
        FileUtils.copyFile(oldFile, new File(deletedTmpDirectory, oldPath));
        oldFile.delete();
        
        
        FileUtils.copyInputStreamToFile(inputStream, new File(contentDirectory, path));
        context.addAddedFile(path);
    }
}
