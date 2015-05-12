package pl.edu.icm.saos.common.chart.formatter;

import java.util.Collections;
import java.util.List;

import pl.edu.icm.saos.common.order.OrderableComparator;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


/**
 * 
 * A class managing the {@link PointValueFormatter}s (set by {@link #setPointValueFormatters(List)}).
 * 
 * 
 * @author Łukasz Dumiszewski
 */
public class PointValueFormatterManager {

    
    private List<PointValueFormatter> pointValueFormatters = Lists.newArrayList();
    

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns a {@link PointValueFormatter} that handles the specific class. If more that one formatter handling the given class
     * can be found, then {@link PointValueFormatter} with the lowest {@link PointValueFormatter#getOrder()} is returned. 
     * 
     * @see #setPointValueFormatters(List)
     */
    public PointValueFormatter getFormatter(Class<?> valueClass) {
        
        Preconditions.checkNotNull(valueClass);
        
        for (PointValueFormatter pointValueFormatter : pointValueFormatters) {
            
            if (pointValueFormatter.handles(valueClass)) {
                
                return pointValueFormatter;
                
            }
            
        }
        
        throw new IllegalArgumentException("No pointValueFormatter handling " + valueClass.getName() + " can be found");
        
    }
    
    
    /**
     * Finds a proper formatter and formats the passed value by using it. Uses {@link #getFormatter(Class)} internally to get the relevant formatter.
     * 
     */
    public String format(Object value) {
        
        if (value == null) {
            
            return null;
        
        }
        
        return getFormatter(value.getClass()).format(value);
        
    }



    //------------------------ SETTERS --------------------------
    
    /**
     * Sets {@link PointValueFormatter}s that this manager will manage. <b> Sorts </b> the passed list of formatters
     * before assigning it to an internal variable. Sorting is based on {@link PointValueFormatter#getOrder()} - the point formatters
     * with the lowest order will have the highest precedence.
     * 
     * 
     */
    public void setPointValueFormatters(List<PointValueFormatter> pointValueFormatters) {
        
        Collections.sort(pointValueFormatters, new OrderableComparator<PointValueFormatter>());        
        this.pointValueFormatters = pointValueFormatters;
        
    }
    
}
