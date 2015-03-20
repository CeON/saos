package pl.edu.icm.saos.webapp.analysis.request;

import com.google.common.base.Preconditions;


/**
 * 
 * An x range defined as a period limited by start and end month-year pairs.
 * 
 * @author Łukasz Dumiszewski
 */

public class UixMonthYearRange implements UixRange {

    private int startMonth;
    private int startYear;
    
    private int endMonth;
    private int endYear;
    
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * Start month of the range 
     */
    public int getStartMonth() {
        return startMonth;
    }
    
    /**
     * Start year of the range 
     */
    public int getStartYear() {
        return startYear;
    }
    
    /**
     * End month of the year
     */
    public int getEndMonth() {
        return endMonth;
    }
    
    /**
     * End year of the range
     */
    public int getEndYear() {
        return endYear;
    }
    
    

   
    //------------------------ SETTERS --------------------------
    
    public void setStartMonth(int startMonth) {
        
        Preconditions.checkArgument(startMonth > 0);
        
        this.startMonth = startMonth;
    }
    
    
    public void setStartYear(int startYear) {
        
        Preconditions.checkArgument(startYear > 0);
        
        this.startYear = startYear;
    }
    
    
    public void setEndMonth(int endMonth) {
        
        Preconditions.checkArgument(endMonth > 0);
        
        this.endMonth = endMonth;
    }
    
    
    public void setEndYear(int endYear) {
        
        Preconditions.checkArgument(endYear > 0);
        
        this.endYear = endYear;
    }
    
}
