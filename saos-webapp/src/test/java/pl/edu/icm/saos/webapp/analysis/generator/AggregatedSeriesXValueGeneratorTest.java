package pl.edu.icm.saos.webapp.analysis.generator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class AggregatedSeriesXValueGeneratorTest {

    private AggregatedSeriesXValueGenerator aggregatedSeriesXValueGenerator = new AggregatedSeriesXValueGenerator();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void generateXValue() {
        
        // execute & assert
        assertEquals(1, aggregatedSeriesXValueGenerator.generateXValue(0));
        assertEquals(3, aggregatedSeriesXValueGenerator.generateXValue(1));
        assertEquals(5, aggregatedSeriesXValueGenerator.generateXValue(2));
        assertEquals(7, aggregatedSeriesXValueGenerator.generateXValue(3));
        
    }
    
}
