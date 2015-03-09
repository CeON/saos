package pl.edu.icm.saos.webapp.analysis.request;

/**
 * @author Łukasz Dumiszewski
 */

public class UiXSettings {
    
    private int xMonthStart;
    private int xYearStart;
    private int xPeriodInMonths;
    
    
    
    //------------------------ GETTERS --------------------------
    
    public int getXMonthStart() {
        return xMonthStart;
    }
    
    public int getXYearStart() {
        return xYearStart;
    }
    
    public int getXPeriodInMonths() {
        return xPeriodInMonths;
    }
    
    
    
    //------------------------ SETTERS --------------------------
    
    
    public void setXMonthStart(int xMonthStart) {
        this.xMonthStart = xMonthStart;
    }
    
    public void setXYearStart(int xYearStart) {
        this.xYearStart = xYearStart;
    }
    
    public void setXPeriodInMonths(int xPeriodInMonths) {
        this.xPeriodInMonths = xPeriodInMonths;
    }
    
}
