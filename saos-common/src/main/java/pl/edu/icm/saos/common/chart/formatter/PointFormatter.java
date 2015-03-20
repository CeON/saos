package pl.edu.icm.saos.common.chart.formatter;

import pl.edu.icm.saos.common.chart.Point;

/**
 * {@link Point} formatter
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class PointFormatter {

    
    private PointValueFormatterManager pointValueFormatterManager;
    
    
    //------------------------ LOGIC --------------------------

    /**
     * Formats the given {@link Point} into {@link String[]} 
     */
    public String[] formatPoint(Point<?, ?> point) {
        
        String x = pointValueFormatterManager.format(point.getX());
        String y = pointValueFormatterManager.format(point.getY());
        
        return new String[]{x, y};
        
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setPointValueFormatterManager(PointValueFormatterManager pointValueFormatterManager) {
        this.pointValueFormatterManager = pointValueFormatterManager;
    }
   
    
}
