package pl.edu.icm.saos.search.config.service;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;

/**
 * Informs solr instance that index should be reloaded.
 * 
 * @author madryk
 */
public interface IndexReloader {

    /**
     * Reloads provided index. If index doesn't exist then it should be created.
     * @param indexConfiguration - definition of index that should be reloaded
     */
    void reloadIndex(IndexConfiguration indexConfiguration);
}
