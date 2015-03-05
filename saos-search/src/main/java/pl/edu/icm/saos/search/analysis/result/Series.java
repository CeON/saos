package pl.edu.icm.saos.search.analysis.result;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

/**
 * Series of 2D points.
 * 
 * @author Łukasz Dumiszewski
 */

public class Series<X, Y> {
    
    
    private List<Point<X, Y>> points = Lists.newArrayList();
    
    
    //------------------------ GETTERS --------------------------
    
    public List<Point<X, Y>> getPoints() {
        return points;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public void addPoint(X x, Y y) {
        addPoint(new Point<>(x, y));
    }
    
    public void addPoint(Point<X, Y> point) {
        points.add(point);
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setPoints(List<Point<X, Y>> points) {
        this.points = points;
    }
    
//------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.points);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final Series<?,?> other = (Series<?,?>) obj;
        
        return Objects.equals(this.points, other.points);

    }

}
