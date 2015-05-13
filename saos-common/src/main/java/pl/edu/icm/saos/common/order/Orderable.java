package pl.edu.icm.saos.common.order;

/**
 * Contract for objects that have a certain order among other objects
 * 
 * @author Łukasz Dumiszewski
 */

public interface Orderable {

    /**
     * Returns order of an object among other objects 
     */
    public int getOrder();
    
}
