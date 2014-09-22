package pl.edu.icm.saos.search.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import static pl.edu.icm.saos.search.FilesAssert.*;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import pl.edu.icm.saos.search.FilesTestSupport;

/**
 * @author madryk
 */
public class SearchIOUtilTest extends FilesTestSupport {

    @Test
    public void copyProperties() throws IOException {
        File tmpDir = super.createTempDir();
        File targetPropertyFile = new File(tmpDir, "propertyFile.properties");
        Properties properties = new Properties();
        properties.put("firstKey", "firstValue");
        properties.put("secondKey", "secondValue");
        
        SearchIOUtils.copyProperties(properties, targetPropertyFile);
        
        assertPropertyFile(targetPropertyFile, properties);
    }
    
    @Test
    public void copyResource() throws IOException {
        File tmpDir = super.createTempDir();
        File targetFile = new File(tmpDir, "someFile.txt");
        Resource resource = new ByteArrayResource("some resource".getBytes());
        
        SearchIOUtils.copyResource(resource, targetFile);
        
        assertFile(targetFile, "some resource");
    }
}
