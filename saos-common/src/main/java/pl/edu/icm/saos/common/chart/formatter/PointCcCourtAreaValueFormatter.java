package pl.edu.icm.saos.common.chart.formatter;

import org.springframework.beans.factory.annotation.Value;

import pl.edu.icm.saos.common.chart.value.CcCourtArea;

import com.google.common.base.Preconditions;

/**
 * {@link PointValueFormatter} implementation handling {@link CcCourtArea} class
 * 
 * @author Łukasz Dumiszewski
 */

public class PointCcCourtAreaValueFormatter extends AbstractPointValueFormatter {

        
        
    //------------------------ LOGIC --------------------------
        
    @Override
    public String format(Object value) {
            
        if (value == null) {
            return null;
        } 
            
        CcCourtArea ccCourtArea = (CcCourtArea)value;
            
        return ccCourtArea.getName();
    }


        
    @Override
    public boolean handles(Class<?> valueClass) {
            
        Preconditions.checkNotNull(valueClass);
            
        return CcCourtArea.class.equals(valueClass);
    
    }

        

    
    //------------------------ PRIVATE --------------------------
        
        
    @Value("1")
    @Override
    public void setOrder(int order) {
        super.setOrder(order);
    }
    
}
