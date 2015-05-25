package pl.edu.icm.saos.persistence.common;

/**
 * Exception thrown when attempting to persist generated entity
 * 
 * @author madryk
 */
public class GeneratedEntityPersistException extends RuntimeException {

    private static final long serialVersionUID = -2887922875899910562L;
    

    //------------------------ CONSTRUCTORS --------------------------
    
    public GeneratedEntityPersistException(Object entity) {
        super("Detected attempt to persist a generated entity " + entity + ". Persisting of generated entities is not allowed");
    }

}
