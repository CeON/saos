package pl.edu.icm.saos.search.config.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import pl.edu.icm.saos.search.config.model.SolrConfigurationException;

/**
 * Some helper methods for copying configuration files
 * 
 * @author madryk
 */
class SearchConfigurationFilesUtils {

    private SearchConfigurationFilesUtils() { }

    public static void copyProperties(Properties properties, File target) {
        
        Writer writer = null;
        try {
            writer =  new FileWriter(target);
            properties.store(writer, null);
        } catch (IOException e) {
            throw new SolrConfigurationException("Unable to create configuration file " + target.getAbsolutePath(), e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public static void copyResource(Resource source, File target) {
        
        try {
            FileUtils.copyInputStreamToFile(source.getInputStream(), target);
        } catch (IOException e) {
            throw new SolrConfigurationException("Unable to create configuration file " + target.getAbsolutePath(), e);
        }
    }

}
