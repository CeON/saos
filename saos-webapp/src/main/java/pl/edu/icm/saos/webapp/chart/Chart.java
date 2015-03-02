package pl.edu.icm.saos.webapp.chart;

import java.util.List;

import com.google.common.collect.Lists;


/**
 * Representation of non-specific chart data.
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class Chart {

    private List<Series> seriesList = Lists.newArrayList();
    private List<String[]> xaxisTicks = Lists.newArrayList();
    
    
    public Chart() {
        
    }
    
    public Chart(Series... series) {
        for (Series s : series) {
            seriesList.add(s);
        }
    }
    
    
    //******************** GETTERS ********************
    
    /**
     * List of series data for the chart. Note that there can be more than one series for a chart.
     * 
     * */
    public List<Series> getSeriesList() {
        return seriesList;
    }

    public List<String[]> getXaxisTicks() {
        return xaxisTicks;
    }

    
    //******************** LOGIC ********************
    
    public void addSeries(Series series) {
        seriesList.add(series);
    }
    
    
    //******************** SETTERS ********************
    
    public void setSeriesList(List<Series> seriesList) {
        this.seriesList = seriesList;
    }

    public void setXaxisTicks(List<String[]> xaxisTicks) {
        this.xaxisTicks = xaxisTicks;
    }
    
   

    
    
    
    
    
    
    //******************** SERIES CLASS ********************
    
    public static class Series {
        
        List<String[]> data = Lists.newArrayList();
        
        
        //******************** GETTERS ********************
        
        public List<String[]> getData() {
            return data;
        }
        
        //******************** LOGIC ********************
        
        public void addPoint(String x, String y) {
            addPoint(new String[]{x, y});
        }
        
        public void addPoint(String[] point) {
            data.add(point);
        }
        
        
        //******************** SETTERS ********************
        
        public void setData(List<String[]> data) {
            this.data = data;
        }
        
        
        
        
    }
    
    
    
}
