package pl.edu.icm.saos.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.edu.icm.saos.api.response.representations.ErrorRepresentation;

import java.util.Map;

/**
 * @author pavtel
 */
public class ControllersEntityExceptionHandler {


    //----------- BUSINESS METHODS -------------------
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handelIllegalArgumentError(Exception ex){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorRepresentation.Builder errorRepresentation = create(httpStatus, ILLEGAL_ARGUMENT_ERROR_CODE, ex);
        return createErrorResponse(errorRepresentation, httpStatus);
    }

    @ExceptionHandler(WrongRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleWrongRequestParameterError(WrongRequestParameterException ex){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorRepresentation.Builder builder = create(httpStatus, WRONG_REQUEST_PARAMETER_ERROR_CODE, ex);
        builder.property(ex.getParameterName());

        return createErrorResponse(builder, httpStatus);
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<Map<String, Object>> handleGeneralError(Exception ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorRepresentation.Builder builder = create(httpStatus, GENERAL_ERROR_CODE, ex);

        return createErrorResponse(builder, httpStatus);
    }


    //-----------END BUSINESS METHODS -----------------


    //----------- PRIVATE ----------------------------
    private ErrorRepresentation.Builder create(HttpStatus httpStatus, String code, Exception e){
        ErrorRepresentation.Builder builder = new ErrorRepresentation.Builder();
        builder.status(String.valueOf(httpStatus.value()))
                .code(code)
                .message(e.getMessage());
        return builder;
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(ErrorRepresentation.Builder builder, HttpStatus httpStatus){
        Map<String, Object> representation = builder.build();
        return new ResponseEntity<>(representation, httpStatus);
    }
    //----------- END PRIVATE --------------------------



    //----------- CONSTANTS ---------------------
    public static final String ILLEGAL_ARGUMENT_ERROR_CODE = "40011";
    public static final String WRONG_REQUEST_PARAMETER_ERROR_CODE = "40012";
    public static final String GENERAL_ERROR_CODE = "50011";
}
