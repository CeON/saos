package pl.edu.icm.saos.common.util;

import java.lang.reflect.Method;

import org.springframework.util.ReflectionUtils;

/**
 * @author Łukasz Dumiszewski
 */

public final class ReflectionTools {


    private ReflectionTools()  {
        throw new IllegalStateException("may not be instantiated");
    }
    
    
    /**
     * Finds the method with the methodName in clazz (and its superclasses)
     * If the method cannot be found for the methodParam then looks for method whose param is any
     * of the methodParam interfaces or superclasses. 
     */
    public static Method findMethod(Class<?> clazz, String methodName, Object methodParam) {
        
        Method method = null;
        
        Class<?> methodParamClass = methodParam.getClass();
        
        while (methodParamClass != null) {
            
            // try class
            method = ReflectionUtils.findMethod(clazz, "visit", methodParamClass);
            if (method != null) {
                return method;
            }
                
            //try interfaces
            for (Class<?> interf : methodParamClass.getInterfaces()) {
                method = ReflectionUtils.findMethod(clazz, "visit", interf);
                if (method != null) {
                    return method;
                }
            }
                
            //step up in class hierarchy
            methodParamClass = methodParamClass.getSuperclass();
        }
        
        return null;
    }
}
