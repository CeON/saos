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
    
    @Test
    public void initialize_TEMPORARY() throws IOException {
        // given
        SolrHomeLocationPolicy solrHomeLocationPolicy = new SolrHomeLocationPolicy();
        solrHomeLocationPolicy.setConfigurationPath(null);
        
        // when
        solrHomeLocationPolicy.initialize();
        String actualSolrHomePath = solrHomeLocationPolicy.getSolrHome();
        File actualSolrHome = new File(actualSolrHomePath);
        
        // then
        assertEmptyDirectory(actualSolrHome);
        File javaTmpDir = new File(System.getProperty("java.io.tmpdir"));
        assertTrue(FileUtils.directoryContains(javaTmpDir, actualSolrHome));
        
        
        FileUtils.deleteQuietly(actualSolrHome);
    }
    
    @Test
    public void initialize_CUSTOM() throws IOException {
        // given
        SolrHomeLocationPolicy solrHomeLocationPolicy = new SolrHomeLocationPolicy();
        File solrCustomHome = Files.createTempDir();
        solrHomeLocationPolicy.setConfigurationPath(solrCustomHome.getAbsolutePath());
        
        // when
        solrHomeLocationPolicy.initialize();
        String actualSolrHomePath = solrHomeLocationPolicy.getSolrHome();
        
        // then
        assertEquals(solrCustomHome.getAbsolutePath(), actualSolrHomePath);
        
        
        FileUtils.deleteQuietly(solrCustomHome);
    }
    
    @Test
    public void cleanup_EMPTY() {
        // given
        SolrHomeLocationPolicy solrHomeLocationPolicy = new SolrHomeLocationPolicy();
        solrHomeLocationPolicy.setConfigurationPath(null);
        solrHomeLocationPolicy.initialize();
        String solrHomePath = solrHomeLocationPolicy.getSolrHome();
        
        // when
        solrHomeLocationPolicy.cleanup();
        
        // then
        assertFalse(new File(solrHomePath).exists());
    }
    
    @Test
    public void cleanup_NOT_EMPTY() throws IOException {
        // given
        SolrHomeLocationPolicy solrHomeLocationPolicy = new SolrHomeLocationPolicy();
        solrHomeLocationPolicy.setConfigurationPath(null);
        solrHomeLocationPolicy.initialize();
        
        File solrHome = new File(solrHomeLocationPolicy.getSolrHome());
        File solrHomeContentFile = new File(solrHome, "someFile");
        solrHomeContentFile.createNewFile();
        
        // when
        solrHomeLocationPolicy.cleanup();
        
        // then
        assertTrue(solrHome.exists());
        
        FileUtils.deleteQuietly(solrHome);
    }
}
