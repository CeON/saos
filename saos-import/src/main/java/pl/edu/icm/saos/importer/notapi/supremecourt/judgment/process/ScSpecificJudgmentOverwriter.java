package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scSpecificJudgmentOverwriter")
public class ScSpecificJudgmentOverwriter implements JudgmentOverwriter<SupremeCourtJudgment> {

    
    
    
    //------------------------ LOGIC --------------------------
    
    
    @Override
    public void overwriteJudgment(SupremeCourtJudgment oldJudgment, SupremeCourtJudgment newJudgment, ImportCorrectionList correctionList) {
        
        overwriteScChambers(oldJudgment, newJudgment);
        
        overwriteScChamberDivision(oldJudgment, newJudgment);
        
        overwritePersonnelType(oldJudgment, newJudgment);
        
        overwriteScJudgmentForm(oldJudgment, newJudgment);
    }


    
  
    //------------------------ PRIVATE --------------------------
    
    
    private void overwriteScJudgmentForm(SupremeCourtJudgment oldJudgment, SupremeCourtJudgment newJudgment) {
        
        oldJudgment.setScJudgmentForm(newJudgment.getScJudgmentForm());
        
    }
    

    private void overwritePersonnelType(SupremeCourtJudgment oldJudgment, SupremeCourtJudgment newJudgment) {
        
        oldJudgment.setPersonnelType(newJudgment.getPersonnelType());
        
    }
    
    
    private void overwriteScChamberDivision(SupremeCourtJudgment oldJudgment, SupremeCourtJudgment newJudgment) {
    
        oldJudgment.setScChamberDivision(newJudgment.getScChamberDivision());
    
    }
    
    
    private void overwriteScChambers(SupremeCourtJudgment oldJudgment, SupremeCourtJudgment newJudgment) {
        
        oldJudgment.getScChambers().stream().filter(scChamber->!newJudgment.containsScChamber(scChamber)).forEach(scChamber->oldJudgment.removeScChamber(scChamber));
        
        newJudgment.getScChambers().stream().filter(scChamber->!oldJudgment.containsScChamber(scChamber)).forEach(scChamber->oldJudgment.addScChamber(scChamber));
      
    }


   
}
