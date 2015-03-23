package pl.edu.icm.saos.common.chart.formatter;


import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.base.Preconditions;

/**
 * 
 * {@link PointValueFormatter} implementation handling {@link LocalDate} class
 * 
 * @author Łukasz Dumiszewski
 */
public class PointLocalDateValueFormatter extends AbstractPointValueFormatter {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String format(Object value) {
        
        if (value == null) {
            return null;
        } 
        
        LocalDate date = (LocalDate)value;
        
        return date.toString("dd/MM/yy");
     }

    
    @Override
    public boolean handles(Class<?> valueClass) {
        
        Preconditions.checkNotNull(valueClass);
        
        return LocalDate.class.equals(valueClass);
    }

    
    //------------------------ SETTERS --------------------------
    
    @Value("1")
    @Override
    public void setOrder(int order) {
        super.setOrder(order);
    }

}
