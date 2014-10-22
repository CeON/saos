package pl.edu.icm.saos.persistence.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class SupremeCourtJudgmentTest {

    
    private SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
    
    private SupremeCourtChamber scChamberABC = new SupremeCourtChamber();
    private SupremeCourtChamber scChamberDEF = new SupremeCourtChamber();
    
    
    @Before
    public void before() {
        scChamberABC.setName("ABC");
        scChamberDEF.setName("DEF");
    }
    
    
    @Test
    public void addScChamber() {
        
        assertEquals(0, scJudgment.getScChambers().size());
        
        scJudgment.addScChamber(scChamberABC);
        
        assertEquals(1, scJudgment.getScChambers().size());
        assertTrue(scChamberABC == scJudgment.getScChambers().get(0));
        
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void addScChamber_SameExists() {
        
        scJudgment.addScChamber(scChamberABC);
        
        scJudgment.addScChamber(scChamberABC);
        
    }
    
    
    
    @Test
    public void containsScChamber() {
        
        assertFalse(scJudgment.containsScChamber(scChamberABC));
        assertFalse(scJudgment.containsScChamber(scChamberDEF));
        
        scJudgment.addScChamber(scChamberABC);
        scJudgment.addScChamber(scChamberDEF);
        
        assertTrue(scJudgment.containsScChamber(scChamberABC));
        assertTrue(scJudgment.containsScChamber(scChamberDEF));
        
    }
    
    
    @Test
    public void removeScChamber() {
        
        scJudgment.addScChamber(scChamberABC);
        scJudgment.addScChamber(scChamberDEF);
        
        scJudgment.removeScChamber(scChamberABC);
        
        assertEquals(1, scJudgment.getScChambers().size());
        assertEquals(scChamberDEF, scJudgment.getScChambers().get(0));
    }
    
    
    
}
