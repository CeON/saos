package pl.edu.icm.saos.common.chart.value;

/**
 * Period of time
 * 
 * @author Łukasz Dumiszewski
 */

public interface TimePeriod {

    public enum TimePeriodType {YEAR, MONTH, DAY};
    
    
    /**
     * Returns the {@link TimePeriodType} represented by the specific implementation.
     */
    public TimePeriodType getPeriod();
    
}
