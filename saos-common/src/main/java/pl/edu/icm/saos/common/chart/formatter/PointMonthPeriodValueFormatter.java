package pl.edu.icm.saos.common.chart.formatter;

import org.springframework.beans.factory.annotation.Value;

import pl.edu.icm.saos.common.chart.value.MonthPeriod;

import com.google.common.base.Preconditions;

/**
 * 
 * {@link PointValueFormatter} implementation handling {@link MonthPeriod} class
 *
 * 
 * @author Łukasz Dumiszewski
 */

public class PointMonthPeriodValueFormatter extends AbstractPointValueFormatter {

        
        
    //------------------------ LOGIC --------------------------
        
    @Override
    public String format(Object value) {
            
        if (value == null) {
            return null;
        } 
            
        MonthPeriod monthPeriod = (MonthPeriod)value;
            
        if (monthPeriod.isOneMonthPeriod()) {
                
            return format(monthPeriod.getStartYear(), monthPeriod.getEndMonth());
            
        }
            
        return format(monthPeriod.getStartYear(), monthPeriod.getStartMonth()) + "-" + format(monthPeriod.getEndYear(), monthPeriod.getEndMonth());
    }


        
    @Override
    public boolean handles(Class<?> valueClass) {
            
        Preconditions.checkNotNull(valueClass);
            
        return MonthPeriod.class.equals(valueClass);
    
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
