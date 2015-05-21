package pl.edu.icm.saos.common.chart.value;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;

/**
 * Period of time limited by start and end day (inclusively)
 * 
 * @author Łukasz Dumiszewski
 */

public class DayPeriod implements TimePeriod {

    
    private SimpleLocalDate startDay;
    private SimpleLocalDate endDay;
    
    
    
    //------------------------ CONSTRUCTORS --------------------------

    /**
     * 
     * @param startDay see {@link #getStartDay()}, must not be null
     * @param endDay see {@link #getEndDay()}, must not be null
     * 
     * @throws IllegalArgumentException if the end day is before start day
     * @throws NullPointerException if startDay or endDay is null
     */
    public DayPeriod(SimpleLocalDate startDay, SimpleLocalDate endDay) {
        super();
        Preconditions.checkNotNull(startDay);
        Preconditions.checkNotNull(endDay);
        Preconditions.checkArgument(!endDay.isBefore(startDay));
        this.startDay = startDay;
        this.endDay = endDay;
    }

    
    //------------------------ GETTERS --------------------------
    
    /**
     * Start date of the range period (inclusive)
     */
    public SimpleLocalDate getStartDay() {
        return startDay;
    }
    
    /**
     * End date of the range period (inclusive)
     */
    public SimpleLocalDate getEndDay() {
        return endDay;
    }
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Is this period a one day period, i.e. {@link #getStartDay()} equals {@link #getEndDay()} 
     */
    @JsonIgnore
    public boolean isOneDayPeriod() {
        return startDay.equals(endDay);
    }
   
    @Override
    public TimePeriodType getPeriod() {
        return TimePeriodType.DAY;
    }

    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.startDay, this.endDay);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final DayPeriod other = (DayPeriod) obj;
        
        return Objects.equals(this.startDay, other.startDay)
                && Objects.equals(this.endDay, other.endDay);

    }


    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return "DayPeriod [startDay=" + startDay + ", endDay=" + endDay + "]";
    }




    
    
}
