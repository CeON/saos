package pl.edu.icm.saos.webapp.analysis.result;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

/**
 * 
 * Formatter handling {@link LocalDate} class
 * 
 * @author Łukasz Dumiszewski
 */
@Service("pointLocalDateValueFormatter")
public class PointLocalDateValueFormatter extends AbstractPointValueFormatter {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String format(Object value) {
        
        if (value == null) {
            return null;
        } 
        
        LocalDate date = (LocalDate)value;
        
        return ""+date.toDate().getTime();
        
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
