package pl.edu.icm.saos.webapp.analysis.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.result.Point;

/**
 * {@link Point} formatter
 * 
 * @author Łukasz Dumiszewski
 *
 */
@Service("pointFormatter")
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
    
    @Autowired
    public void setPointValueFormatterManager(PointValueFormatterManager pointValueFormatterManager) {
        this.pointValueFormatterManager = pointValueFormatterManager;
    }
   
    
}
