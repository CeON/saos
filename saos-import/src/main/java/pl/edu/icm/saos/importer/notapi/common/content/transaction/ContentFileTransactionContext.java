package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import com.google.common.base.Preconditions;

/**
 * Context of transaction for operations on judgment content files.
 * 
 * @author madryk
 */
public class ContentFileTransactionContext {

    private File deletedTmpDirectory;
    
    private Queue<String> addedFilesQueue;
    
    private File contentDirectory;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    /**
     * Creates new transaction context
     * 
     * @param contentDirectory - directory where content files are stored
     * @param deletedTmpDirectory - used to store deleted files from content directory
     *     within transaction context.
     */
    public ContentFileTransactionContext(File contentDirectory, File deletedTmpDirectory) {
        Preconditions.checkNotNull(contentDirectory);
        Preconditions.checkNotNull(deletedTmpDirectory);
        
        this.contentDirectory = contentDirectory;
        this.deletedTmpDirectory = deletedTmpDirectory;
        addedFilesQueue = new LinkedList<String>();
    }
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * Returns temporary directory that contains deleted files within current transaction context.
     */
    public File getDeletedTmpDirectory() {
        return deletedTmpDirectory;
    }
    
    /**
     * Returns queue of file paths which was added within current transaction context.
     * Paths are relative to {@link #getContentDirectory()}.
     */
    public Queue<String> getAddedFilesQueue() {
        return addedFilesQueue;
    }
    
    /**
     * Returns directory where judgments content files are stored
     */
    public File getContentDirectory() {
        return contentDirectory;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Adds file path to queue
     */
    public void addAddedFile(String path) {
        addedFilesQueue.add(path);
    }
    
    /**
     * Polls added file path from queue
     */
    public String pollAddedFileFromQueue() {
        return addedFilesQueue.poll();
    }
    
    /**
     * Returns {@literal true} if queue of added file paths is empty
     */
    public boolean isAddedFilesQueueEmpty() {
        return addedFilesQueue.isEmpty();
    }
    
}
