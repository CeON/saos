package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.io.FileUtils;

import com.google.common.io.Files;

/**
 * Handler providing functionality to perform transaction like operations on files.
 * It can perform series of adding and deleting files within destination directory.
 * Next these operations can be commited or rollbacked
 * (using {@link FilesOperationsTransactionManager#commit(String)} or {@link FilesOperationsTransactionManager#rollback(String)}).
 * Commits approves all changes and applies it to destination directory.
 * Rollbacks withdraws all changes and leaves destination directory in a state
 * as it was at the beginning.
 * 
 * This class uses internally some temporary directory to store files that will be deleted by commit operation.
 * 
 * @author madryk
 */
public class FilesOperationsTransaction {
    
    private File deletedTmpDirectory;
    
    private Queue<String> addedFilesList;
    
    private File contentDirectory;
    
    
    //------------------------ CONTRUCTORS --------------------------
    
    FilesOperationsTransaction(File contentDirectory) {
        this.contentDirectory = contentDirectory;
        
        deletedTmpDirectory = Files.createTempDir();
        addedFilesList = new LinkedList<String>();
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public void addFile(InputStream inputStream, String path) throws IOException {
        FileUtils.copyInputStreamToFile(inputStream, new File(contentDirectory, path));
        addedFilesList.add(path);
    }
    
    public void overwriteFile(InputStream inputStream, String path, String oldPath) throws IOException {
        FileUtils.copyFile(new File(contentDirectory, oldPath), new File(deletedTmpDirectory, oldPath));
        FileUtils.deleteQuietly(new File(contentDirectory, oldPath));
        
        FileUtils.copyInputStreamToFile(inputStream, new File(contentDirectory, path));
        addedFilesList.add(path);
    }
    
    //------------------------ PACKAGE-PRIVATE --------------------------
    
    void commit() throws IOException {
        FileUtils.deleteDirectory(deletedTmpDirectory);
    }
    
    void rollback() throws IOException {
        while(!addedFilesList.isEmpty()) {
            String addedFilePath = addedFilesList.poll();
            FileUtils.deleteQuietly(new File(contentDirectory, addedFilePath));
        }
        FileUtils.copyDirectory(deletedTmpDirectory, contentDirectory);
        FileUtils.deleteDirectory(deletedTmpDirectory);
    }
    
}
