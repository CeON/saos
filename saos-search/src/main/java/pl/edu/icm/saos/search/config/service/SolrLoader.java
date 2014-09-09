package pl.edu.icm.saos.search.config.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;

/**
 * @author madryk
 */
@Service
public class SolrLoader implements ApplicationListener<ContextRefreshedEvent> {
    
    @Autowired
    SolrIndexConfigurationCopier indexConfigurationCopier;
    
    @Autowired
    IndexReloader indexReloader;
    
    @Autowired List<IndexConfiguration> indexesConfiguration;

    @Value("${solr.index.configuration.copy}")
    private boolean copyConfiguration = false;
    
    @Value("${solr.index.configuration.home}")
    private String configurationPath;

    public void load() {

        for (IndexConfiguration indexConfiguration : indexesConfiguration) {
            if (copyConfiguration && StringUtils.isNotBlank(configurationPath)) {
                indexConfigurationCopier.copyIndexConfiguration(indexConfiguration, configurationPath);
            }

            indexReloader.reloadIndex(indexConfiguration);
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        load();
    }
    
}
