package pl.edu.icm.saos.importer.notapi.common.content;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Interface representing input stream of file with its name.
 * 
 * @author madryk
 */
public interface InputStreamWithFilename extends Closeable {
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * Returns input stream that contains file content
     */
    InputStream getInputStream() throws IOException;

    /**
     * Returns name of file
     */
    String getFilename();
    
}
