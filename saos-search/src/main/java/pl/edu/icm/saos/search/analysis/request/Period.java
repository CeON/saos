package pl.edu.icm.saos.search.analysis.request;

import java.util.Objects;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */

public class Period {
    
    public enum PartialUnit { WEEK, MONTH, YEAR};
    
    private int value;
    private PartialUnit unit;
    
    //------------------------ CONSTRUCTORS --------------------------
    
    /**
     * @param value value of the gap, must be greater than 0, e.g. 2
     * @param unit unit of the gap, e.g. MONTH
     * 
     * @throws IllegalArgumentException if value <= 0
     * @throws NullPointerException if unit is null
     */
    public Period(int value, PartialUnit unit) {
        Preconditions.checkNotNull(unit);
        Preconditions.checkArgument(value>0);
        
        this.value = value;
        this.unit = unit;
    }
    
    //------------------------ GETTERS --------------------------
    
    public int getValue() {
        return value;
    }
    
    public PartialUnit getUnit() {
        return unit;
    }
    
    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        return Objects.hash(this.value, this.unit);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final Period other = (Period) obj;
        
        return Objects.equals(this.value, other.value)
                && Objects.equals(this.unit, other.unit);

    }

    
}