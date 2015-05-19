package pl.edu.icm.saos.common.chart.value;

import org.joda.time.LocalDate;

import com.google.common.base.Preconditions;

/**
 * Simple decorator of {@link LocalDate} appropriate for use as dto/json 
 * 
 * 
 * @author Łukasz Dumiszewski
 */

public class SimpleLocalDate {

    
    private LocalDate localDate;
    
    
    
    //------------------------ CONSTRUCTORS --------------------------

    public SimpleLocalDate(LocalDate localDate) {
        Preconditions.checkNotNull(localDate);
        this.localDate = localDate;
    }

    
    //------------------------ GETTERS --------------------------
    
    public int getDayOfMonth() {
        return localDate.getDayOfMonth();
    }
    
    public int getMonthOfYear() {
        return localDate.getMonthOfYear();
    }
    
    public int getYear() {
        return localDate.getYear();
    }

    
    //------------------------ LOGIC --------------------------
    
    public boolean isBefore(SimpleLocalDate simpleLocalDate) {
        Preconditions.checkNotNull(simpleLocalDate);
        return localDate.isBefore(simpleLocalDate.localDate);
    }
    
    public String toString(String pattern) {
        return localDate.toString(pattern);
    }
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return this.localDate.hashCode();
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        return this.localDate.equals(obj);

    }


   

    
    
    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return "SimpleLocalDate [dayOfMonth=" + localDate.getDayOfMonth() + 
                ", monthOfYear=" + localDate.getMonthOfYear() + 
                ", year=" + localDate.getYear() + "]";
    }

 
    
    
    
}
