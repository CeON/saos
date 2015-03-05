package pl.edu.icm.saos.search.analysis.request;


import org.joda.time.LocalDate;

/**
 * @author Łukasz Dumiszewski
 */

public class XDateRange implements XRange {

    
    private LocalDate startDate;
    private LocalDate endDate; 
    private Period gap;
    

    //------------------------ GETTERS --------------------------
    
    public LocalDate getStartDate() {
        return startDate; 
    }

    public LocalDate getEndDate() {
        return endDate;
    }

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
    
    
    //------------------------ INNER CLASSES --------------------------
    
   

}
