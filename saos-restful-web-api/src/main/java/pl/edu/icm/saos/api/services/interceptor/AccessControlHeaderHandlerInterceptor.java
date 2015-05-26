package pl.edu.icm.saos.api.services.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor adding {@literal Access-Control-Allow-Origin} header.
 * 
 * @author madryk
 */
public class AccessControlHeaderHandlerInterceptor extends HandlerInterceptorAdapter {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        return true;
    }
}
