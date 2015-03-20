package pl.edu.icm.saos.common.chart.value;

import java.util.Objects;

/**
 * 
 * Representation of a month-year date
 * 
 * @author Łukasz Dumiszewski
 */

public class MonthYear {

    

    public int year;
    
    public int month;
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public MonthYear(int month, int year) {
        super();
        this.year = year;
        this.month = month;
    }
    
    //------------------------ GETTERS --------------------------
    
    public int getYear() {
        return year;
    }
    
    public int getMonth() {
        return month;
    }
    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.month, this.year);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final MonthYear other = (MonthYear) obj;
        
        return Objects.equals(this.month, other.month)
                && Objects.equals(this.year, other.year);

    }
    
    
    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return "MonthYear [year=" + year + ", month=" + month + "]";
    }
    
}
