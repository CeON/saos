package pl.edu.icm.saos.webapp.analysis.result;

import org.springframework.beans.factory.annotation.Value;

import pl.edu.icm.saos.common.chart.formatter.AbstractPointValueFormatter;
import pl.edu.icm.saos.common.chart.value.Week;

import com.google.common.base.Preconditions;

/**
 * 
 * Formatter handling {@link Week} class breaking startDate and endDate into separate lines
 * 
 * @author Łukasz Dumiszewski
 */
public class PointWeekBrValueFormatter extends AbstractPointValueFormatter {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String format(Object value) {
        
        if (value == null) {
            return null;
        } 
        
        Week week = (Week)value;
        
        return week.getStartDate().toString("dd/MM/yy") + "<br/>-<br/>" + week.getEndDate().toString("dd/MM/yy");
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
