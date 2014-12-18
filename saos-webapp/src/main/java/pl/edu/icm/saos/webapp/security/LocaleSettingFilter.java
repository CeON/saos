package pl.edu.icm.saos.webapp.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author Łukasz Dumiszewski
 */

public class LocaleSettingFilter extends GenericFilterBean {

    private LocaleResolver localeResolver;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        LocaleContextHolder.setLocale(localeResolver.resolveLocale((HttpServletRequest)request));
        
        chain.doFilter(request, response);
        
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }
    
    
    

}
