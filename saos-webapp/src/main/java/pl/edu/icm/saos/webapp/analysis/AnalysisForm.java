package pl.edu.icm.saos.webapp.analysis;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class AnalysisForm {

    private List<SeriesSearchCriteria> seriesSearchCriteriaList = Lists.newArrayList();
    
    private int xMonthStart;
    private int xYearStart;
    private int xPeriodInMonths;
    
    
    //------------------------ GETTERS --------------------------
    
    public List<SeriesSearchCriteria> getSeriesSearchCriteriaList() {
        return seriesSearchCriteriaList;
    }
    public int getxMonthStart() {
        return xMonthStart;
    }
    public int getxYearStart() {
        return xYearStart;
    }
    public int getxPeriodInMonths() {
        return xPeriodInMonths;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public void addSeriesSearchCriteria(SeriesSearchCriteria seriesSearchCriteria) {
        seriesSearchCriteriaList.add(seriesSearchCriteria);
    }
    
    //------------------------ SETTERS --------------------------
    
    public void setSeriesSearchCriteriaList(List<SeriesSearchCriteria> seriesSearchCriteriaList) {
        this.seriesSearchCriteriaList = seriesSearchCriteriaList;
    }
    public void setxMonthStart(int xMonthStart) {
        this.xMonthStart = xMonthStart;
    }
    public void setxYearStart(int xYearStart) {
        this.xYearStart = xYearStart;
    }
    public void setxPeriodInMonths(int xPeriodInMonths) {
        this.xPeriodInMonths = xPeriodInMonths;
    }
    
}
