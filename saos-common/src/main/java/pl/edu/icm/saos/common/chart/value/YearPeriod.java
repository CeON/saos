package pl.edu.icm.saos.common.chart.value;

import java.util.Objects;

import com.google.common.base.Preconditions;

/**
 * Period of time limited by start and end year (inclusively)
 * 
 * @author Łukasz Dumiszewski
 */

public class YearPeriod {

    
    private int startYear;
    
    private int endYear;
    
    
    
    //------------------------ CONSTRUCTORS --------------------------

    /**
     * 
     * @param startYear see {@link #getStartYear()}
     * @param endYear see {@link #getEndYear()}
     * 
     * @throws IllegalArgumentException if startYear is greater than endYear
     */
    public YearPeriod(int startYear, int endYear) {
        super();
        Preconditions.checkArgument(startYear <= endYear);
        this.startYear = startYear;
        this.endYear = endYear;
    }

    
    //------------------------ GETTERS --------------------------
    
    /**
     * Start year of a period (inclusive)
     */
    public int getStartYear() {
        return startYear;
    }
    
    /**
     * End year of a period (inclusive)
     */
    public int getEndYear() {
        return endYear;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Is this period a one year period, i.e. {@link #getStartYear()} equals {@link #getEndYear()} 
     */
    
    public boolean isOneYearPeriod() {
        return startYear == endYear;
    }
    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.startYear, this.endYear);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final YearPeriod other = (YearPeriod) obj;
        
        return Objects.equals(this.startYear, other.startYear)
                && Objects.equals(this.endYear, other.endYear);

    }


    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return "YearPeriod [startYear=" + startYear + ", endYear=" + endYear + "]";
    }



}
