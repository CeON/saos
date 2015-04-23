package pl.edu.icm.saos.common.cache;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */
public class MethodSignatureKeyGeneratorTest {

    private MethodSignatureKeyGenerator keyGenerator = new MethodSignatureKeyGenerator();
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void generateKey_NO_ARG_METHOD() throws NoSuchMethodException, SecurityException {
        
        // given
        String target = "XYZ";
        Method method = String.class.getMethod("toString");
        
        // execute
        String key = keyGenerator.generate(target, method);
        
        // assert
        assertEquals("String:toString:", key);
    }
    
    
    @Test
    public void generateKey_ARG_METHOD() throws NoSuchMethodException, SecurityException {
        
        // given
        String target = "XYZ";
        Method method = String.class.getMethod("toString");
        Object[] params = new Object[]{30, new Param(), null, new String[]{"aa","bb"}};
        
        
        // execute
        String key = keyGenerator.generate(target, method, params);
        
        // assert
        assertEquals("String:toString: arg1:30 arg2:100$==300pln arg3:null arg4:{aa,bb}", key);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private static class Param {
        
        public String toString() {
            return "100$==300pln";
        }
        
    }
    
}
