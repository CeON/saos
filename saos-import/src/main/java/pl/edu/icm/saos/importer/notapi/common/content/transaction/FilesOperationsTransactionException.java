package pl.edu.icm.saos.importer.notapi.common.content.transaction;

public class FilesOperationsTransactionException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    
    //------------------------ CONSTRUCTORS --------------------------

    public FilesOperationsTransactionException(String message) {
        super(message);
    }
    
    public FilesOperationsTransactionException(Throwable cause) {
        super(cause);
    }
    
    public FilesOperationsTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
