package pl.edu.icm.saos.search.config.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.solr.core.ConfigSolr;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.SolrConfigurationException;
import pl.edu.icm.saos.search.util.SolrConstants;

@Service
public class CoreContainerFactory {

    private static final String CONF_BASE_CLASSPATH = "pl/edu/icm/saos/search/config";
    
    @Autowired
    private Environment environment;
    
    private CoreContainer coreContainer;
    
    public CoreContainer fetchCoreContainer() {
        if (coreContainer != null) {
            return coreContainer;
        }
        ClassPathResource solrConfFile = new ClassPathResource(CONF_BASE_CLASSPATH + "/" + SolrConstants.SOLR_CONFIG_FILENAME);
        String solrHome = environment.getProperty("solr.index.configuration.home");
        
        SolrResourceLoader loader = new SolrResourceLoader(solrHome);
        
        try (InputStream solrConfStream = solrConfFile.getInputStream()) {
            ConfigSolr cs = ConfigSolr.fromInputStream(loader, solrConfStream);
            coreContainer = new CoreContainer(loader, cs);
        } catch (IOException e) {
            IOUtils.closeQuietly(loader);
            throw new SolrConfigurationException("Unable to read solr.xml file", e);
        }
        
        return coreContainer;
    }
    
    
}
