package pl.edu.icm.saos.webapp.security;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import pl.edu.icm.saos.common.service.ServiceResponse;
import pl.edu.icm.saos.common.service.ServiceResponseFactory;

import com.google.common.base.Preconditions;

/**
 * {@link AuthenticationEntryPoint} implementation that writes {@link ServiceResponse} to the response body by using
 * a specified {@link HttpMessageConverter}
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class ServiceBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    
    private String basicRealm;
    private String mainMessage;
    private HttpMessageConverter<ServiceResponse> messageConverter;
   
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + basicRealm + "\"");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        MediaType mediaType = messageConverter.getSupportedMediaTypes().get(0);
        response.setContentType(mediaType.toString());
        ServiceResponse serviceResponse = ServiceResponseFactory.createErrorResponse(mainMessage, authException.getMessage());
        messageConverter.write(serviceResponse, mediaType, new ServletServerHttpResponse(response));
    }

    
    @PostConstruct
    public void postConstruct() {
        Preconditions.checkArgument(StringUtils.isNotBlank(basicRealm));
        Preconditions.checkArgument(StringUtils.isNotBlank(mainMessage));
        Preconditions.checkNotNull(messageConverter);
    }
    
    
    //------------------------ SETTERS --------------------------

    /**
     * 
     * @param messageConverter converter that will be used put a {@link ServiceResponse} into a http response stream. 
     * It's default mediaType (first element of the {@link HttpMessageConverter#getSupportedMediaTypes()} list) will be used to
     * set the contentType of the response.
     */
    public void setMessageConverter(HttpMessageConverter<ServiceResponse> messageConverter) {
        this.messageConverter = messageConverter;
    }

    /**
     * @param basic realm Basic realm name that will be sent in "WWW-Authenticate" header
     */
    public void setBasicRealm(String basicRealm) {
        this.basicRealm = basicRealm;
    }

    /**
     * 
     * @param mainMessage See {@link ServiceResponseFactory#createErrorResponse(String, String)}
     */
    public void setMainMessage(String mainMessage) {
        this.mainMessage = mainMessage;
    }


    
}
