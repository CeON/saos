package pl.edu.icm.saos.search.config.service;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;
import pl.edu.icm.saos.search.config.model.SolrConfigurationException;
import pl.edu.icm.saos.search.util.SearchIOUtils;
import pl.edu.icm.saos.search.util.SolrConstants;

/**
 * @author madryk
 */
@Service
public class SolrIndexConfigurationCopier {

    private static Logger log = LoggerFactory.getLogger(SolrIndexConfigurationCopier.class);

    public void copyIndexConfiguration(IndexConfiguration indexConfiguration, String configurationPath) {
        log.info("Copying configuration files for index with name {}", indexConfiguration.getName());

        File confHomeDir = new File(configurationPath);

        File indexDir = new File(confHomeDir, indexConfiguration.getInstanceDir());
        createDirectory(indexDir);

        if (indexConfiguration.isCreateIndexPropertyFile()) {
            createIndexPropertyFile(indexDir, indexConfiguration);
        }

        File indexConfDir = new File(indexDir, SolrConstants.INDEX_CONFIG_DIRECTORY_NAME);
        createDirectory(indexConfDir);

        for (Resource configurationFile : indexConfiguration.getConfigurationFiles()) {
            File targetFile = new File(indexConfDir, configurationFile.getFilename());
            try {
                SearchIOUtils.copyResource(configurationFile, targetFile);
            } catch (IOException e) {
                throw new SolrConfigurationException("Unable to create configuration file " + targetFile.getAbsolutePath() + 
                        " for " + indexConfiguration.getName() + " index", e);
            }
        }
    }

    public void cleanupIndexConfiguration(IndexConfiguration indexConfiguration, String configurationPath) {
        if (indexConfiguration.isPersistent()) {
            return;
        }
        log.info("Cleaning configuration files and data for index with name {}", indexConfiguration.getName());

        File confHomeDir = new File(configurationPath);
        File indexDir = new File(confHomeDir, indexConfiguration.getInstanceDir());
        FileUtils.deleteQuietly(indexDir);
    }


    //------------------------ PRIVATE --------------------------
    
    private void createIndexPropertyFile(File indexDir, IndexConfiguration indexConfiguration) {
        File targetFile = new File(indexDir, SolrConstants.INDEX_PROPERTIES_FILENAME);

        Properties indexProperties = new Properties();
        indexProperties.put("name", indexConfiguration.getName());

        try {
            SearchIOUtils.copyProperties(indexProperties, targetFile);
        } catch (IOException e) {
            throw new SolrConfigurationException("Unable to create configuration file " + targetFile.getAbsolutePath() + 
                    " for " + indexConfiguration.getName() + " index", e);
        }
    }

    private void createDirectory(File directory) {

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new SolrConfigurationException("Unable to create directory " + directory.getAbsolutePath());
            }
        } else if (directory.isFile()) {
            throw new SolrConfigurationException("Found file at location " + directory.getAbsolutePath() + " but expected a directory");
        }
    }

}
