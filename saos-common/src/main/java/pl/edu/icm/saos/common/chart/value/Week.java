package pl.edu.icm.saos.common.chart.value;

import java.util.Objects;

import org.joda.time.LocalDate;

import com.google.common.base.Preconditions;

/**
 * 
 * Representation of a week. A week is 7 days or less long (if one wants to represent incomplete week, i.e. a week
 * that has not finished yet)
 * 
 * @author Łukasz Dumiszewski
 */

public class Week {

    public LocalDate startDate;
    
    public LocalDate endDate;
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    /**
     * 
     * @param startDate {@link #getStartDate()}, must not be null and must be before end date (no more
     * than 6 days)
     * @param endDate {@link #getEndDate(), must not be null and must be after the start date (no more than 6 days)
     */
    public Week(LocalDate startDate, LocalDate endDate) {
        super();
        
        Preconditions.checkNotNull(startDate);
        Preconditions.checkNotNull(endDate);
        Preconditions.checkArgument(endDate.isAfter(startDate));
        Preconditions.checkArgument(endDate.minusDays(7).isBefore(startDate));
        
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * First day of the week (inclusive) 
     */
    public LocalDate getStartDate() {
        return startDate;
    }
    
    /**
     * Last day of the week (inclusive). 
     */
    public LocalDate getEndDate() {
        return endDate;
    }
    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.startDate, this.endDate);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final Week other = (Week) obj;
        
        return Objects.equals(this.startDate, other.startDate)
                && Objects.equals(this.endDate, other.endDate);

    }


    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "Week [startDate=" + startDate + ", endDate=" + endDate + "]";
    }
    
}
