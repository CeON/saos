package pl.edu.icm.saos.search.config.service;

import static pl.edu.icm.saos.search.FilesAssert.assertFile;
import static pl.edu.icm.saos.search.FilesAssert.assertPropertyFile;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import com.google.common.io.Files;

/**
 * @author madryk
 */
public class SearchConfigurationFilesUtilsTest {

    @Test
    public void copyProperties() throws IOException {
        File tmpDir = Files.createTempDir();
        File targetPropertyFile = new File(tmpDir, "propertyFile.properties");
        Properties properties = new Properties();
        properties.put("firstKey", "firstValue");
        properties.put("secondKey", "secondValue");
        
        SearchConfigurationFilesUtils.copyProperties(properties, targetPropertyFile);
        
        assertPropertyFile(targetPropertyFile, properties);
        FileUtils.deleteDirectory(tmpDir);
    }
    
    @Test
    public void copyResource() throws IOException {
        File tmpDir = Files.createTempDir();
        File targetFile = new File(tmpDir, "someFile.txt");
        Resource resource = new ByteArrayResource("some resource".getBytes());
        
        SearchConfigurationFilesUtils.copyResource(resource, targetFile);
        
        assertFile(targetFile, "some resource");
        FileUtils.deleteDirectory(tmpDir);
    }
}
