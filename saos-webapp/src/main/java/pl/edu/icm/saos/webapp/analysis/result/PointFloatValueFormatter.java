package pl.edu.icm.saos.webapp.analysis.result;

import java.math.RoundingMode;
import java.text.NumberFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;

import pl.edu.icm.saos.common.chart.formatter.AbstractPointValueFormatter;
import pl.edu.icm.saos.common.chart.formatter.PointValueFormatter;

import com.google.common.base.Preconditions;

/**
 * {@link PointValueFormatter} implementation handling float values
 * 
 * @author Łukasz Dumiszewski
 */
public class PointFloatValueFormatter extends AbstractPointValueFormatter {

    
    private int maxFractionDigits = 3;
    private RoundingMode roundingMode = RoundingMode.HALF_UP;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String format(Object value) {
        
        if (value == null) {
            return null;
        } 
        
        Float floatValue = (Float)value; 
        
        NumberFormat n = NumberFormat.getInstance(LocaleContextHolder.getLocale());
        n.setMaximumFractionDigits(maxFractionDigits);
        n.setRoundingMode(roundingMode);
        
        return n.format(floatValue);
        
    }

    @Override
    public boolean handles(Class<?> valueClass) {
        
        Preconditions.checkNotNull(valueClass);
        
        return Float.class.equals(valueClass);
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Value("1")
    @Override
    public void setOrder(int order) {
        super.setOrder(order);
    }

    /**
     * See {@link NumberFormat#setMaximumFractionDigits(int)}, defaults to 3
     */
    public void setMaxFractionDigits(int maxFractionDigits) {
        this.maxFractionDigits = maxFractionDigits;
    }

    /**
     * See {@link NumberFormat#setRoundingMode(RoundingMode)}, defaults to {@link RoundingMode#HALF_UP}
     */
    public void setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
    }

}
