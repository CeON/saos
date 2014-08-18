package pl.edu.icm.saos.common.visitor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import pl.edu.icm.saos.common.util.VisitableFoo;
import pl.edu.icm.saos.common.util.VisitorA;


/**
 * @author Łukasz Dumiszewski
 */

public class VisitorUtilsTest {

    private VisitorA visitorA = new VisitorA(); 
    
    private VisitableFoo visitableFoo = mock(VisitableFoo.class);
    
    
    @Test
    public void executeVisitorMethod() {
        VisitorUtils.executeVisitorMethod(visitorA, visitableFoo);
        verify(visitableFoo).fooA();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void executeVisitorMethod_MethodNotFound() {
        VisitorUtils.executeVisitorMethod(visitorA, "aaa");
        
    }
    
    
}
