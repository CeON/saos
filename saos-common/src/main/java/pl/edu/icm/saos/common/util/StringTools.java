package pl.edu.icm.saos.common.util;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Łukasz Dumiszewski
 */

public final class StringTools {

    
    //------------------------ CONSTRUCTORS --------------------------
    
    private StringTools() {
        throw new IllegalStateException("may not be instantiated");
    }
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Squashes adjacent white-spaces into one space then trims them. <br/> <br/>
     * <pre>
     * StringTools.squashAndTrim(null)  = null
     * StringTools.squashAndTrim("    ")  = ""
     * StringTools.squashAndTrim("Alice has \t   a cat   ")  = "Alice has a cat"
     * </pre>
     */
    public static String squashAndTrim(String value) {
        
        if (value == null) {
            return null;
        }
        
        if (StringUtils.isBlank(value)) {
            return "";
        }
        
        return value.replaceAll("\\s+", " ").trim();
    }
    
    
    /**
     * Returns value.toLowerCase(Locale.ROOT) (see: {@link String#toLowerCase(Locale)}) <br/>
     * Returns null if the passed value is null.
     */
    public static String toRootLowerCase(String value) {
        
        if (value == null) {
            return null;
        }
        
        return value.toLowerCase(Locale.ROOT);
    }
    
    
    
}
