package pl.edu.icm.saos.search.search.exceptions;

/**
 * @author madryk
 */
public class SolrSearchExecutionException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public SolrSearchExecutionException(String message) {
        super(message);
    }
    
    public SolrSearchExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
