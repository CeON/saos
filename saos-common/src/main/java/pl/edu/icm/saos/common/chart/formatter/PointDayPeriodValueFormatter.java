package pl.edu.icm.saos.common.chart.formatter;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

import pl.edu.icm.saos.common.chart.value.DayPeriod;

import com.google.common.base.Preconditions;

/**
 * 
 * {@link PointValueFormatter} implementation handling {@link DayPeriod} class
 * 
 * @author Łukasz Dumiszewski
 */

public class PointDayPeriodValueFormatter extends AbstractPointValueFormatter {

        
        
    //------------------------ LOGIC --------------------------
        
    @Override
    public String format(Object value) {
            
        if (value == null) {
            return null;
        } 
            
        DayPeriod dayPeriod = (DayPeriod)value;
            
        if (dayPeriod.isOneDayPeriod()) {
                
            return formatDate(dayPeriod.getStartDay());
            
        }
            
        return formatDate(dayPeriod.getStartDay()) + "-" + formatDate(dayPeriod.getEndDay());
    }


        
    @Override
    public boolean handles(Class<?> valueClass) {
            
        Preconditions.checkNotNull(valueClass);
            
        return DayPeriod.class.equals(valueClass);
    
    }

        

    
    //------------------------ PRIVATE --------------------------
        
    
    private String formatDate(LocalDate date) {
        
        return date.toString("dd/MM/yy");
        
    }

        
    //------------------------ SETTERS --------------------------
        
    @Value("1")
    @Override
    public void setOrder(int order) {
        super.setOrder(order);
    }
    
}
