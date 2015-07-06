package pl.edu.icm.saos.api.services.exceptions;

import org.springframework.http.MediaType;

/**
 * Thrown in rest api controllers when requested media type
 * defined in Accept header was not supported.
 * 
 * @author madryk
 */
public class MediaTypeNotSupportedException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    private String acceptHeader;

    private MediaType supportedMediaType;


    //------------------------ CONSTRUCTORS --------------------------

    public MediaTypeNotSupportedException(String acceptHeader, MediaType supportedMediaType) {
        super();
        this.acceptHeader = acceptHeader;
        this.supportedMediaType = supportedMediaType;
    }


    //------------------------ GETTERS --------------------------

    public String getAcceptHeader() {
        return acceptHeader;
    }

    public MediaType getSupportedMediaType() {
        return supportedMediaType;
    }
}
