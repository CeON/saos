package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class ScChamberDivisionNameExtractorTest {

    private ScChamberDivisionNameExtractor scChamberDivisionNameExtractor = new ScChamberDivisionNameExtractor();
    
    
    
    @Test(expected=IllegalArgumentException.class)
    public void extractDivisionName_IllegalArgument() {
    
        scChamberDivisionNameExtractor.extractDivisionName("Izba Karna Wyd 1");
    
    }
       
    
    
    @Test
    public void extractDivisionName() {
        
        String divisionName = scChamberDivisionNameExtractor.extractDivisionName("Izba Karna Wydział 1");
        
        assertEquals("Wydział 1", divisionName);
    
    }
    
    
    @Test
    public void extractChamberName() {
        
        String divisionName = scChamberDivisionNameExtractor.extractChamberName("Izba Karna Wydział 1");
        
        assertEquals("Izba Karna", divisionName);
        
    }
}
