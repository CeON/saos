package pl.edu.icm.saos.search.config.service;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;

/**
 * @author madryk
 */
public interface IndexReloader {

    void reloadIndex(IndexConfiguration indexConfiguration);
}
