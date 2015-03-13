package pl.edu.icm.saos.webapp.analysis.request;

/**
 * @author Łukasz Dumiszewski
 */

public class UixSettings {
    
    private int monthStart;
    private int yearStart;
    
    private int monthEnd;
    private int yearEnd;
    
    
    //------------------------ GETTERS --------------------------
    
    public int getMonthStart() {
        return monthStart;
    }
    
    public int getYearStart() {
        return yearStart;
    }
    
    public int getMonthEnd() {
        return monthEnd;
    }
    
    public int getYearEnd() {
        return yearEnd;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setMonthStart(int monthStart) {
        this.monthStart = monthStart;
    }
    
    public void setYearStart(int yearStart) {
        this.yearStart = yearStart;
    }
    
    public void setMonthEnd(int monthEnd) {
        this.monthEnd = monthEnd;
    }
    
    public void setYearEnd(int yearEnd) {
        this.yearEnd = yearEnd;
    }
    
    
   
    
    
   
}
