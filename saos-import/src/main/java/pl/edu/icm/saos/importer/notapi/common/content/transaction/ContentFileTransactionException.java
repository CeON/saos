package pl.edu.icm.saos.importer.notapi.common.content.transaction;

/**
 * Thrown when there was error in commit or rollback of content file transaction
 * 
 * @author madryk
 */
public class ContentFileTransactionException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    
    //------------------------ CONSTRUCTORS --------------------------

    public ContentFileTransactionException(String message) {
        super(message);
    }
    
    public ContentFileTransactionException(Throwable cause) {
        super(cause);
    }
    
    public ContentFileTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
