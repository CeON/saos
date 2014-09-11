package pl.edu.icm.saos.search.config;

import java.io.File;
import java.io.IOException;

import org.apache.solr.core.CoreContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.util.SearchIOUtils;
import pl.edu.icm.saos.search.util.SolrConstants;

@Service
public class CoreContainerFactory {

    private static final String CONF_BASE_CLASSPATH = "pl/edu/icm/saos/search/config";
    
    @Autowired
    private Environment environment;
    
    private CoreContainer coreContainer;
    
    public CoreContainer fetchCoreContainer() throws IOException {
        if (coreContainer != null) {
            return coreContainer;
        }
        ClassPathResource solrConfFile = new ClassPathResource(CONF_BASE_CLASSPATH + "/" + SolrConstants.SOLR_CONFIG_FILENAME);
        File f = File.createTempFile("solr", ".xml");
        SearchIOUtils.copyResource(solrConfFile, f);
        
        String solrHome = environment.getProperty("solr.index.configuration.home");
        coreContainer = CoreContainer.createAndLoad(solrHome, f);
        f.delete();
        return coreContainer;
    }
    
    
}
