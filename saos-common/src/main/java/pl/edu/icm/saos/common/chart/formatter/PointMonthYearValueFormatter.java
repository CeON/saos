package pl.edu.icm.saos.common.chart.formatter;

import org.springframework.beans.factory.annotation.Value;

import pl.edu.icm.saos.common.chart.value.MonthYear;

import com.google.common.base.Preconditions;

/**
 * {@link PointValueFormatter} implementation handling {@link MonthYear} class
 * 
 * @author Łukasz Dumiszewski
 */
public class PointMonthYearValueFormatter extends AbstractPointValueFormatter {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String format(Object value) {
        
        if (value == null) {
            return null;
        } 
        
        MonthYear monthYear = (MonthYear)value;
        
        return monthYear.getMonth() + "/" + monthYear.getYear();
        
    }

    
    @Override
    public boolean handles(Class<?> valueClass) {
        
        Preconditions.checkNotNull(valueClass);
        
        return MonthYear.class.equals(valueClass);
    }

    
    //------------------------ SETTERS --------------------------
    
    @Value("1")
    @Override
    public void setOrder(int order) {
        super.setOrder(order);
    }

}