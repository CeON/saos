package pl.edu.icm.saos.webapp.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;

import pl.edu.icm.saos.common.service.ServiceResponse;
import pl.edu.icm.saos.common.service.ServiceResponseFactory;

import com.google.common.collect.Lists;


public class ServiceBasicAuthenticationEntryPointTest {
    
    
    private ServiceBasicAuthenticationEntryPoint serviceBasicAuthenticationEntryPoint = new ServiceBasicAuthenticationEntryPoint();
    
    @Mock private HttpMessageConverter<ServiceResponse> messageConverter;
    
    private String basicRealm = "REALM NAME";
    private String mainMessage = "MAIN MESSAGE";
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
        serviceBasicAuthenticationEntryPoint.setMessageConverter(messageConverter);
        
        serviceBasicAuthenticationEntryPoint.setBasicRealm(basicRealm);
        
        serviceBasicAuthenticationEntryPoint.setMainMessage(mainMessage);
        
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test
    public void commence() throws Exception {
        
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String authExceptionMessage = "EXCEPTION MESSAGE";
        AuthenticationException authException = mock(AuthenticationException.class);
        when(messageConverter.getSupportedMediaTypes()).thenReturn(Lists.newArrayList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));
        when(authException.getMessage()).thenReturn(authExceptionMessage);
        
        
        // execute
        
        serviceBasicAuthenticationEntryPoint.commence(request, response, authException);
        
        
        // assert
        
        verify(response).addHeader("WWW-Authenticate", "Basic realm=\"" + basicRealm +"\"");
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON.toString());
        
        ArgumentCaptor<ServiceResponse> serviceResponseArg = ArgumentCaptor.forClass(ServiceResponse.class);
        ArgumentCaptor<ServletServerHttpResponse> servletServerHttpResponseArg = ArgumentCaptor.forClass(ServletServerHttpResponse.class);
        
        verify(messageConverter).write(serviceResponseArg.capture(), Mockito.eq(MediaType.APPLICATION_JSON), servletServerHttpResponseArg.capture());
        
        assertEquals(ServiceResponseFactory.createErrorResponse(mainMessage, authExceptionMessage), serviceResponseArg.getValue());
        
        assertTrue(servletServerHttpResponseArg.getValue().getServletResponse() == response);
        
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void postConstruct_EmptyBasicRealm() {
        
        // given
        
        serviceBasicAuthenticationEntryPoint.setBasicRealm("");
        
        // execute
        
        serviceBasicAuthenticationEntryPoint.postConstruct();
        
    }

    
    @Test(expected=IllegalArgumentException.class)
    public void postConstruct_EmptyMainMessage() {
        
        // given
        
        serviceBasicAuthenticationEntryPoint.setMainMessage(" ");
        
        // execute
        
        serviceBasicAuthenticationEntryPoint.postConstruct();
        
    }
    
    
    @Test(expected=NullPointerException.class)
    public void postConstruct_NullMessageConverter() {
        
        // given
        
        serviceBasicAuthenticationEntryPoint.setMessageConverter(null);
        
        // execute
        
        serviceBasicAuthenticationEntryPoint.postConstruct();
        
    }


}