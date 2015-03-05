package pl.edu.icm.saos.search.analysis.request;


import java.util.Objects;

import org.joda.time.LocalDate;

/**
 * Date range of the x values
 * 
 * @author Łukasz Dumiszewski
 */

public class XDateRange implements XRange {

    
    private LocalDate startDate;
    private LocalDate endDate; 
    private Period gap;
    

    //------------------------ GETTERS --------------------------
    
    /**
     * Start date of the range 
     */
    public LocalDate getStartDate() {
        return startDate; 
    }

    /**
     * End date of the range
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * The gap between the x values, e.g. 1 MONTH. 
     */
    public Period getGap() {
        return gap;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setGap(Period gap) {
        this.gap = gap;
    }
    
    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.startDate, this.endDate, this.gap);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final XDateRange other = (XDateRange) obj;
        
        return Objects.equals(this.startDate, other.startDate) &&
               Objects.equals(this.endDate, other.endDate) &&
               Objects.equals(this.gap, other.gap);

    }
    
   

}
