package pl.edu.icm.saos.common.util;

/**
 * @author Łukasz Dumiszewski
 */

public class VisitorA {
    
    public void visit(VisitableBar visitable) {
        visitable.barA();
    }
    
    
    public void visit(VisitableFoo visitable) {
        visitable.fooA();
    }
    
    
    

}
