package pl.edu.icm.saos.search;

import static junit.framework.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

/**
 * @author madryk
 */
public class FilesAssert {

    private FilesAssert() { }
    
    public static void assertIsDirectory(File actual) {
        assertTrue(actual.exists());
        assertTrue(actual.isDirectory());
    }
    
    public static void assertEmptyDirectory(File actual) {
        assertTrue(actual.exists());
        assertTrue(actual.isDirectory());
        assertEquals(0, actual.list().length);
    }
    
    public static void assertFile(File actual) {
        assertTrue(actual.exists());
        assertTrue(actual.isFile());
    }
    
    public static void assertFile(File actual, String expectedContent) throws IOException {
        assertTrue(actual.exists());
        assertTrue(actual.isFile());
        String fileContent = FileUtils.readFileToString(actual, "utf-8");
        assertEquals(expectedContent, fileContent);
    }
    
    public static void assertPropertyFile(File actual, Properties properties) throws IOException {
        assertTrue(actual.exists());
        assertTrue(actual.isFile());
        
        Properties actualProperties = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(actual);
            actualProperties.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        
        assertEquals(properties, actualProperties);
    }
}
