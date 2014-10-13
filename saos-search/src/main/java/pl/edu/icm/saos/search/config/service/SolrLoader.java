package pl.edu.icm.saos.search.config.service;

import java.util.List;

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

    private SolrHomeLocationPolicy solrHomeLocationPolicy;

    private SolrIndexConfigurationCopier indexConfigurationCopier;

    private IndexReloader indexReloader;

    private List<IndexConfiguration> indexesConfigurations;

    @Value("${solr.index.configuration.copy}")
    private boolean copyConfiguration = false;

    
    public void load() {

        for (IndexConfiguration indexConfiguration : indexesConfigurations) {
            if (copyConfiguration) {
                indexConfigurationCopier.copyIndexConfiguration(indexConfiguration, solrHomeLocationPolicy.getSolrHome());
            }

            indexReloader.reloadIndex(indexConfiguration);
        }
    }

    public void shutdown() {

        for (IndexConfiguration indexConfiguration : indexesConfigurations) {
            if (copyConfiguration) {
                indexConfigurationCopier.cleanupIndexConfiguration(indexConfiguration, solrHomeLocationPolicy.getSolrHome());
            }
        }
        solrHomeLocationPolicy.cleanup();
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
    public void setSolrHomeLocationPolicy(
            SolrHomeLocationPolicy solrHomeLocationPolicy) {
        this.solrHomeLocationPolicy = solrHomeLocationPolicy;
    }

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
    public void setIndexesConfigurations(
            List<IndexConfiguration> indexesConfigurations) {
        this.indexesConfigurations = indexesConfigurations;
    }

    public void setCopyConfiguration(boolean copyConfiguration) {
        this.copyConfiguration = copyConfiguration;
    }


}
