package pl.edu.icm.saos.importer.commoncourt.process;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;


/**
 * @author Łukasz Dumiszewski
 */

public class CcJudgmentOverwriterTest {

    
    private CcJudgmentOverwriter ccJudgmentOverwriter = new CcJudgmentOverwriter();
    
    
  
    
    
    @Test
    public void overwriteSpecificData_courtData_division() {
        CommonCourtJudgment oldJudgment = new CommonCourtJudgment();
        CommonCourtDivision division = new CommonCourtDivision();
        division.setCode("1234");
        CommonCourt court = new CommonCourt();
        division.setCourt(court);
        oldJudgment.setCourtDivision(division);
        
        CommonCourtJudgment newJudgment = new CommonCourtJudgment();
        
        CommonCourtDivision newDivision = new CommonCourtDivision();
        newDivision.setCode("1234");
        CommonCourt newCourt = new CommonCourt();
        newDivision.setCourt(newCourt);
        newJudgment.setCourtDivision(newDivision);
        
        ccJudgmentOverwriter.overwriteSpecificData(oldJudgment, newJudgment);
        
        
        
        assertEquals(newDivision, oldJudgment.getCourtDivision());
        assertEquals(newDivision, newJudgment.getCourtDivision());
        
    }
    
}
