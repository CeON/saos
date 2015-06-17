package pl.edu.icm.saos.common.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class HttpServletRequestUtilsTest {

    
    private HttpServletRequest request = mock(HttpServletRequest.class);
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected=NullPointerException.class)
    public void extractClientIp_NULL() {
        
        // execute
        HttpServletRequestUtils.extractClientIp(null);
    }

    
    @Test
    public void extractClientIp_X_FORWARDED_FOR() {
        
        // given
        when(request.getHeader("X-FORWARDED-FOR")).thenReturn("12.12.12.12");
        
        // execute & assert
        assertEquals("12.12.12.12", HttpServletRequestUtils.extractClientIp(request));
    }


    @Test
    public void extractClientIp_NO_X_FORWARDED_FOR() {
        
        // given
        when(request.getHeader("X-FORWARDED-FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("13.13.13.13");
        
        // execute & assert
        assertEquals("13.13.13.13", HttpServletRequestUtils.extractClientIp(request));
    }

    
    @Test(expected=NullPointerException.class)
    public void extractScheme_NULL() {
        
        // execute
        HttpServletRequestUtils.extractScheme(null);
    }
    
    
    @Test
    public void extractScheme_X_FORWARDED_PROTO() {
        
        // given
        when(request.getHeader("X-FORWARDED-PROTO")).thenReturn("https");
        
        // execute & assert
        assertEquals("https", HttpServletRequestUtils.extractScheme(request));
    }


    @Test
    public void extractScheme_NO_X_FORWARDED_PROTO() {
        
        // given
        when(request.getHeader("X-FORWARDED-PROTO")).thenReturn(null);
        when(request.getScheme()).thenReturn("https");
        
        // execute & assert
        assertEquals("https", HttpServletRequestUtils.extractScheme(request));
    }
    
    
    @Test(expected=NullPointerException.class)
    public void extractHost_NULL() {
        
        // execute
        HttpServletRequestUtils.extractHost(null);
    }
    
    
    @Test
    public void extractHost_X_FORWARDED_HOST() {
        
        // given
        when(request.getHeader("X-FORWARDED-HOST")).thenReturn("saos.org.pl");
        
        // execute & assert
        assertEquals("saos.org.pl", HttpServletRequestUtils.extractHost(request));
    }


    @Test
    public void extractHost_NO_X_FORWARDED_HOST() {
        
        // given
        when(request.getHeader("X-FORWARDED-HOST")).thenReturn(null);
        when(request.getServerName()).thenReturn("saos.org.pl");
        
        // execute & assert
        assertEquals("saos.org.pl", HttpServletRequestUtils.extractHost(request));
    }
    

    @Test(expected=NullPointerException.class)
    public void extractServetPort_NULL() {
        
        // execute
        HttpServletRequestUtils.extractServerPort(null);
    }
    
    
    @Test
    public void extractServerPort_X_FORWARDED_PORT() {
        
        // given
        when(request.getIntHeader("X-FORWARDED-PORT")).thenReturn(123);
        
        // execute & assert
        assertEquals(123, HttpServletRequestUtils.extractServerPort(request));
    }


    @Test
    public void extractServerPort_NO_X_FORWARDED_PORT() {
        
        // given
        when(request.getIntHeader("X-FORWARDED-PORT")).thenReturn(-1);
        when(request.getServerPort()).thenReturn(123);
        
        // execute & assert
        assertEquals(123, HttpServletRequestUtils.extractServerPort(request));
    }
    
    
    @Test
    public void constructRequestUrl_NON_STANDARD_PORT() {
        
        // given
        when(request.getHeader("X-FORWARDED-PROTO")).thenReturn("https");
        when(request.getHeader("X-FORWARDED-HOST")).thenReturn("saos.org.pl");
        when(request.getIntHeader("X-FORWARDED-PORT")).thenReturn(773);
        when(request.getRequestURI()).thenReturn("/search");
        
        // execute & assert
        assertEquals("https://saos.org.pl:773/search", HttpServletRequestUtils.constructRequestUrl(request));
    }

    @Test
    public void constructRequestUrl_STANDARD_PORT() {
        
        // given
        when(request.getHeader("X-FORWARDED-PROTO")).thenReturn("https");
        when(request.getHeader("X-FORWARDED-HOST")).thenReturn("saos.org.pl");
        when(request.getIntHeader("X-FORWARDED-PORT")).thenReturn(443);
        when(request.getRequestURI()).thenReturn("/search");
        
        // execute & assert
        assertEquals("https://saos.org.pl/search", HttpServletRequestUtils.constructRequestUrl(request));
    }


    @Test
    public void isDefaultPort() {
        
        // execute & assert
        assertTrue(HttpServletRequestUtils.isDefaultPort("https", 443));
        assertTrue(HttpServletRequestUtils.isDefaultPort("http", 80));
        assertFalse(HttpServletRequestUtils.isDefaultPort("https", 80));
        assertFalse(HttpServletRequestUtils.isDefaultPort("http", 443));
        assertFalse(HttpServletRequestUtils.isDefaultPort("http", 4443));
    }

    
    @Test(expected=NullPointerException.class)
    public void isDefaultPort_Scheme_NULL() {
     
        // execute & assert
        HttpServletRequestUtils.isDefaultPort(null, 443);
     }
}
