package pl.edu.icm.saos.common.chart.formatter;

import org.springframework.beans.factory.annotation.Value;

import pl.edu.icm.saos.common.chart.value.Week;

import com.google.common.base.Preconditions;

/**
 * 
 * {@link PointValueFormatter} implementation handling {@link Week} class
 * 
 * @author Łukasz Dumiszewski
 */
public class PointWeekValueFormatter extends AbstractPointValueFormatter {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String format(Object value) {
        
        if (value == null) {
            return null;
        } 
        
        Week week = (Week)value;
        
        return week.getStartDate().toString("dd/MM/yy") + "-" + week.getEndDate().toString("dd/MM/yy");
     }

    
    @Override
    public boolean handles(Class<?> valueClass) {
        
        Preconditions.checkNotNull(valueClass);
        
        return Week.class.equals(valueClass);
    }

    
    //------------------------ SETTERS --------------------------
    
    @Value("1")
    @Override
    public void setOrder(int order) {
        super.setOrder(order);
    }

}
