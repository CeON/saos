package pl.edu.icm.saos.api.services.exceptions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * Utility class to verify {@link HttpServletRequest} elements (for example headers or requested method).
 * 
 * @author madryk
 */
public class HttpServletRequestVerifyUtils {

    /**
     * Checks if request has correct method.
     * 
     * @throws MethodNotSupportedException if requested method is not allowed 
     */
    public static void checkRequestMethod(HttpServletRequest request, HttpMethod allowedMethod) {
        if (!StringUtils.equals(request.getMethod(), allowedMethod.name())) {
            throw new MethodNotSupportedException(request.getMethod(), allowedMethod.name());
        }
    }
    
    /**
     * Checks if Accept header of request defined allowed media type.
     * 
     * @throws MediaTypeNotSupportedException if Accept header doesn't contain allowed media type 
     */
    public static void checkAcceptHeader(String acceptHeader, MediaType allowedMediaType) {
        List<MediaType> acceptMediaTypes = MediaType.parseMediaTypes(acceptHeader);
        
        for (MediaType acceptMediaType : acceptMediaTypes) {
            if (acceptMediaType.isCompatibleWith(allowedMediaType)) {
                return;
            }
        }
        throw new MediaTypeNotSupportedException(acceptHeader, allowedMediaType);
    }
}
