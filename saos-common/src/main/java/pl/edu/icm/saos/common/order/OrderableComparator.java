package pl.edu.icm.saos.common.order;

import java.util.Comparator;


/**
 * An {@link Orderable} comparator
 * 
 * @author Łukasz Dumiszewski
 */

public class OrderableComparator<T extends Orderable> implements Comparator<T> {
    
    @Override
    public int compare(T o1, T o2) {
          
        return Integer.compare(o1.getOrder(), o2.getOrder());
       
    }
}
