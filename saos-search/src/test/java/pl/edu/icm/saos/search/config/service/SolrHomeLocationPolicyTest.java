package pl.edu.icm.saos.search.config.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.common.io.Files;

import static pl.edu.icm.saos.search.FilesAssert.*;
import static junit.framework.Assert.*;

/**
 * @author madryk
 */
public class SolrHomeLocationPolicyTest {

    private SolrHomeLocationPolicy solrHomeLocationPolicy = new SolrHomeLocationPolicy();
    
    @Test
    public void fetchSolrHome_TEMPORARY() throws IOException {
        // given
        solrHomeLocationPolicy.setConfigurationPath(null);
        
        // when fetchSolrHome
        String actualSolrHomePath = solrHomeLocationPolicy.fetchSolrHome();
        File actualSolrHome = new File(actualSolrHomePath);
        
        // then
        assertEmptyDirectory(actualSolrHome);
        File javaTmpDir = new File(System.getProperty("java.io.tmpdir"));
        assertTrue(FileUtils.directoryContains(javaTmpDir, actualSolrHome));
        
        
        // when cleanup
        solrHomeLocationPolicy.cleanup();
        // then
        assertFalse(actualSolrHome.exists());
    }
    
    @Test
    public void fetchSolrHome_CUSTOM() throws IOException {
        // given
        File solrCustomHome = Files.createTempDir();
        File solrHomeContentFile = new File(solrCustomHome, "someFile");
        solrHomeContentFile.createNewFile();
        solrHomeLocationPolicy.setConfigurationPath(solrCustomHome.getAbsolutePath());
        
        // when fetchSolrHome
        String actualSolrHomePath = solrHomeLocationPolicy.fetchSolrHome();
        File actualSolrHome = new File(actualSolrHomePath);
        
        // then
        assertEquals(solrCustomHome.getAbsolutePath(), actualSolrHomePath);
        
        
        // when cleanup not empty
        solrHomeLocationPolicy.cleanup();
        // then
        assertTrue(actualSolrHome.exists());
        
        FileUtils.deleteQuietly(solrCustomHome);
    }
}
