package pl.edu.icm.saos.enrichment.upload;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_INTERVAL_SERVER_ERROR;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_UNSUPPORTED_HTTP_CONTENT_TYPE;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_UNSUPPORTED_HTTP_METHOD;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.OK_MESSAGE;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.icm.saos.common.service.ServiceException;
import pl.edu.icm.saos.common.service.ServiceResponse;
import pl.edu.icm.saos.common.service.ServiceResponseFactory;
import pl.edu.icm.saos.enrichment.process.UploadEnrichmentTagProcessor;
import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;

import com.google.common.net.HttpHeaders;

/**
 * @author Łukasz Dumiszewski
 */
@RestController
public class EnrichmentTagUploadController {


    private static Logger log = LoggerFactory.getLogger(EnrichmentTagUploadController.class);
    
    private final static String SUPPORTED_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    
    
    @Autowired
    private EnrichmentTagUploadService enrichmentTagUploadService;
    
    @Autowired
    private UploadEnrichmentTagProcessor uploadEnrichmentTagProcessor;
    
    @Autowired
    private EnrichmentTagUploadMessageHttpStatusMapper enrichmentTagUploadMessageHttpStatusMapper;
    
    
    
    //------------------------ LOGIC --------------------------
    
   
    /**
     * Invoked by the SAOS Enricher to upload new enrichment tags.<br/>
     * <br/>
     * <ol>Involves two steps:
     * <li>{@link EnrichmentTagUploadService#uploadEnrichmentTags(java.io.InputStream)} - uploads tags to {@link UploadEnrichmentTag}</li>
     * <li>{@link UploadEnrichmentTagProcessor#processUploadedEnrichmentTags()} - processes uploaded tags (overwrites production tags), invoked
     * asynchronously after successful upload </li>
     * <ol>
     *  
     */
	@RequestMapping(value="/api/enrichment/tags")
    public ResponseEntity<ServiceResponse> uploadEnrichmentTags(
                                         @RequestHeader(value=HttpHeaders.CONTENT_TYPE, required=false) String contentType,
                                         HttpServletRequest request) throws IOException {
        
        checkRequestMethod(request);
        checkContentType(contentType);
        
        enrichmentTagUploadService.uploadEnrichmentTags(request.getInputStream());
        
        asyncProcessUploadedEnrichmentTags(); // async so the SAOS enricher wouldn't have to wait for the end the processing part
        
        return new ResponseEntity<ServiceResponse>(ServiceResponseFactory.createOkResponse(OK_MESSAGE, ""), HttpStatus.OK);
        
    }



	
    
    
    //------------------------ PRIVATE --------------------------

	
	private void asyncProcessUploadedEnrichmentTags() {
		
		new Thread(new Runnable() {
			        		public void run() {
			        			uploadEnrichmentTagProcessor.processUploadedEnrichmentTags();
			        		};
    			   }).start();
		
	}
    
    

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
    private ResponseEntity<ServiceResponse> serviceException(ServiceException e) {
        return errorResponse(e.getMainMessage(), e, enrichmentTagUploadMessageHttpStatusMapper.getHttpStatus(e.getMainMessage()));
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ServiceResponse> exception(Exception e) {
        return errorResponse(ERROR_INTERVAL_SERVER_ERROR, e, INTERNAL_SERVER_ERROR);
    }
    
    private ResponseEntity<ServiceResponse> errorResponse(String mainMessage, Exception e, HttpStatus httpStatus) {
        log.warn("", e);
        return new ResponseEntity<ServiceResponse>(ServiceResponseFactory.createErrorResponse(mainMessage, e.getMessage()), httpStatus);
    }

    
    
}
