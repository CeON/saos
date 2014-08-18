package pl.edu.icm.saos.common.util;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class ReflectionToolsTest {

    @Test
    public void find_VisitorA_VisitableFoo() {
        Class<?> clazz = VisitorA.class;
        Object param = new VisitableFoo();

        Method m = ReflectionTools.findMethod(clazz, "visit", param);
        
        assertMethod(m, clazz, "visit", param.getClass());
    }

   

    @Test
    public void find_VisitorA_VisitableBar() {
        Class<?> clazz = VisitorA.class;
        Object param = new VisitableBar();
        
        Method m = ReflectionTools.findMethod(clazz, "visit", param);
        
        assertMethod(m, clazz, "visit", param.getClass());
        
    }

    
    @Test
    public void find_VisitorB_VisitableFoo() {
        Class<?> clazz = VisitorB.class;
        Object param = new VisitableFoo();
        
        Method m = ReflectionTools.findMethod(clazz, "visit", param);
        
        assertMethod(m, VisitorA.class, "visit", param.getClass());
        
    }
    
    @Test
    public void find_VisitorB_VisitableBar() {
        Class<?> clazz = VisitorB.class;
        Object param = new VisitableBar();
        
        Method m = ReflectionTools.findMethod(clazz, "visit", param);
        
        assertMethod(m, VisitorB.class, "visit", param.getClass());
    }
    
    
    @Test
    public void find_VisitorB_VisitableCar() {
        Class<?> clazz = VisitorB.class;
        Object param = new VisitableCar();
        
        Method m = ReflectionTools.findMethod(clazz, "visit", param);
        
        assertMethod(m, VisitorB.class, "visit", VisitableBar.class);
    }
    
    @Test
    public void find_VisitorA_VisitableCar() {
        Class<?> clazz = VisitorA.class;
        Object param = new VisitableCar();
        
        Method m = ReflectionTools.findMethod(clazz, "visit", param);
        
        assertMethod(m, VisitorA.class, "visit", VisitableBar.class);
    }
    
    //------------------------ PRIVATE --------------------------
    
    
    private void assertMethod(Method m, Class<?> expectedClass, String expectedName, Class<?> expectedParamClass) {
        assertEquals(expectedClass, m.getDeclaringClass());
        assertEquals(expectedName, m.getName());
        assertEquals(1, m.getParameterTypes().length);
        assertEquals(expectedParamClass, m.getParameterTypes()[0]);
    }
}
