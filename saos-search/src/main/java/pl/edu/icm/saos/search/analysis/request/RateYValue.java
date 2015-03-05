package pl.edu.icm.saos.search.analysis.request;

import java.util.Objects;

import com.google.common.base.Preconditions;

/**
 * {@link YValueType} implementation that represents y value as a rate (for example the number of judgments meeting
 * certain criteria per 1000 of judgments)
 *  
 * @author Łukasz Dumiszewski
 */

public class RateYValue implements YValueType {

    public int rateRatio;
   
    
    //------------------------ CONSTRUCTORS --------------------------
    
    /**
     * 
     * @param see {@link #getRateRatio()}
     * @throws IllegalArgumentException if rateRatio <= 0
     */
    public RateYValue(int rateRatio) {
        super();
        Preconditions.checkArgument(rateRatio > 0);
        this.rateRatio = rateRatio;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    
    /**
     * Gives the value used for calculating rate value. One can have y-values that are not absolute numbers
     * of results but results per specific number (rate ratio is exactly this number).
     * <br/><br/>
     * Let's assume: <br/>
     * <li>A = the number of results</li>
     * <li>B = the number of all possible results</li>
     * <li>C = the rate ratio</li>
     * Then the y-value of the given point will be calculated as: A*(C/B)  
     */
    public int getRateRatio() {
        return rateRatio;
    }

    
    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        return Objects.hash(this.rateRatio);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final RateYValue other = (RateYValue) obj;
        
        return Objects.equals(this.rateRatio, other.rateRatio);

    }
    
}
