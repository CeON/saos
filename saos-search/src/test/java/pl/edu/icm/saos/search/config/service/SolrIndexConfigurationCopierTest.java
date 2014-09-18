package pl.edu.icm.saos.search.config.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static pl.edu.icm.saos.search.FilesAssert.assertEmptyDirectory;
import static pl.edu.icm.saos.search.FilesAssert.assertFile;
import static pl.edu.icm.saos.search.FilesAssert.assertIsDirectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import pl.edu.icm.saos.search.FilesTestSupport;
import pl.edu.icm.saos.search.config.model.IndexConfiguration;

/**
 * @author madryk
 */
public class SolrIndexConfigurationCopierTest extends FilesTestSupport {

    private SolrIndexConfigurationCopier indexConfigurationCopier = new SolrIndexConfigurationCopier();
    
    
    @Test
    public void copyIndexConfiguration() throws IOException {
        File tmpDir = super.createTempDir();
        NamedByteArrayResource firstResource = new NamedByteArrayResource("some resource content".getBytes(), "filename.txt");
        NamedByteArrayResource secondResource = new NamedByteArrayResource("some other resource content".getBytes(), "secondFilename.txt");
        IndexConfiguration indexConfiguration = createIndexConfiguration("indexName", "indexDirectory", firstResource, secondResource);
        
        indexConfigurationCopier.copyIndexConfiguration(indexConfiguration, tmpDir.getAbsolutePath());
        
        assertIndexConfStructure(tmpDir, "indexDirectory");
        assertIndexConfFile(tmpDir, "indexDirectory", "filename.txt", "some resource content");
        assertIndexConfFile(tmpDir, "indexDirectory", "secondFilename.txt", "some other resource content");
    }
    
    @Test
    public void copyIndexConfiguration_WITH_PROPERTY_FILE() throws IOException {
        File tmpDir = super.createTempDir();
        IndexConfiguration indexConfiguration = createIndexConfiguration("indexName", "indexDirectory");
        indexConfiguration.setCreateIndexPropertyFile(true);
        
        indexConfigurationCopier.copyIndexConfiguration(indexConfiguration, tmpDir.getAbsolutePath());
        
        assertIndexConfStructure(tmpDir, "indexDirectory");
        assertIndexPropertiesFile(tmpDir, "indexDirectory", "indexName");
    }
    
    @Test
    public void cleanupIndexConfiguration() {
        File tmpDir = super.createTempDir();
        NamedByteArrayResource firstResource = new NamedByteArrayResource("some resource content".getBytes(), "filename.txt");
        IndexConfiguration indexConfiguration = createIndexConfiguration("indexName", "indexDirectory", firstResource);
        indexConfiguration.setPersistent(false);
        indexConfigurationCopier.copyIndexConfiguration(indexConfiguration, tmpDir.getAbsolutePath());
        
        
        indexConfigurationCopier.cleanupIndexConfiguration(indexConfiguration, tmpDir.getAbsolutePath());
        
        
        assertEmptyDirectory(tmpDir);
    }
    
    protected void assertIndexConfStructure(File baseDir, String indexDirectory) throws IOException {
        File indexDir = new File(baseDir, indexDirectory);
        assertIsDirectory(indexDir);
        
        File indexConfDir = new File(indexDir, "conf");
        assertIsDirectory(indexConfDir);
    }
    
    protected void assertIndexConfFile(File baseDir, String indexDirectory, String filename, String content) throws IOException {
        File indexDir = new File(baseDir, indexDirectory);
        File indexConfDir = new File(indexDir, "conf");
        File confFile = new File(indexConfDir, filename);
        assertFile(confFile, content);
    }
    
    protected void assertIndexPropertiesFile(File baseDir, String indexDirectory, String indexName) throws IOException {
        File indexDir = new File(baseDir, indexDirectory);
        File propertyFile = new File(indexDir, "core.properties");
        assertFile(propertyFile);
        
        InputStream is = new FileInputStream(propertyFile);
        Properties properties = new Properties();
        properties.load(is);
        is.close();
        
        assertTrue(properties.containsKey("name"));
        assertEquals(indexName, properties.getProperty("name"));
    }
    
    protected IndexConfiguration createIndexConfiguration(String indexName, String indexDir, Resource ... configResources) {
        IndexConfiguration indexConfiguration = new IndexConfiguration();
        indexConfiguration.setName(indexName);
        indexConfiguration.setInstanceDir(indexDir);
        indexConfiguration.setConfigurationFiles(Arrays.asList(configResources));
        
        return indexConfiguration;
    }
    
    
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
