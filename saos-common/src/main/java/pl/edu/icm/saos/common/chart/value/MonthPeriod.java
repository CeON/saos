package pl.edu.icm.saos.common.chart.value;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;

/**
 * Period of time limited by start and end months
 * 
 * @author Łukasz Dumiszewski
 */

public class MonthPeriod {

    
    private int startYear;
    private int startMonth;
    
    private int endYear;
    private int endMonth;
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    /**
     * @param startYear see {@link #getStartYear()}
     * @param startMonth see {@link #getStartMonth()}, must be between 1 and 12 (inclusively)
     * @param endYear see {@link #getEndYear()}
     * @param endMonth see {@link #getEndMonth()}, must be between 1 and 12 (inclusively)
     * 
     * @throws IllegalArgumentException if startMonth or endMonth is not between 1 and 12 or 
     * if the endYear/endMonth is before startYear/startMonth.
     */
    public MonthPeriod(int startYear, int startMonth, int endYear, int endMonth) {
        super();
        Preconditions.checkArgument(startMonth > 0 && startMonth < 13);
        Preconditions.checkArgument(endMonth > 0 && endMonth < 13);
        checkEndNotBeforeStart(startYear, startMonth, endYear, endMonth);
        
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.endYear = endYear;
        this.endMonth = endMonth;
    }


    
    
    //------------------------ GETTERS --------------------------
    
    
    /**
     * Start year of the period 
     */
    public int getStartYear() {
        return startYear;
    }
    
    /**
     * Start month a the period
     */
    public int getStartMonth() {
        return startMonth;
    }
    
    /**
     * End year of the period 
     */
    public int getEndYear() {
        return endYear;
    }
    
    /**
     * End month of the period 
     */
    public int getEndMonth() {
        return endMonth;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Is this period a one month period, i.e. start year-month == end year-month 
     */
    @JsonIgnore
    public boolean isOneMonthPeriod() {
        return startYear == endYear && startMonth == endMonth;
    }
    
    //------------------------ PRIVATE --------------------------

    private void checkEndNotBeforeStart(int startYear, int startMonth, int endYear, int endMonth) {
        Preconditions.checkArgument(endYear > startYear || (endYear == startYear && endMonth >= startMonth));
    }
    
    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.startYear, this.startMonth, this.endYear, this.endMonth);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final MonthPeriod other = (MonthPeriod) obj;
        
        return Objects.equals(this.startYear, other.startYear)
                && Objects.equals(this.startMonth, other.startMonth)
                && Objects.equals(this.endYear, other.endYear)
                && Objects.equals(this.endMonth, other.endMonth);

    }


    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return "MonthPeriod [startYear=" + startYear + ", startMonth=" + startMonth + ", endYear="
                + endYear + ", endMonth=" + endMonth + "]";
    }
    
}
