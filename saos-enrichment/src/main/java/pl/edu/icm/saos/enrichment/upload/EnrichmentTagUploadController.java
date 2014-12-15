package pl.edu.icm.saos.enrichment.upload;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_INTERVAL_SERVER_ERROR;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_INVALID_JSON_FORMAT;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_SAME_TAG_ALREADY_UPLOADED;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_UNSUPPORTED_HTTP_CONTENT_TYPE;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_UNSUPPORTED_HTTP_METHOD;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.OK_MESSAGE;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.icm.saos.common.json.JsonItemParseException;
import pl.edu.icm.saos.common.service.ServiceException;
import pl.edu.icm.saos.common.service.ServiceResponse;
import pl.edu.icm.saos.common.service.ServiceResponseFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.net.HttpHeaders;

/**
 * @author Łukasz Dumiszewski
 */
@RestController
public class EnrichmentTagUploadController {


    private static Logger log = LoggerFactory.getLogger(EnrichmentTagUploadController.class);
    
    private final static String SUPPORTED_CONTENT_TYPE = "application/json";
    
    
    @Autowired
    private EnrichmentTagUploadService enrichmentTagUploadService;
    
    
    
    //------------------------ LOGIC --------------------------
    
   
    @RequestMapping(value="/api/enrichment/tags")
    public ResponseEntity<ServiceResponse> uploadEnrichmentTags(
                                         @RequestHeader(value=HttpHeaders.CONTENT_TYPE, required=false) String contentType,
                                         HttpServletRequest request) throws IOException {
        
        checkRequestMethod(request);
        checkContentType(contentType);
        
        enrichmentTagUploadService.uploadEnrichmentTags(request.getInputStream());
        
        return new ResponseEntity<ServiceResponse>(ServiceResponseFactory.createOkResponse(OK_MESSAGE, ""), HttpStatus.OK);
        
    }
    
    
    //------------------------ SETTERS --------------------------

    

    // why so? when supported method is specified in requestMapping then dispatcherserlvlet throws possible exception 'outside' the controller.
    // we can handle it only in general exception resolver (or controllerAdvice (specifying base packages does not help)) - but we want to 
    // return DepositResponse only for this Controller.
    private void checkRequestMethod(HttpServletRequest request) {
        if (!request.getMethod().equals(RequestMethod.PUT.name())) {
            throw new ServiceException(ERROR_UNSUPPORTED_HTTP_METHOD, request.getMethod() + ", only PUT allowed");
        }
    }

    // same as checkRequestMethod
    private void checkContentType(String contentType) {
        if (!SUPPORTED_CONTENT_TYPE.equals(contentType)) {
            throw new ServiceException(ERROR_UNSUPPORTED_HTTP_CONTENT_TYPE, contentType + ", only " + SUPPORTED_CONTENT_TYPE + " allowed");
        }
    }

    
    //------------------ exception handlers ----------------------
    
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ServiceResponse> serviceException(ServiceException e) {
        return errorResponse(e.getMainMessage(), e, BAD_REQUEST);
    }

    @ExceptionHandler(JsonItemParseException.class)
    public ResponseEntity<ServiceResponse> parseItemException(JsonItemParseException e) {
        return errorResponse(ERROR_INVALID_JSON_FORMAT, e, BAD_REQUEST);
        
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ServiceResponse> parseException(JsonParseException e) {
        return errorResponse(ERROR_INVALID_JSON_FORMAT, e, BAD_REQUEST);
        
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ServiceResponse> validationException(ValidationException e) {
        return errorResponse(ERROR_INVALID_JSON_FORMAT, e, BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ServiceResponse> dataIntegrityException(DataIntegrityViolationException e) {
        return errorResponse(ERROR_SAME_TAG_ALREADY_UPLOADED, e, BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceResponse> exception(Exception e) {
        return errorResponse(ERROR_INTERVAL_SERVER_ERROR, e, INTERNAL_SERVER_ERROR);
    }
    
  
    //------------------------ PRIVATE --------------------------
    
    private ResponseEntity<ServiceResponse> errorResponse(String mainMessage, Exception e, HttpStatus httpStatus) {
        log.warn("", e);
        return new ResponseEntity<ServiceResponse>(ServiceResponseFactory.createErrorResponse(mainMessage, e.getMessage()), httpStatus);
    }

    
}
