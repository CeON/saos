package pl.edu.icm.saos.search.analysis.result;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;


/**
 * Representation of non-specific chart data.
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class Chart<X, Y> implements Serializable {



    private static final long serialVersionUID = 1L;

    private List<Series<X, Y>> seriesList = Lists.newArrayList();
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    
    public Chart() {
        
    }
    
    
    public Chart(List<Series<X, Y>> seriesList) {
        super();
        this.seriesList = seriesList;
    }


    
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * List of series for the chart. Note that there can be more than one series for a chart.
     * 
     * */
    public List<Series<X, Y>> getSeriesList() {
        return seriesList;
    }

   
    
    
    //------------------------ LOGIC --------------------------
    
    public void addSeries(Series<X, Y> series) {
        seriesList.add(series);
    }
    
    
    
    
    //------------------------ SETTERS --------------------------
    
    public void setSeriesList(List<Series<X, Y>> seriesList) {
        this.seriesList = seriesList;
    }
    
    
    
    
    
    
    
}
