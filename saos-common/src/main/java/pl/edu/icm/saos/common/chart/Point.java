package pl.edu.icm.saos.common.chart;

import java.util.Objects;

/**
 * A simple 2-dimensional point
 * 
 * @author Łukasz Dumiszewski
 */

public class Point<X, Y> {

    private X x;
    private Y y;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public Point(X x, Y y) {
        super();
        this.x = x;
        this.y = y;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public X getX() {
        return x;
    }
    public Y getY() {
        return y;
    }


    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final Point<?,?> other = (Point<?,?>) obj;
        
        return Objects.equals(this.x, other.x)
                && Objects.equals(this.y, other.y);

    }

    
    //------------------------ toString --------------------------

    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }


   
}
