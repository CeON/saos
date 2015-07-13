package pl.edu.icm.saos.api.services.exceptions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;

import pl.edu.icm.saos.api.services.exceptions.status.ErrorReason;
import pl.edu.icm.saos.api.services.representations.ErrorRepresentation;
import pl.edu.icm.saos.common.json.JsonFormatter;

/**
 * Exception handler for restful api controllers.
 * Every controller in restful api should extend this handler.
 * @author pavtel
 */
public class ControllersEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ControllersEntityExceptionHandler.class);


    private JsonFormatter jsonFormatter;


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
            String message = String.format("parameter '%s' : can't have value '%s'", fieldError.getField(), fieldError.getRejectedValue());
            builder.message(message);
        }


        return createErrorResponse(builder, errorStatus);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatchError(TypeMismatchException ex){
        ErrorReason errorStatus = ErrorReason.WRONG_REQUEST_PARAMETER_ERROR;
        
        ErrorRepresentation.Builder builder = create(errorStatus, ex);
        builder.propertyName(ex.getPropertyName());
        String message = String.format("incorrect parameter value '%s'", ex.getValue());
        builder.message(message);
        
        return createErrorResponse(builder, errorStatus);
    }

    @ExceptionHandler(PageDoesNotExistException.class)
    public ResponseEntity<Map<String, Object>> handleNotExisingPageError(Exception ex){
        ErrorReason errorStatus = ErrorReason.PAGE_DOES_NOT_EXIST_ERROR;

        ErrorRepresentation.Builder errorRepresentation = create(errorStatus , ex);

        return createErrorResponse(errorRepresentation, errorStatus);
    }

    @ExceptionHandler(MethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleNotSupportedMethodError(MethodNotSupportedException ex) {
        ErrorReason errorStatus = ErrorReason.UNSUPPORTED_HTTP_METHOD_ERROR;
        
        ErrorRepresentation.Builder builder = create(errorStatus, ex);
        String message = String.format("Not supported method '%s', only %s allowed", ex.getRequestedMethod(), ex.getSupportedMethod());
        builder.message(message);
        
        return createErrorResponse(builder, errorStatus);
    }

    @ExceptionHandler(MediaTypeNotSupportedException.class)
    public void handleInvalidAcceptHeaderError(HttpServletRequest request, HttpServletResponse response, MediaTypeNotSupportedException ex) throws IOException {
        
        ErrorReason errorStatus = ErrorReason.UNSUPPORTED_MEDIA_TYPE_ERROR;
        
        ErrorRepresentation.Builder builder = create(errorStatus, ex);
        
        String message = String.format("Not acceptable media type '%s', only %s allowed", ex.getAcceptHeader(), ex.getSupportedMediaType());
        builder.message(message);
        
        
        String errorBody = jsonFormatter.formatObject(builder.build());
      
        response.addHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        
        PrintWriter writer = response.getWriter();
        
        writer.print(errorBody);
        writer.flush();
        writer.close();
        
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<Map<String, Object>> handleGeneralError(Exception ex) {

        ErrorReason errorStatus = ErrorReason.GENERAL_INTERNAL_ERROR;

        ErrorRepresentation.Builder builder = create(errorStatus, ex);


        log.error("general exception: "+ ExceptionUtils.getStackTrace(ex));

        return createErrorResponse(builder, errorStatus);
    }




    //------------------------ PRIVATE --------------------------

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

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJsonFormatter(JsonFormatter jsonFormatter) {
        this.jsonFormatter = jsonFormatter;
    }
}
