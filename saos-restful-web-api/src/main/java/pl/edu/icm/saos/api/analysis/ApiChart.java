package pl.edu.icm.saos.api.analysis;

import java.util.List;

import com.google.common.collect.Lists;


/**
 * Representation of non-specific chart data for use in API
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class ApiChart {

    
    
    private List<ApiSeries> seriesList = Lists.newArrayList();
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public ApiChart() {
        
    }
    
    public ApiChart(ApiSeries... series) {
        for (ApiSeries s : series) {
            seriesList.add(s);
        }
    }
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * List of series data for the chart. Note that there can be more than one series for a chart.
    */
    public List<ApiSeries> getSeriesList() {
        return seriesList;
    }

   
    //------------------------ LOGIC --------------------------
    
    public void addSeries(ApiSeries series) {
        seriesList.add(series);
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setSeriesList(List<ApiSeries> seriesList) {
        this.seriesList = seriesList;
    }
   
    
    
    
    
    
    
    //------------------------ INNER CLASSES --------------------------
    
    /**
     * UI series representation, contains points.
     * 
     * @author Łukasz Dumiszewski
     *
     */
    public static class ApiSeries {
        
        List<Object[]> points = Lists.newArrayList();
        
        
        //------------------------ GETTERS --------------------------
        
        /**
         * Data of the series (points).
         */
        public List<Object[]> getPoints() {
            return points;
        }
        
        //------------------------ LOGIC --------------------------
        
        public void addPoint(String x, Object y) {
            addPoint(new Object[]{x, y});
        }
        
        public void addPoint(Object[] point) {
            points.add(point);
        }
        
        
        //------------------------ SETTERS --------------------------
        
        public void setPoints(List<Object[]> points) {
            this.points = points;
        }
        
        
        
        
    }







    
    
    
}
