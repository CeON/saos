package pl.edu.icm.saos.search;

import static junit.framework.Assert.*;
import java.io.File;
import java.io.IOException;

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
}
