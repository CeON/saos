package pl.edu.icm.saos.common.http;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Preconditions;

/**
 * Utility class with methods facilitating extracting various info from an {@link HttpServletRequest}  
 * 
 * @author Łukasz Dumiszewski
 */

public final class HttpServletRequestUtils {


    private HttpServletRequestUtils() {
        throw new IllegalStateException("may not be instantiated");
    }
    
    
    /**
     * Returns client ip. Looks for X-FORWARDED-FOR header and if it is set, returns its value.
     * If there is no X-FORWARDED-FOR header set then returns {@link HttpServletRequest#getRemoteAddr()}
     */
    public static String extractClientIp(HttpServletRequest request) {
        
        Preconditions.checkNotNull(request);
        
        String ipAddress = request.getHeader("X-FORWARDED-FOR");  
        if (ipAddress == null) {  
           ipAddress = request.getRemoteAddr();  
        }
        return ipAddress;
    }
    
    
    /**
     * Returns scheme (like http, https etc) provided by a client. Looks for X-FORWARDED-PROTO header and if it is set, returns its value.
     * If there is no X-FORWARDED-PROTO header set then returns {@link HttpServletRequest#getScheme()}
     */
    public static String extractScheme(HttpServletRequest request) {
        
        Preconditions.checkNotNull(request);
        
        String scheme = request.getHeader("X-FORWARDED-PROTO");  
        if (scheme == null) {  
           scheme = request.getScheme();
        }
        return scheme;
    }
    
    
    /**
     * Returns host (like saos.org.pl). Looks for X-FORWARDED-HOST header and if it is set, returns its value.
     * If there is no X-FORWARDED-HOST header set then returns {@link HttpServletRequest#getServerName()}
     */
    public static String extractHost(HttpServletRequest request) {
        
        Preconditions.checkNotNull(request);
        
        String host = request.getHeader("X-FORWARDED-HOST");  
        if (host == null) {  
           host = request.getServerName();
        }
        return host;
    }
    
    
    /**
     * Returns server port provided by a client. Looks for X-FORWARDED-PORT header and if it is set, returns its value.
     * If there is no X-FORWARDED-PORT header set then returns {@link HttpServletRequest#getServerPort()()}
     */
    public static int extractServerPort(HttpServletRequest request) {
        
        Preconditions.checkNotNull(request);
        
        int port = request.getIntHeader("X-FORWARDED-PORT");  
        if (port == -1) {  
           port = request.getServerPort();
        }
        return port;
    }
    
    
    /**
     * Returns url sent by the client (without parameters).
     * The url consists of: <br/>
     * <ul>
     * <li> scheme - {@link #extractClientScheme(HttpServletRequest)} </li>
     * <li> host - {@link #extractClientHost(HttpServletRequest)} </li>
     * <li> port - {@link #extractServerPort(HttpServletRequest)} </li>
     * <li> urlPath - {@link HttpServletRequest#getRequestURI()} </li>
     * </ul>
     */
    public static String constructRequestUrl(HttpServletRequest request) {
        
        StringBuilder url = new StringBuilder();
        
        String scheme = extractScheme(request);
        
        int port = extractServerPort(request);
        
        url.append (scheme);   
        url.append ("://");
        url.append (extractHost(request));
        
        if (!isDefaultPort(scheme, port)) {
            url.append (':');
            url.append (port);
        }
        
        String urlPath = request.getRequestURI();
        url.append(urlPath);

        return url.toString();
        
    }
    
    /**
     * Is the given port a default port for the given scheme 
     */
    public static boolean isDefaultPort(String scheme, int port) {
        
        Preconditions.checkNotNull(scheme);
        
        return ((scheme.equals("http") && port == 80) || (scheme.equals("https") && port == 443));
    }
    
}