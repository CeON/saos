package pl.edu.icm.saos.search.config.service;

import java.io.File;

import org.jadira.usertype.spi.utils.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.SolrConfigurationException;

import com.google.common.io.Files;

/**
 * Controls creation of solr home folder.
 * When solr home in no longer needed then {@link #cleanup()} should be called.
 * @author madryk
 */
@Service
public class SolrHomeLocationPolicy {

    @Value("${solr.index.configuration.home:}")
    private String configurationPath;
    
    private File solrHome;
    
    
    public String fetchSolrHome() {
        if (solrHome != null) {
            return solrHome.getAbsolutePath();
        }
        
        if (StringUtils.isEmpty(configurationPath)) {
            solrHome = Files.createTempDir();
            return solrHome.getAbsolutePath();
        } else {
            solrHome = new File(configurationPath);
            createDirectory(solrHome);
        }
        return configurationPath;
    }
    
    public void cleanup() {
        solrHome.delete();
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void createDirectory(File directory) {

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new SolrConfigurationException("Unable to create directory " + directory.getAbsolutePath());
            }
        } else if (directory.isFile()) {
            throw new SolrConfigurationException("Found file at location " + directory.getAbsolutePath() + " but expected a directory");
        }
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setConfigurationPath(String configurationPath) {
        this.configurationPath = configurationPath;
    }
}
