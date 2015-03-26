package pl.edu.icm.saos.common.chart.formatter;

import org.springframework.beans.factory.annotation.Value;

import pl.edu.icm.saos.common.chart.value.YearPeriod;

import com.google.common.base.Preconditions;

/**
 * {@link PointValueFormatter} implementation handling {@link YearPeriod} class
 *
 * @author Łukasz Dumiszewski
 */

public class PointYearPeriodValueFormatter extends AbstractPointValueFormatter {

        
        
    //------------------------ LOGIC --------------------------
        
    @Override
    public String format(Object value) {
            
        if (value == null) {
            return null;
        } 
            
        YearPeriod yearPeriod = (YearPeriod)value;
            
        if (yearPeriod.isOneYearPeriod()) {
                
            return ""+yearPeriod.getStartYear();
            
        }
            
        return yearPeriod.getStartYear() + "-" + yearPeriod.getEndYear();
    }


        
    @Override
    public boolean handles(Class<?> valueClass) {
            
        Preconditions.checkNotNull(valueClass);
            
        return YearPeriod.class.equals(valueClass);
    
    }

        

    
    //------------------------ PRIVATE --------------------------
        
    
    private String format(int year, int month) {
        
        return month + "/" + year;
        
    }

        
    //------------------------ SETTERS --------------------------
        
    @Value("1")
    @Override
    public void setOrder(int order) {
        super.setOrder(order);
    }
    
}
