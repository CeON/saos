package pl.edu.icm.saos.search.config.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;

/**
 * @author madryk
 */
@Service
public class SolrLoader implements ApplicationListener<ApplicationContextEvent> {

    private SolrIndexConfigurationCopier indexConfigurationCopier;

    private IndexReloader indexReloader;

    private List<IndexConfiguration> indexesConfigurations;

    @Value("${solr.index.configuration.copy}")
    private boolean copyConfiguration = false;

    @Value("${solr.index.configuration.home}")
    private String configurationPath;

    
    public void load() {

        for (IndexConfiguration indexConfiguration : indexesConfigurations) {
            if (copyConfiguration && StringUtils.isNotBlank(configurationPath)) {
                indexConfigurationCopier.copyIndexConfiguration(indexConfiguration, configurationPath);
            }

            indexReloader.reloadIndex(indexConfiguration);
        }
    }

    public void shutdown() {

        for (IndexConfiguration indexConfiguration : indexesConfigurations) {
            if (copyConfiguration && StringUtils.isNotBlank(configurationPath)) {
                indexConfigurationCopier.cleanupIndexConfiguration(indexConfiguration, configurationPath);
            }
        }
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            load();
        } else if (event instanceof ContextClosedEvent) {
            shutdown();
        }
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setIndexConfigurationCopier(
            SolrIndexConfigurationCopier indexConfigurationCopier) {
        this.indexConfigurationCopier = indexConfigurationCopier;
    }

    @Autowired
    public void setIndexReloader(IndexReloader indexReloader) {
        this.indexReloader = indexReloader;
    }

    @Autowired
    public void setIndexesConfiguration(
            List<IndexConfiguration> indexesConfiguration) {
        this.indexesConfigurations = indexesConfiguration;
    }

    public void setCopyConfiguration(boolean copyConfiguration) {
        this.copyConfiguration = copyConfiguration;
    }

    public void setConfigurationPath(String configurationPath) {
        this.configurationPath = configurationPath;
    }

}
