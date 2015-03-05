package pl.edu.icm.saos.search.analysis.request;

import com.google.common.base.Preconditions;

/**
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
    public Integer getRateRatio() {
        return rateRatio;
    }

    
}
