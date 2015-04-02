package pl.edu.icm.saos.api.services.exceptions;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;

import pl.edu.icm.saos.api.services.exceptions.status.ErrorReason;
import pl.edu.icm.saos.api.services.representations.ErrorRepresentation;

/**
 * Exception handler for restful api controllers.
 * Every controller in restful api should extend this handler.
 * @author pavtel
 */
public class ControllersEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ControllersEntityExceptionHandler.class);



    //------------------------ LOGIC --------------------------
    @ExceptionHandler(ElementDoesNotExistException.class)
    public ResponseEntity<Map<String, Object>> handelIllegalArgumentError(Exception ex){
        ErrorReason errorStatus = ErrorReason.ELEMENT_DOES_NOT_EXIST_ERROR;

        ErrorRepresentation.Builder errorRepresentation = create(errorStatus , ex);

        return createErrorResponse(errorRepresentation, errorStatus);
    }

    @ExceptionHandler(WrongRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleWrongRequestParameterError(WrongRequestParameterException ex){
        ErrorReason errorStatus = ErrorReason.WRONG_REQUEST_PARAMETER_ERROR;

        ErrorRepresentation.Builder builder = create(errorStatus, ex);
        builder.propertyName(ex.getParameterName());

        return createErrorResponse(builder, errorStatus);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, Object>> handleConversionFailedError(BindException ex) {
        ErrorReason errorStatus = ErrorReason.WRONG_REQUEST_PARAMETER_ERROR;

        ErrorRepresentation.Builder builder = create(errorStatus, ex);

        if(causedByFieldError(ex)){
            FieldError fieldError = extractFieldError(ex);
            builder.propertyName(fieldError.getField());
            String message = String.format("parameter '%s' can't have value '%s'", fieldError.getField(), fieldError.getRejectedValue());
            builder.message(message);
        }


        return createErrorResponse(builder, errorStatus);
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<Map<String, Object>> handleGeneralError(Exception ex) {

        ErrorReason errorStatus = ErrorReason.GENERAL_INTERNAL_ERROR;

        ErrorRepresentation.Builder builder = create(errorStatus, ex);


        log.warn("general exception: "+ ExceptionUtils.getStackTrace(ex));

        return createErrorResponse(builder, errorStatus);
    }




    //----------- PRIVATE ----------------------------

    private ErrorRepresentation.Builder create(ErrorReason errorStatus, Exception ex){
        ErrorRepresentation.Builder builder = new ErrorRepresentation.Builder();
        builder.httpStatus(errorStatus.httpStatusValue())
                .message(ex.getMessage())
                .reason(errorStatus.errorReason());

        return builder;
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(ErrorRepresentation.Builder builder, ErrorReason errorStatus){
        Map<String, Object> representation = builder.build();
        return new ResponseEntity<>(representation, errorStatus.httpStatus());
    }

    private boolean causedByFieldError(BindException ex){
        if(!ex.getAllErrors().isEmpty()){
            ObjectError error = ex.getAllErrors().get(0);
            return error instanceof FieldError;
        } else {
            return false;
        }
    }

    private FieldError extractFieldError(BindException ex){
        return (FieldError) ex.getAllErrors().get(0);
    }
}
