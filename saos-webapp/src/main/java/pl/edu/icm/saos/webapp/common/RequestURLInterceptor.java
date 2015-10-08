package pl.edu.icm.saos.webapp.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import pl.edu.icm.saos.common.http.HttpServletRequestUtils;

/**
 * Interceptor adds to request full url with parameters
 * 
 * @author Łukasz Pawełczak
 */
public class RequestURLInterceptor extends HandlerInterceptorAdapter {

    /**
     * Adds clients request url (with parameters) to request.
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    
	request.setAttribute("requestUrlWithParameters", HttpServletRequestUtils.constructRequestUrlWithParameters(request));
    	return true;
    }
        
}

