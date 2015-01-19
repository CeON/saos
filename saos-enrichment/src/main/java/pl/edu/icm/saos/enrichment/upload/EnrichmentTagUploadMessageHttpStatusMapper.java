package pl.edu.icm.saos.enrichment.upload;

import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_AUTHENTICATION_FAILED;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_EMPTY_DATA;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_INTERVAL_SERVER_ERROR;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_INVALID_DATA;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_INVALID_JSON_FORMAT;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_IO;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_MAX_UPLOAD_SIZE_EXCEEDED;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_SAME_TAG_ALREADY_UPLOADED;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_UNSUPPORTED_HTTP_CONTENT_TYPE;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_UNSUPPORTED_HTTP_METHOD;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.OK_MESSAGE;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


/**
 * Maps enrichment tag service response message ({@link EnrichmentTagUploadResponseMessages}) to {@link HttpStatus}
 * @author Łukasz Dumiszewski
 *
 */
@Service("enrichmentTagUploadMessageHttpStatusMapper")
public class EnrichmentTagUploadMessageHttpStatusMapper {

	
	 Map<String, HttpStatus> messageStatusMap = new HashMap<>();
	 
	 {
		messageStatusMap.put(OK_MESSAGE, HttpStatus.OK);
		messageStatusMap.put(ERROR_AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED); 
	    messageStatusMap.put(ERROR_UNSUPPORTED_HTTP_METHOD, HttpStatus.METHOD_NOT_ALLOWED); 
		messageStatusMap.put(ERROR_UNSUPPORTED_HTTP_CONTENT_TYPE, HttpStatus.UNSUPPORTED_MEDIA_TYPE); 
		messageStatusMap.put(ERROR_EMPTY_DATA, HttpStatus.BAD_REQUEST);
		messageStatusMap.put(ERROR_INVALID_JSON_FORMAT, HttpStatus.BAD_REQUEST); 
		messageStatusMap.put(ERROR_INVALID_DATA, HttpStatus.BAD_REQUEST);
		messageStatusMap.put(ERROR_INTERVAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		messageStatusMap.put(ERROR_MAX_UPLOAD_SIZE_EXCEEDED, HttpStatus.REQUEST_ENTITY_TOO_LARGE);
		messageStatusMap.put(ERROR_SAME_TAG_ALREADY_UPLOADED, HttpStatus.BAD_REQUEST);
		messageStatusMap.put(ERROR_IO, HttpStatus.INTERNAL_SERVER_ERROR);
	   
	 }
	 
	 
	 //------------------------ LOGIC --------------------------
	 
	 /**
	  * Returns {@link HttpStatus} corresponding to the given message 
	  * @throws IllegalArgumentException if no http status for the passed message could be found
	  */
	 public HttpStatus getHttpStatus(String message) {
		 HttpStatus httpStatus = messageStatusMap.get(message);
		 
		 if (httpStatus == null) {
			 throw new IllegalArgumentException("no http status for the message: " + message);
		 }
		 
		 return httpStatus;
	 }
	 
}
