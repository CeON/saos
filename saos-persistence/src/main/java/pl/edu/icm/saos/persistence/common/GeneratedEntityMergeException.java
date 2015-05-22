package pl.edu.icm.saos.persistence.common;

/**
 * Exception thrown when attempting to merge generated intity into hibernate session
 * 
 * @author madryk
 */
public class GeneratedEntityMergeException extends RuntimeException {

    private static final long serialVersionUID = 7156528396044589039L;

    
    //------------------------ CONSTRUCTORS --------------------------
    
    public GeneratedEntityMergeException(Object entity) {
        super("Detected attempt to merge generated entity " + entity + ". Merging generated entities is not allowed");
    }
}
