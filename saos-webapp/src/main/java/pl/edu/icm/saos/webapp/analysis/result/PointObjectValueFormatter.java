package pl.edu.icm.saos.webapp.analysis.result;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

/**
 * A general formatter handling all kinds of objects
 * 
 * @author Łukasz Dumiszewski
 */
@Service("pointObjectValueFormatter")
public class PointObjectValueFormatter extends AbstractPointValueFormatter {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String format(Object value) {
        
        if (value == null) {
            return null;
        } 
        
        return value.toString();
        
    }

    @Override
    public boolean handles(Class<?> valueClass) {
        
        Preconditions.checkNotNull(valueClass);
        
        return true;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Value("100")
    @Override
    public void setOrder(int order) {
        super.setOrder(order);
    }

}
