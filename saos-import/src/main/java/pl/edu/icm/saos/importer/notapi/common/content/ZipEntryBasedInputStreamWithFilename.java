package pl.edu.icm.saos.importer.notapi.common.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Class representing input stream of file inside zip archive with its name.
 * 
 * @author madryk
 */
public class ZipEntryBasedInputStreamWithFilename implements InputStreamWithFilename {

    private ZipFile zipFile;
    
    private ZipEntry zipEntry;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    /**
     * Creates {@link InputStreamWithFilename} object based on {@link ZipFile}
     * and one of its {@link ZipEntry}
     */
    public ZipEntryBasedInputStreamWithFilename(ZipFile zipFile, ZipEntry zipEntry) {
        this.zipFile = zipFile;
        this.zipEntry = zipEntry;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    @Override
    public InputStream getInputStream() throws IOException {
        return zipFile.getInputStream(zipEntry);
    }
    
    @Override
    public String getFilename() {
        return zipEntry.getName();
    }
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Closes zip file.<br/>
     * 
     * Closing zip file will close all of input streams previously
     * returned by {@link #getInputStream()} also.
     * 
     */
    @Override
    public void close() throws IOException {
        zipFile.close();
    }

}
