package pl.edu.icm.saos.api.services.exceptions.status;

import org.springframework.http.HttpStatus;

/**
 * Represents app error's status dictionary
 * @author pavtel
 */
public enum  ErrorReason {
    /**
     * Represents internal server error
     */
    GENERAL_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL ERROR"),

    /**
     * Represents situation when some of the request parameter had wrong value.
     * For example parameter 'pageLimit' has too big value.
     */
    WRONG_REQUEST_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "WRONG REQUEST PARAMETER"),

    /**
     * Represents situation when element with given id does not exist.
     * For example judgment with id '-134';
     */
    ELEMENT_DOES_NOT_EXIST_ERROR(HttpStatus.NOT_FOUND, "ELEMENT DOES NOT EXIST"),
    
    PAGE_DOES_NOT_EXIST_ERROR(HttpStatus.NOT_FOUND, "PAGE DOES NOT EXIST"),
    
    UNSUPPORTED_HTTP_METHOD_ERROR(HttpStatus.METHOD_NOT_ALLOWED, "UNSUPPORTED HTTP METHOD"),
    
    UNSUPPORTED_MEDIA_TYPE_ERROR(HttpStatus.NOT_ACCEPTABLE, "UNSUPPORTED MEDIA TYPE");

    private HttpStatus httpStatus;
    private String errorReason;

    ErrorReason(HttpStatus httpStatus, String errorReason) {
        this.httpStatus = httpStatus;
        this.errorReason = errorReason;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    public String httpStatusValue(){
        return String.valueOf(httpStatus.value());
    }

    public String errorReason() {
        return errorReason;
    }

}
