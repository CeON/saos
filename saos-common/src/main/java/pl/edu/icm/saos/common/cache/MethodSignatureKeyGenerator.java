package pl.edu.icm.saos.common.cache;

import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.cache.interceptor.KeyGenerator;

/**
 * @author Łukasz Dumiszewski
 */

public class MethodSignatureKeyGenerator implements KeyGenerator {

    @Override
    public String generate(Object target, Method method, Object... params) {
        
       String key = target.getClass().getSimpleName() + ":" + method.getName() + ":";
        
       key += generateParamsKeyPart(params);
       
       return key;
        
    }
    
    
    
    
    //------------------------ PRIVATE --------------------------
    
    private static String generateParamsKeyPart(Object[] args) {
        
        if (args == null || args.length == 0) {
            return "";
        }
        
        StringBuilder argsValue = new StringBuilder();
        
        
        for (int i = 0; i <args.length; i++) {
            argsValue.append(" arg"+(i+1) +":");
            
            Object arg = args[i];
            
            if (arg == null) {
                argsValue.append("null");
                continue;
            } 
            
            if (arg.getClass().isArray() ) {            
                argsValue.append(ArrayUtils.toString(arg));
                continue;
            }
            
            argsValue.append(arg.toString());
            
        }
                
        return argsValue.toString();
    }

}
