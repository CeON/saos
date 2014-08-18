package pl.edu.icm.saos.common.util;

/**
 * @author Łukasz Dumiszewski
 */

public class VisitorB extends VisitorA {
 
    
    public void visit(VisitableBar visitable) {
        visitable.barB();
    }
    
}
