package pl.edu.icm.saos.enrichment.upload;

/**
 * @author Łukasz Dumiszewski
 */

public final class EnrichmentTagUploadResponseMessages {

    private EnrichmentTagUploadResponseMessages() {
        throw new IllegalStateException("not to instantiate");
    }
    
    
    public static final String OK_MESSAGE = "Enrichment tags successfully uploaded";

    public static final String ERROR_ACCESS_DENIED = "access denied"; 
    public static final String ERROR_AUTHENTICATION_FAILED = "authentication failed"; 
    public static final String ERROR_UNSUPPORTED_HTTP_METHOD = "unsupported http method type"; 
    public static final String ERROR_UNSUPPORTED_HTTP_CONTENT_TYPE = "unsupported http content type"; 
    public static final String ERROR_EMPTY_DATA = "empty enrichment tag data"; 
    public static final String ERROR_INVALID_JSON_FORMAT = "invalid JSON format"; 
    public static final String ERROR_INVALID_DATA = "invalid data";
    public static final String ERROR_INTERVAL_SERVER_ERROR = "internal server error";
    public static final String ERROR_MAX_UPLOAD_SIZE_EXCEEDED = "max upload size exceeded";
    public static final String ERROR_SAME_TAG_ALREADY_UPLOADED = "same tag already uploaded"; 
    public static final String ERROR_IO = "I/O error";
}