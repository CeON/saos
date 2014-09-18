package pl.edu.icm.saos.search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;

import pl.edu.icm.saos.search.util.SearchIOUtils;

import com.google.common.io.Files;

/**
 * @author madryk
 */
public class FilesTestSupport {
    
    private List<File> createdTemporaryDirectories = new ArrayList<File>();
    
    @After
    public void cleanup() {
        for (File dir : createdTemporaryDirectories) {
            SearchIOUtils.removeDirectoryRecursive(dir);
        }
        createdTemporaryDirectories.clear();
    }
    
    protected File createTempDir() {
        File tmpDir = Files.createTempDir();
        createdTemporaryDirectories.add(tmpDir);
        
        return tmpDir;
    }
}
