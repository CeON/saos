package pl.edu.icm.saos.search.config.service;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;

/**
 * Informs solr instance that index should be reloaded.
 * 
 * @author madryk
 */
public interface IndexReloader {

    void reloadIndex(IndexConfiguration indexConfiguration);
}
