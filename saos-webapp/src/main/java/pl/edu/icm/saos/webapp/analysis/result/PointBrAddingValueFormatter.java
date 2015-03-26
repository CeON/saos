package pl.edu.icm.saos.webapp.analysis.result;

import org.springframework.beans.factory.annotation.Value;

import pl.edu.icm.saos.common.chart.formatter.AbstractPointValueFormatter;
import pl.edu.icm.saos.common.chart.formatter.PointValueFormatter;

import com.google.common.base.Preconditions;

/**
 * 
 * A {@link PointValueFormatter} implementation that delegates actual formatting to the formatter passed to the constructor,
 * and that adds to the formatted value html breaking tags.
 * 
 * @author Łukasz Dumiszewski
 */
public class PointBrAddingValueFormatter extends AbstractPointValueFormatter {

    

    private PointValueFormatter delegatedFormatter;
    
    
    //------------------------ CONSTRUCTORS --------------------------

    /**
     * @param delegatedFormatter the formatter that the actual formatting will be delegated to
     * @NullPointerException if the passed formatter is null
     */
    public PointBrAddingValueFormatter(PointValueFormatter delegatedFormatter) {
        super();
        Preconditions.checkNotNull(delegatedFormatter);
        this.delegatedFormatter = delegatedFormatter;
    }

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Delegates the actual formatting to the formatter passed to the constructor.
     * Replaces first occurrence of "-" in formatted string with "&lt;br/&gt;-&lt;br/&gt;" 
     */
    @Override
    public String format(Object value) {
        
        String formattedValue = delegatedFormatter.format(value);
        
        return formattedValue.replace("-", "<br/>-<br/>");
     }

    
    /**
     * Invokes the handles method of the formatter passed to the constructor.
     */
    @Override
    public boolean handles(Class<?> valueClass) {
        
        return delegatedFormatter.handles(valueClass);
    }

    
    //------------------------ SETTERS --------------------------
    
    @Value("1")
    @Override
    public void setOrder(int order) {
        super.setOrder(order);
    }

}
