package pl.edu.icm.saos.api.exceptions.status;

import org.springframework.http.HttpStatus;

/**
 * Represents app error's status dictionary
 * @author pavtel
 */
public enum  ErrorStatus {
    /**
     * Represents internal server error
     */
    GENERAL_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL ERROR", "50011"),

    /**
     * Represents situation when some of the request parameter had wrong value.
     * For example parameter 'pageLimit' has too big value.
     */
    WRONG_REQUEST_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "WRONG REQUEST PARAMETER", "40011"),

    /**
     * Represents situation when element with given id does not exist.
     * For example judgment with id '-134';
     */
    ELEMENT_DOES_NOT_EXIST_ERROR(HttpStatus.NOT_FOUND, "ELEMENT DOES NOT EXISTS", "40411");

    private HttpStatus httpStatus;
    private String errorName;
    private String linkSuffix;

    ErrorStatus(HttpStatus httpStatus, String errorName, String linkSuffix) {
        this.httpStatus = httpStatus;
        this.errorName = errorName;
        this.linkSuffix = linkSuffix;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    public String httpStatusValue(){
        return String.valueOf(httpStatus.value());
    }

    public String errorName() {
        return errorName;
    }

    public String linkSuffix() {
        return linkSuffix;
    }
}
