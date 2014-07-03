package pl.edu.icm.saos.common.testUtils;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

/**
 * @author Łukasz Dumiszewski
 */

public final class ReflectionFieldSetter {
    
    private ReflectionFieldSetter() {
        throw new IllegalArgumentException("may not be instantiated");
    }

    public static void setField(Object object, String fieldName, Object newValue) {
        try {
            Field idField = ReflectionUtils.findField(object.getClass(), fieldName);
            ReflectionUtils.makeAccessible(idField);
            ReflectionUtils.setField(idField, object, newValue);
            
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
