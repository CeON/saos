package pl.edu.icm.saos.search.config;

import org.apache.solr.core.CoreContainer;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;
import pl.edu.icm.saos.search.config.service.IndexReloader;

/**
 * @author madryk
 */
public class EmbeddedSolrIndexReloader implements IndexReloader {
    
    private CoreContainer coreContainer;
    
    @Override
    public void reloadIndex(IndexConfiguration indexConfiguration) {
        coreContainer.load();
    }

    public void setCoreContainer(CoreContainer coreContainer) {
        this.coreContainer = coreContainer;
    }

}
