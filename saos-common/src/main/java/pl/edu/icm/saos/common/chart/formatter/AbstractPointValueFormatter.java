package pl.edu.icm.saos.common.chart.formatter;

/**
 * 
 * An abstract implementation of {@link PointValueFormatter} facilitating writing of specific formatters.
 * 
 * @author Łukasz Dumiszewski
 */

public abstract class AbstractPointValueFormatter implements PointValueFormatter {

    
    private int order;
    
    
    //------------------------ GETTERS --------------------------
    
    @Override
    public int getOrder() {
        return order;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setOrder(int order) {
        this.order = order;
    }
}
