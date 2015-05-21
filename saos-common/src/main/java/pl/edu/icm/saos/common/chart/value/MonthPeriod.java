package pl.edu.icm.saos.common.chart.value;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;

/**
 * Period of time limited by start and end months
 * 
 * @author Łukasz Dumiszewski
 */

public class MonthPeriod implements TimePeriod {

    
    private int startYear;
    private int startMonthOfYear;
    
    private int endYear;
    private int endMonthOfYear;
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    /**
     * @param startYear see {@link #getStartYear()}
     * @param startMonthOfYear see {@link #getStartMonthOfYear()}, must be between 1 and 12 (inclusively)
     * @param endYear see {@link #getEndYear()}
     * @param endMonthOfYear see {@link #getEndMonthOfYear()}, must be between 1 and 12 (inclusively)
     * 
     * @throws IllegalArgumentException if startMonth or endMonth is not between 1 and 12 or 
     * if the endYear/endMonth is before startYear/startMonth.
     */
    public MonthPeriod(int startYear, int startMonthOfYear, int endYear, int endMonthOfYear) {
        super();
        Preconditions.checkArgument(startMonthOfYear > 0 && startMonthOfYear < 13);
        Preconditions.checkArgument(endMonthOfYear > 0 && endMonthOfYear < 13);
        checkEndNotBeforeStart(startYear, startMonthOfYear, endYear, endMonthOfYear);
        
        this.startYear = startYear;
        this.startMonthOfYear = startMonthOfYear;
        this.endYear = endYear;
        this.endMonthOfYear = endMonthOfYear;
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
    public int getStartMonthOfYear() {
        return startMonthOfYear;
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
    public int getEndMonthOfYear() {
        return endMonthOfYear;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Is this period a one month period, i.e. start year-month == end year-month 
     */
    @JsonIgnore
    public boolean isOneMonthPeriod() {
        return startYear == endYear && startMonthOfYear == endMonthOfYear;
    }
    


    @Override
    public TimePeriodType getPeriod() {
        return TimePeriodType.MONTH;
    }
    
    
    //------------------------ PRIVATE --------------------------

    private void checkEndNotBeforeStart(int startYear, int startMonth, int endYear, int endMonth) {
        Preconditions.checkArgument(endYear > startYear || (endYear == startYear && endMonth >= startMonth));
    }
    
    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.startYear, this.startMonthOfYear, this.endYear, this.endMonthOfYear);
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
                && Objects.equals(this.startMonthOfYear, other.startMonthOfYear)
                && Objects.equals(this.endYear, other.endYear)
                && Objects.equals(this.endMonthOfYear, other.endMonthOfYear);

    }


    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return "MonthPeriod [startYear=" + startYear + ", startMonthOfYear=" + startMonthOfYear + ", endYear="
                + endYear + ", endMonthOfYear=" + endMonthOfYear + "]";
    }



}
