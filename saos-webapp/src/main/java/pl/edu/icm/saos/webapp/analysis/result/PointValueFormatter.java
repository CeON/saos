package pl.edu.icm.saos.webapp.analysis.result;

/**
 * 
 * Formatter of x/y values of {@link Point}.
 * 
 * @author Łukasz Dumiszewski
 */

public interface PointValueFormatter {

    
    /**
     * Formats the given value object into String
     */
    public String format(Object value);

    /**
     * Returns true if the formatter handles (can format) objects of the given class 
     */
    public boolean handles(Class<?> valueClass);
    
    /**
     * Returns the order of this formatter. The order can be used when one has the list of formatters
     * and wants to give them specific precedence.
     */
    public int getOrder();
    
}
