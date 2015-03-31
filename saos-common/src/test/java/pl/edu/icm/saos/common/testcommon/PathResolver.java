package pl.edu.icm.saos.common.testcommon;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;

/**
 * @author madryk
 */
public class PathResolver {

    //------------------------ CONSTRUCTORS --------------------------
    
    private PathResolver() { }
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Resolves provided classpath to absolute path of file or directory
     * in file system.
     * 
     * @param classpathLocation
     * @return absolute path to file or directory
     * @throws IllegalAccessException when file doesn't exists
     */
    public static String resolveToAbsolutePath(String classpathLocation) {
        try {
            File file = new ClassPathResource(classpathLocation).getFile();
            return file.getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
