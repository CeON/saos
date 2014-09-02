package pl.edu.icm.saos.search.config.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;
import pl.edu.icm.saos.search.config.model.SolrConfigurationException;

@Component
public class SolrIndexConfigurationCopier {
    
    private static Logger log = LoggerFactory.getLogger(SolrIndexConfigurationCopier.class);
    
    private static final String INDEX_CONF_DIR_NAME = "conf";


    public void copyIndexConfiguration(IndexConfiguration indexConfiguration, String configurationPath) {
        log.info("Copying configuration files for index with name {}", indexConfiguration.getName());
        
        File confHomeDir = new File(configurationPath);
        
        File indexDir = new File(confHomeDir, indexConfiguration.getInstanceDir());
        createDirectory(indexDir);
        
        File indexConfDir = new File(indexDir, INDEX_CONF_DIR_NAME);
        createDirectory(indexConfDir);
        
        for (Resource configurationFile : indexConfiguration.getConfigurationFiles()) {
            File targetFile = new File(indexConfDir, configurationFile.getFilename());
            try {
                copyResource(configurationFile, targetFile);
            } catch (IOException e) {
                throw new SolrConfigurationException("Unable to create configuration file " + targetFile.getAbsolutePath() + 
                        " for " + indexConfiguration.getName() + " index", e);
            }
        }
    }
    
    private void createDirectory(File directory) {

        if (!directory.exists()) {
            if (!directory.mkdir()) {
                throw new SolrConfigurationException("Unable to create directory " + directory.getAbsolutePath());
            }
        } else if (directory.isFile()) {
            throw new SolrConfigurationException("Found file at location " + directory.getAbsolutePath() + " but expected a directory");
        }
    }
    
    private void copyResource(Resource source, File target) throws IOException {
        target.createNewFile();
        InputStream inputStream = source.getInputStream();
        OutputStream outputStream = new FileOutputStream(target);
        IOUtils.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }
}
