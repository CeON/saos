package pl.edu.icm.saos.search.config.model;

/**
 * Thrown when application was unable to prepare solr instance
 * 
 * @author madryk
 */
public class SolrConfigurationException extends RuntimeException {

    private static final long serialVersionUID = -2936543837495005943L;


    public SolrConfigurationException(String message) {
        super(message);
    }
    
    public SolrConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
