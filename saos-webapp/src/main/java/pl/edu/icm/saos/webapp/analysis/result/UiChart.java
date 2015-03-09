package pl.edu.icm.saos.webapp.analysis.result;

import java.util.List;

import com.google.common.collect.Lists;


/**
 * Representation of non-specific chart data for use on UI view.
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class UiChart {

    private List<UiSeries> seriesList = Lists.newArrayList();
    private List<String[]> xaxisTicks = Lists.newArrayList();
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public UiChart() {
        
    }
    
    public UiChart(UiSeries... series) {
        for (UiSeries s : series) {
            seriesList.add(s);
        }
    }
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * List of series data for the chart. Note that there can be more than one series for a chart.
     * 
     * */
    public List<UiSeries> getSeriesList() {
        return seriesList;
    }

    /**
     * Short labels of the series, legend.  
     */
    public List<String[]> getXaxisTicks() {
        return xaxisTicks;
    }

    
    
    //------------------------ LOGIC --------------------------
    
    public void addSeries(UiSeries series) {
        seriesList.add(series);
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setSeriesList(List<UiSeries> seriesList) {
        this.seriesList = seriesList;
    }

    public void setXaxisTicks(List<String[]> xaxisTicks) {
        this.xaxisTicks = xaxisTicks;
    }
    
   

    
    
    
    
    
    
    //------------------------ INNER CLASSES --------------------------
    
    /**
     * UI series representation, contains points.
     * 
     * @author Łukasz Dumiszewski
     *
     */
    public static class UiSeries {
        
        List<String[]> data = Lists.newArrayList();
        
        
        //------------------------ GETTERS --------------------------
        
        /**
         * Data of the series (points).
         */
        public List<String[]> getData() {
            return data;
        }
        
        //------------------------ LOGIC --------------------------
        
        public void addPoint(String x, String y) {
            addPoint(new String[]{x, y});
        }
        
        public void addPoint(String[] point) {
            data.add(point);
        }
        
        
        //------------------------ SETTERS --------------------------
        
        public void setData(List<String[]> data) {
            this.data = data;
        }
        
        
        
        
    }
    
    
    
}
