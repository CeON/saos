package pl.edu.icm.saos.importer.notapi.common.content;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class representing input stream of file with its name
 * 
 * @author madryk
 */
public class InputStreamWithFilename implements Closeable  {

    private InputStream inputStream;
    
    private String filename;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public InputStreamWithFilename(InputStream inputStream, String filename) {
        this.inputStream = inputStream;
        this.filename = filename;
    }

    
    //------------------------ GETTERS --------------------------
    
    public InputStream getInputStream() {
        return inputStream;
    }

    public String getFilename() {
        return filename;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
