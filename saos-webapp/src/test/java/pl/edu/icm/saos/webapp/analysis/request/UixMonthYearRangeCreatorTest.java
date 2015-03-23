package pl.edu.icm.saos.webapp.analysis.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.XField;

/**
 * @author Łukasz Dumiszewski
 */

public class UixMonthYearRangeCreatorTest {

    
    private UixMonthYearRangeCreator creator = new UixMonthYearRangeCreator();
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void handles_NULL() {
        
        // execute
        creator.handles(null);
        
    }

    
    @Test
    public void handles() {
        
        // execute & assert
        assertTrue(creator.handles(XField.JUDGMENT_DATE));
        
    }
    
    
    @Test
    public void createRange() {
        
        // execute
        UixMonthYearRange uixRange = creator.createRange();
        
        // assert
        LocalDate today = new LocalDate();
        assertEquals(today.getYear()-20, uixRange.getStartYear());
        assertEquals(1, uixRange.getStartMonth());
        assertEquals(today.getYear(), uixRange.getEndYear());
        assertEquals(today.getMonthOfYear(), uixRange.getEndMonth());
        
    }

}
