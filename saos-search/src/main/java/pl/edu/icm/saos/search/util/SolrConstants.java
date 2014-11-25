package pl.edu.icm.saos.search.util;

/**
 * Constants for index configuration and searching with Solr.
 * @author madryk
 */
public interface SolrConstants {

    String DEFAULT_QUERY = "*:*";
    String RELEVANCE_SORT_NAME = "score";
    
    String SOLR_CONFIG_FILENAME = "solr.xml";
    String INDEX_CONFIG_DIRECTORY_NAME = "conf";
    String INDEX_PROPERTIES_FILENAME = "core.properties";
}
