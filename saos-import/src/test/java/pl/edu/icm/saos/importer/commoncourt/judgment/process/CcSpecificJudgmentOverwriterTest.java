package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.hamcrest.Matchers;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;


/**
 * @author Łukasz Dumiszewski
 */

public class CcSpecificJudgmentOverwriterTest {

    
    private CcSpecificJudgmentOverwriter ccJudgmentOverwriter = new CcSpecificJudgmentOverwriter();
    
    
  
    
    
    @Test
    public void overwriteJudgment_courtData_division() {
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
        
        ccJudgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        
        
        assertEquals(newDivision, oldJudgment.getCourtDivision());
        assertEquals(newDivision, newJudgment.getCourtDivision());
        
    }
    
    
    @Test
    public void overwriteJudgment_Keywords() {
        
        CcJudgmentKeyword keywordABC = new CcJudgmentKeyword("ABC");
        CcJudgmentKeyword keywordDEF = new CcJudgmentKeyword("DEF");
        CcJudgmentKeyword keywordGHI = new CcJudgmentKeyword("GHI");
        
        
        CommonCourtJudgment oldJudgment = new CommonCourtJudgment();
        oldJudgment.addKeyword(keywordABC);
        oldJudgment.addKeyword(keywordDEF);
        
        
        CommonCourtJudgment newJudgment = new CommonCourtJudgment();
        newJudgment.addKeyword(keywordABC);
        newJudgment.addKeyword(keywordGHI);
        
        
        ccJudgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        
        assertThat(newJudgment.getKeywords(), Matchers.containsInAnyOrder(keywordABC, keywordGHI));
        assertThat(oldJudgment.getKeywords(), Matchers.containsInAnyOrder(keywordABC, keywordGHI));
    }
    
}
