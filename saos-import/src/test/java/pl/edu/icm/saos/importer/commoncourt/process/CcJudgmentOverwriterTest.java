package pl.edu.icm.saos.importer.commoncourt.process;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;


/**
 * @author Łukasz Dumiszewski
 */

public class CcJudgmentOverwriterTest {

    
    private CcJudgmentOverwriter ccJudgmentOverwriter = new CcJudgmentOverwriter();
    
    
    @Test
    public void overwriteSpecificData_courtData_court() {
        CommonCourtJudgment oldJudgment = new CommonCourtJudgment();
        CommonCourt oldCourt = new CommonCourt();
        oldCourt.setAppealCourtCode("12");
        oldCourt.setRegionalCourtCode("13");
        oldCourt.setDistrictCourtCode("14");
        oldCourt.setName("Sąd w Gnieźnie");
        oldCourt.setShortName("SG");
        oldJudgment.getCourtData().setCourt(oldCourt);
        
        
        
        CommonCourtJudgment newJudgment = new CommonCourtJudgment();
        CommonCourt newCourt = new CommonCourt();
        
        String newAppealCourtCode = oldCourt.getAppealCourtCode() + "1";
        newCourt.setAppealCourtCode(newAppealCourtCode);
        
        String newRegionalCourtCode = oldCourt.getRegionalCourtCode() + "1";
        newCourt.setRegionalCourtCode(newRegionalCourtCode);
        
        String newDistrictCourtCode = oldCourt.getDistrictCourtCode() + "1";
        newCourt.setDistrictCourtCode(newDistrictCourtCode);
        
        String newName = oldCourt.getName() + "new";
        newCourt.setName(newName);
        
        String newShortName = oldCourt.getShortName() + "new";
        newCourt.setShortName(newShortName);
        
        newJudgment.getCourtData().setCourt(newCourt);
        
        
        
        ccJudgmentOverwriter.overwriteSpecificData(oldJudgment, newJudgment);
        
        
        
        assertEquals(newAppealCourtCode, oldCourt.getAppealCourtCode());
        assertEquals(newRegionalCourtCode, oldCourt.getRegionalCourtCode());
        assertEquals(newDistrictCourtCode, oldCourt.getDistrictCourtCode());
        assertEquals(newName, oldCourt.getName());
        assertEquals(newShortName, oldCourt.getShortName());
        
        assertEquals(newAppealCourtCode, newCourt.getAppealCourtCode());
        assertEquals(newRegionalCourtCode, newCourt.getRegionalCourtCode());
        assertEquals(newDistrictCourtCode, newCourt.getDistrictCourtCode());
        assertEquals(newName, newCourt.getName());
        assertEquals(newShortName, newCourt.getShortName());
        
    }
    
    
    
    @Test
    public void overwriteSpecificData_courtData_division() {
        CommonCourtJudgment oldJudgment = new CommonCourtJudgment();
        oldJudgment.getCourtData().setDivisionNumber(1);
        
        CommonCourtDivisionType oldDivisionType = new CommonCourtDivisionType();
        oldDivisionType.setCode("1111");
        oldDivisionType.setName("Cywilny");
        oldJudgment.getCourtData().setDivisionType(oldDivisionType);
        
        
        
        CommonCourtJudgment newJudgment = new CommonCourtJudgment();
        
        int newDivisionNumber = oldJudgment.getCourtData().getDivisionNumber();
        newJudgment.getCourtData().setDivisionNumber(1);
        
        CommonCourtDivisionType newDivisionType = new CommonCourtDivisionType();
        newDivisionType.setCode("222");
        newDivisionType.setName("Karny");
        newJudgment.getCourtData().setDivisionType(newDivisionType);
        
        
        
        ccJudgmentOverwriter.overwriteSpecificData(oldJudgment, newJudgment);
        
        
        
        assertEquals(newDivisionNumber, oldJudgment.getCourtData().getDivisionNumber());
        assertEquals(newDivisionType, oldJudgment.getCourtData().getDivisionType());
        
        assertEquals(newDivisionNumber, newJudgment.getCourtData().getDivisionNumber());
        assertEquals(newDivisionType, newJudgment.getCourtData().getDivisionType());
        
    }
    
}
