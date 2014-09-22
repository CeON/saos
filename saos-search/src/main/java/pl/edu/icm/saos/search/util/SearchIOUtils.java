package pl.edu.icm.saos.search.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;

/**
 * @author madryk
 */
public class SearchIOUtils {

    private SearchIOUtils() { }

    public static void copyProperties(Properties properties, File target) throws IOException {
        target.createNewFile();
        
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(target);
            properties.store(outputStream, null);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static void copyResource(Resource source, File target) throws IOException {
        target.createNewFile();
        
        InputStream inputStream = null;
        try {
            inputStream = source.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, target);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        
    }

}
