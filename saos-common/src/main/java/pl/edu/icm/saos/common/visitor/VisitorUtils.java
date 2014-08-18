package pl.edu.icm.saos.common.visitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import pl.edu.icm.saos.common.util.ReflectionTools;

/**
 * @author Łukasz Dumiszewski
 */

public class VisitorUtils {
    
    
    
    /**
     * Finds and executes the method with name "visit" in the visitor (or one of its superclasses) that has only one parameter
     * of type equal to visitedObject.class (or one of its interfaces or superclasses). <br/>
     * For example, if the specified visitor has 2 methods: <br/>
     * 1) visit(Object) <br/>
     * 2) visit(String) <br/>
     * Then, in case of passing visitedObject that is of String type, the second method will be invoked. Otherwise,
     * the first method will be executed.<br/>
     *  
     * Uses {@link ReflectionTools#findMethod(Class, String, Object)} internally. <br/>
     
     * 
     * @throws IllegalArgumentExceptions If the proper method cannot be found
     * 
     */
    public static void executeVisitorMethod(Object visitor, Object visitedObject) {
        
        Class<?> visitableClass = visitedObject.getClass();
        
        Method visitMethod = ReflectionTools.findMethod(visitor.getClass(), "visit", visitedObject);
        
        if (visitMethod != null) {
            try {
               visitMethod.invoke(visitor, new Object[]{visitedObject});
               return;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            
        }
     
        throw new IllegalArgumentException("visit("+visitableClass.getSimpleName()+") method not found in visitor class "+visitor.getClass().getName());
    }
}
