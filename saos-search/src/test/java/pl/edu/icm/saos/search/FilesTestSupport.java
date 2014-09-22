package pl.edu.icm.saos.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;

import com.google.common.io.Files;

/**
 * @author madryk
 */
public class FilesTestSupport {
    
    private List<File> createdTemporaryDirectories = new ArrayList<File>();
    
    @After
    public void cleanup() throws IOException {
        for (File dir : createdTemporaryDirectories) {
            FileUtils.deleteDirectory(dir);
        }
        createdTemporaryDirectories.clear();
    }
    
    protected File createTempDir() {
        File tmpDir = Files.createTempDir();
        createdTemporaryDirectories.add(tmpDir);
        
        return tmpDir;
    }
}
