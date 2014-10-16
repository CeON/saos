package pl.edu.icm.saos.api.exceptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.edu.icm.saos.api.exceptions.status.ErrorStatus;
import pl.edu.icm.saos.api.response.representations.ErrorRepresentation;

import java.util.Map;

/**
 * @author pavtel
 */
public class ControllersEntityExceptionHandler {

    @Value("${restful.api.error.documentation.site}")
    private String errorDocumentationSite;


    //----------- BUSINESS METHODS -------------------
    @ExceptionHandler(ElementDoesNotExistException.class)
    public ResponseEntity<Map<String, Object>> handelIllegalArgumentError(Exception ex){
        ErrorStatus errorStatus = ErrorStatus.ELEMENT_DOES_NOT_EXIST_ERROR;

        ErrorRepresentation.Builder errorRepresentation = create(errorStatus , ex);

        return createErrorResponse(errorRepresentation, errorStatus);
    }

    @ExceptionHandler(WrongRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleWrongRequestParameterError(WrongRequestParameterException ex){
        ErrorStatus errorStatus = ErrorStatus.WRONG_REQUEST_PARAMETER_ERROR;

        ErrorRepresentation.Builder builder = create(errorStatus, ex);
        builder.propertyName(ex.getParameterName());

        return createErrorResponse(builder, errorStatus);
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<Map<String, Object>> handleGeneralError(Exception ex) {
        ErrorStatus errorStatus = ErrorStatus.GENERAL_INTERNAL_ERROR;

        ErrorRepresentation.Builder builder = create(errorStatus, ex);

        return createErrorResponse(builder, errorStatus);
    }


    //-----------END BUSINESS METHODS -----------------


    //----------- PRIVATE ----------------------------

    private ErrorRepresentation.Builder create(ErrorStatus errorStatus, Exception ex){
        ErrorRepresentation.Builder builder = new ErrorRepresentation.Builder();
        builder.httpStatus(errorStatus.httpStatusValue())
                .message(ex.getMessage())
                .name(errorStatus.errorName())
                .moreInfo(errorDocumentationSite+errorStatus.name());

        return builder;
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(ErrorRepresentation.Builder builder, ErrorStatus errorStatus){
        Map<String, Object> representation = builder.build();
        return new ResponseEntity<>(representation, errorStatus.httpStatus());
    }
    //----------- END PRIVATE --------------------------


    //----------------- setters ----------------------
    public void setErrorDocumentationSite(String errorDocumentationSite) {
        this.errorDocumentationSite = errorDocumentationSite;
    }
}
