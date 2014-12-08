package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;


/**
 * @author Łukasz Dumiszewski
 */

public class CcSpecificJudgmentOverwriterTest {

    
    private CcSpecificJudgmentOverwriter ccJudgmentOverwriter = new CcSpecificJudgmentOverwriter();
    
    
    private CommonCourtJudgment oldJudgment = new CommonCourtJudgment(); 
    
    private CommonCourtJudgment newJudgment = new CommonCourtJudgment();
    
    private ImportCorrectionList correctionList = Mockito.mock(ImportCorrectionList.class);
    
    
    @Test
    public void overwriteJudgment_courtData_division() {
        
        CommonCourtDivision division = new CommonCourtDivision();
        division.setCode("1234");
        CommonCourt court = new CommonCourt();
        division.setCourt(court);
        oldJudgment.setCourtDivision(division);
        
        
        CommonCourtDivision newDivision = new CommonCourtDivision();
        newDivision.setCode("1234");
        CommonCourt newCourt = new CommonCourt();
        newDivision.setCourt(newCourt);
        newJudgment.setCourtDivision(newDivision);
        
        ccJudgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        
        
        assertEquals(newDivision, oldJudgment.getCourtDivision());
        assertEquals(newDivision, newJudgment.getCourtDivision());
        
    }
    
    
    @Test
    public void overwriteJudgment_Keywords() {
        
        JudgmentKeyword keywordABC = new JudgmentKeyword(CourtType.COMMON, "ABC");
        JudgmentKeyword keywordDEF = new JudgmentKeyword(CourtType.COMMON, "DEF");
        JudgmentKeyword keywordGHI = new JudgmentKeyword(CourtType.COMMON, "GHI");
        
        
        oldJudgment.addKeyword(keywordABC);
        oldJudgment.addKeyword(keywordDEF);
        
        newJudgment.addKeyword(keywordABC);
        newJudgment.addKeyword(keywordGHI);
        
        
        ccJudgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        
        assertThat(newJudgment.getKeywords(), Matchers.containsInAnyOrder(keywordABC, keywordGHI));
        assertThat(oldJudgment.getKeywords(), Matchers.containsInAnyOrder(keywordABC, keywordGHI));
    }
    
}
