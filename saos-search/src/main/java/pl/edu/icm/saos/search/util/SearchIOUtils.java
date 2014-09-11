package pl.edu.icm.saos.search.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * @author madryk
 */
public class SearchIOUtils {

    private static final Logger log = LoggerFactory.getLogger(SearchIOUtils.class);

    private SearchIOUtils() { }

    public static void copyProperties(Properties properties, File target) throws IOException {
        target.createNewFile();
        OutputStream outputStream = new FileOutputStream(target);
        properties.store(outputStream, null);
        outputStream.close();
    }

    public static void copyResource(Resource source, File target) throws IOException {
        target.createNewFile();
        InputStream inputStream = source.getInputStream();
        OutputStream outputStream = new FileOutputStream(target);
        IOUtils.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }

    public static void removeDirectoryRecursive(File target) {
        if (target.isDirectory()) {
            File[] children = target.listFiles();
            for (File child : children) {
                if (child.isDirectory()) {
                    removeDirectoryRecursive(child);
                } else {
                    if (!child.delete()) {
                        log.warn("unable to remove {}", target.getAbsolutePath());
                    }
                }
            }
        }
        if (!target.delete()) {
            log.warn("unable to remove {}", target.getAbsolutePath());
        }

    }
}
