package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Matchers;
import org.junit.Test;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

/**
 * @author Łukasz Dumiszewski
 */

public class ScSpecificJudgmentOverwriterTest {

    
    private ScSpecificJudgmentOverwriter scSpecificJudgmentOverwriter = new ScSpecificJudgmentOverwriter();
    
    private SupremeCourtJudgment oldJudgment = new SupremeCourtJudgment();
    
    private SupremeCourtJudgment newJudgment = new SupremeCourtJudgment();
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    
    
    
    
    @Test
    public void overrideJudgment_scChamberDivision() {
        
        // given
        
        SupremeCourtChamberDivision oldScChamberDivision = new SupremeCourtChamberDivision();
        oldJudgment.setScChamberDivision(oldScChamberDivision);
        
        SupremeCourtChamberDivision newScChamberDivision = new SupremeCourtChamberDivision();
        newJudgment.setScChamberDivision(newScChamberDivision);
        
        
        // execute
        
        scSpecificJudgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);

        
        // assert
        
        assertTrue(newScChamberDivision == newJudgment.getScChamberDivision());
        assertTrue(newScChamberDivision == oldJudgment.getScChamberDivision());
        
    }
    
    
    
    @Test
    public void overrideJudgment_personnelType() {
        
        //given 
        
        oldJudgment.setPersonnelType(PersonnelType.SEVEN_PERSON);
        
        newJudgment.setPersonnelType(PersonnelType.ONE_PERSON);
        
        
        // execute 
        
        scSpecificJudgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);

        
        // assert
        
        assertEquals(PersonnelType.ONE_PERSON, newJudgment.getPersonnelType());
        assertEquals(PersonnelType.ONE_PERSON, oldJudgment.getPersonnelType());
        
    }
    
    
    
    @Test
    public void overrideJudgment_scJudgmentForm() {
        
        // given 
        
        SupremeCourtJudgmentForm oldScJudgmentForm = new SupremeCourtJudgmentForm();
        oldScJudgmentForm.setName("ABC");
        oldJudgment.setScJudgmentForm(oldScJudgmentForm);
        
        SupremeCourtJudgmentForm newScJudgmentForm = new SupremeCourtJudgmentForm();
        oldScJudgmentForm.setName("DEF");
        newJudgment.setScJudgmentForm(newScJudgmentForm);
        
        
        //execute 
        
        scSpecificJudgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);

        
        // assert
        
        assertTrue(newScJudgmentForm == newJudgment.getScJudgmentForm());
        assertTrue(newScJudgmentForm == oldJudgment.getScJudgmentForm());
        
    }

    
    
    @Test
    public void overrideJudgment_scChambers() {
        
        // given
        
        SupremeCourtChamber oldScChamber1 = new SupremeCourtChamber();
        oldScChamber1.setName("ABC");
        
        SupremeCourtChamber oldScChamber2 = new SupremeCourtChamber();
        oldScChamber2.setName("DEF");
        
        oldJudgment.addScChamber(oldScChamber1);
        oldJudgment.addScChamber(oldScChamber2);
        
        
        SupremeCourtChamber newScChamber1 = new SupremeCourtChamber();
        newScChamber1.setName("ABC");
        
        SupremeCourtChamber newScChamber2 = new SupremeCourtChamber();
        newScChamber2.setName("EFG");
        
        
        newJudgment.addScChamber(newScChamber1);
        newJudgment.addScChamber(newScChamber2);
        
        
        // execute
        
        scSpecificJudgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        
        // assert
        
        assertThat(newJudgment.getScChambers(), Matchers.containsInAnyOrder(newScChamber1, newScChamber2));
        assertThat(oldJudgment.getScChambers(), Matchers.containsInAnyOrder(oldScChamber1, newScChamber2));
        
    }
    
    
}
