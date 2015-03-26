package pl.edu.icm.saos.common.chart.value;

import java.util.Objects;

import org.joda.time.LocalDate;

import com.google.common.base.Preconditions;

/**
 * Period of time limited by start and end day (inclusively)
 * 
 * @author Łukasz Dumiszewski
 */

public class DayPeriod {

    
    private LocalDate startDay;
    private LocalDate endDay;
    
    
    
    //------------------------ CONSTRUCTORS --------------------------

    /**
     * 
     * @param startDay see {@link #getStartDay()}, must not be null
     * @param endDay see {@link #getEndDay()}, must not be null
     * 
     * @throws IllegalArgumentException if the end day is before start day
     * @throws NullPointerException if startDay or endDay is null
     */
    public DayPeriod(LocalDate startDay, LocalDate endDay) {
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
    public LocalDate getStartDay() {
        return startDay;
    }
    
    /**
     * End date of the range period (inclusive)
     */
    public LocalDate getEndDay() {
        return endDay;
    }
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Is this period a one day period, i.e. {@link #getStartDay()} equals {@link #getEndDay()} 
     */
    public boolean isOneDayPeriod() {
        return startDay.equals(endDay);
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
