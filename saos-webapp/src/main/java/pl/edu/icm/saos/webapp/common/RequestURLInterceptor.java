package pl.edu.icm.saos.webapp.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import pl.edu.icm.saos.common.http.HttpServletRequestUtils;

/**
 * Interceptor adding the full request url (with parameters) to the intercepted request.
 * The url is kept as a 'requestUrlWithParameters' request variable.
 * 
 * @author Łukasz Pawełczak
 */
public class RequestURLInterceptor extends HandlerInterceptorAdapter {

    /**
     * Adds a client request url string (with parameters) to the request
     * as a variable named 'requestUrlWithParameters'.
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    
	request.setAttribute("requestUrlWithParameters", HttpServletRequestUtils.constructRequestUrlWithParameters(request));
    	return true;
    }
        
}

