package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Context of transaction for operations on files.
 * 
 * @author madryk
 */
public class ContentFileTransactionContext {

    private File deletedTmpDirectory;
    
    private Queue<String> addedFilesQueue;
    
    private File contentDirectory;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public ContentFileTransactionContext(File contentDirectory, File deletedTmpDirectory) {
        this.contentDirectory = contentDirectory;
        this.deletedTmpDirectory = deletedTmpDirectory;
        addedFilesQueue = new LinkedList<String>();
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public File getDeletedTmpDirectory() {
        return deletedTmpDirectory;
    }
    
    public Queue<String> getAddedFilesQueue() {
        return addedFilesQueue;
    }
    
    public File getContentDirectory() {
        return contentDirectory;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public void addAddedFile(String path) {
        addedFilesQueue.add(path);
    }
    
    public String pollAddedFileFromQueue() {
        return addedFilesQueue.poll();
    }
    
    public boolean isAddedFilesQueueEmpty() {
        return addedFilesQueue.isEmpty();
    }
    
}
