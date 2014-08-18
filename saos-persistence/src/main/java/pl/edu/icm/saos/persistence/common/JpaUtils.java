package pl.edu.icm.saos.persistence.common;

import java.util.Collection;

/**
 * @author Łukasz Dumiszewski
 */

public final class JpaUtils {

    private JpaUtils() {
        throw new IllegalStateException("may not be instantiated");
    }
    
    
    public static void initialize(Object proxy) {
        if ( proxy == null ) {
            return;
        }
        
        if (proxy instanceof Collection) {
            ((Collection<?>) proxy).size();
            return;
        }
        
        if (proxy instanceof DataObject) {
            ((DataObject)proxy).getVer();
        }
        
        
        
        
    }
    
}
