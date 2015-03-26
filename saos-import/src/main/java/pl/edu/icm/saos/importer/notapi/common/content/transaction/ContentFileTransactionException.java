package pl.edu.icm.saos.importer.notapi.common.content.transaction;

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
