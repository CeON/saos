package pl.edu.icm.saos.search.config.service;

import static pl.edu.icm.saos.search.FilesAssert.assertEmptyDirectory;
import static pl.edu.icm.saos.search.FilesAssert.assertFile;
import static pl.edu.icm.saos.search.FilesAssert.assertIsDirectory;
import static pl.edu.icm.saos.search.FilesAssert.assertPropertyFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;

import com.google.common.io.Files;

/**
 * @author madryk
 */
public class SolrIndexConfigurationCopierTest {

    private SolrIndexConfigurationCopier indexConfigurationCopier = new SolrIndexConfigurationCopier();
    
    
    @Test
    public void copyIndexConfiguration() throws IOException {
        File tmpDir = Files.createTempDir();
        NamedByteArrayResource firstResource = new NamedByteArrayResource("some resource content".getBytes(), "filename.txt");
        NamedByteArrayResource secondResource = new NamedByteArrayResource("some other resource content".getBytes(), "secondFilename.txt");
        IndexConfiguration indexConfiguration = createIndexConfiguration("indexName", "indexDirectory", firstResource, secondResource);
        
        indexConfigurationCopier.copyIndexConfiguration(indexConfiguration, tmpDir.getAbsolutePath());
        
        assertIndexConfStructure(tmpDir, "indexDirectory");
        assertIndexConfFile(tmpDir, "indexDirectory", "filename.txt", "some resource content");
        assertIndexConfFile(tmpDir, "indexDirectory", "secondFilename.txt", "some other resource content");
        FileUtils.deleteDirectory(tmpDir);
    }
    
    @Test
    public void copyIndexConfiguration_WITH_PROPERTY_FILE() throws IOException {
        File tmpDir = Files.createTempDir();
        IndexConfiguration indexConfiguration = createIndexConfiguration("indexName", "indexDirectory");
        indexConfiguration.setCreateIndexPropertyFile(true);
        
        indexConfigurationCopier.copyIndexConfiguration(indexConfiguration, tmpDir.getAbsolutePath());
        
        assertIndexConfStructure(tmpDir, "indexDirectory");
        assertIndexPropertiesFile(tmpDir, "indexDirectory", "indexName");
        FileUtils.deleteDirectory(tmpDir);
    }
    
    @Test
    public void cleanupIndexConfiguration() throws IOException {
        File tmpDir = Files.createTempDir();
        NamedByteArrayResource firstResource = new NamedByteArrayResource("some resource content".getBytes(), "filename.txt");
        IndexConfiguration indexConfiguration = createIndexConfiguration("indexName", "indexDirectory", firstResource);
        indexConfiguration.setPersistent(false);
        indexConfigurationCopier.copyIndexConfiguration(indexConfiguration, tmpDir.getAbsolutePath());
        
        
        indexConfigurationCopier.cleanupIndexConfiguration(indexConfiguration, tmpDir.getAbsolutePath());
        
        
        assertEmptyDirectory(tmpDir);
        FileUtils.deleteDirectory(tmpDir);
    }
    
    
    //------------------------ PRIVATE: ASSERTS --------------------------
    
    private void assertIndexConfStructure(File baseDir, String indexDirectory) throws IOException {
        File indexDir = new File(baseDir, indexDirectory);
        assertIsDirectory(indexDir);
        
        File indexConfDir = new File(indexDir, "conf");
        assertIsDirectory(indexConfDir);
    }
    
    private void assertIndexConfFile(File baseDir, String indexDirectory, String filename, String content) throws IOException {
        File indexDir = new File(baseDir, indexDirectory);
        File indexConfDir = new File(indexDir, "conf");
        File confFile = new File(indexConfDir, filename);
        assertFile(confFile, content);
    }
    
    private void assertIndexPropertiesFile(File baseDir, String indexDirectory, String indexName) throws IOException {
        File indexDir = new File(baseDir, indexDirectory);
        File propertyFile = new File(indexDir, "core.properties");
        
        Properties expectedProperties = new Properties();
        expectedProperties.put("name", indexName);
        
        assertPropertyFile(propertyFile, expectedProperties);
    }
    
    
    //------------------------ PRIVATE: DATA CREATION --------------------------
    
    protected IndexConfiguration createIndexConfiguration(String indexName, String indexDir, Resource ... configResources) {
        IndexConfiguration indexConfiguration = new IndexConfiguration();
        indexConfiguration.setName(indexName);
        indexConfiguration.setInstanceDir(indexDir);
        indexConfiguration.setConfigurationFiles(Arrays.asList(configResources));
        
        return indexConfiguration;
    }
    
    
    //------------------------ HELPER --------------------------
    
    private class NamedByteArrayResource extends ByteArrayResource {
        
        private String name;
        
        public NamedByteArrayResource(byte[] byteArray, String name) {
            super(byteArray);
            this.name = name;
        }
        
        public NamedByteArrayResource(byte[] byteArray, String name, String description) {
            super(byteArray, description);
            this.name = name;
        }
        
        @Override
        public String getFilename() {
            return name;
        }
    }
}
