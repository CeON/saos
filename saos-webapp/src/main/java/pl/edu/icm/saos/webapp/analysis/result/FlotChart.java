package pl.edu.icm.saos.webapp.analysis.result;

import java.util.List;

import com.google.common.collect.Lists;


/**
 * Representation of non-specific chart data for use by flot javascript library.
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class FlotChart {

    
    
    private List<FlotSeries> seriesList = Lists.newArrayList();
    private List<Object[]> xticks = Lists.newArrayList();
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public FlotChart() {
        
    }
    
    public FlotChart(FlotSeries... series) {
        for (FlotSeries s : series) {
            seriesList.add(s);
        }
    }
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * List of series data for the chart. Note that there can be more than one series for a chart.
    */
    public List<FlotSeries> getSeriesList() {
        return seriesList;
    }

    /**
     * X tick labels corresponding to x tick order numbers <br/>
     * For example:<br/>
     * {1, "02/2013", 2, "03/2014} <br/>
     * <br/>
     * Broader explanation: <br/>
     * Flot chart series can have only x and y values as numbers. If one wants to display
     * other values as x ticks then one has to map x numbers to the values that
     * should be displayed. This method returns the mapping.
     * 
     */
    public List<Object[]> getXticks() {
        return xticks;
    }

   
    //------------------------ LOGIC --------------------------
    
    public void addSeries(FlotSeries series) {
        seriesList.add(series);
    }
    
    public void addXtick(String xkey, String xvalue) {
        xticks.add(new String[]{xkey, xvalue});
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setSeriesList(List<FlotSeries> seriesList) {
        this.seriesList = seriesList;
    }
   
    public void setXticks(List<Object[]> xticks) {
        this.xticks = xticks;
    }


    
    
    
    
    
    
    //------------------------ INNER CLASSES --------------------------
    
    /**
     * Flot series representation, contains points.
     * 
     * @author Łukasz Dumiszewski
     *
     */
    public static class FlotSeries {
        
        List<Number[]> points = Lists.newArrayList();
        
        
        //------------------------ GETTERS --------------------------
        
        /**
         * Data of the series (points).
         */
        public List<Number[]> getPoints() {
            return points;
        }
        
        //------------------------ LOGIC --------------------------
        
        public void addPoint(Integer x, Number y) {
            addPoint(new Number[]{x, y});
        }
        
        public void addPoint(Number[] point) {
            points.add(point);
        }
        
        
        //------------------------ SETTERS --------------------------
        
        public void setPoints(List<Number[]> points) {
            this.points = points;
        }
        
        
        
        
    }

    
    
}
