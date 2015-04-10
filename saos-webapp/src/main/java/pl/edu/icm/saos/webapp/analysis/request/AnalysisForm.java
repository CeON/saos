package pl.edu.icm.saos.webapp.analysis.request;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 
 * An analysis form data holder
 * 
 * @author Łukasz Dumiszewski
 */

public class AnalysisForm {

    private List<JudgmentSeriesFilter> seriesFilters = Lists.newArrayList();
    
    private JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
    
    private UiySettings ysettings = new UiySettings();
    
    
    
    //------------------------ GETTERS --------------------------
    
    public List<JudgmentSeriesFilter> getSeriesFilters() {
        return seriesFilters;
    }
    
    public JudgmentGlobalFilter getGlobalFilter() {
        return globalFilter;
    }

    public UiySettings getYsettings() {
        return ysettings;
    }


    
    
    
    //------------------------ LOGIC --------------------------
    
    public void addSeriesFilter(JudgmentSeriesFilter seriesSearchCriteria) {
        seriesFilters.add(seriesSearchCriteria);
    }


    
    //------------------------ SETTERS --------------------------
    

    public void setSeriesFilters(List<JudgmentSeriesFilter> seriesFilters) {
        this.seriesFilters = seriesFilters;
    }

    public void setGlobalFilter(JudgmentGlobalFilter globalFilter) {
        this.globalFilter = globalFilter;
    }

    public void setYsettings(UiySettings ysettings) {
        this.ysettings = ysettings;
    }
       

   
   
    
}
