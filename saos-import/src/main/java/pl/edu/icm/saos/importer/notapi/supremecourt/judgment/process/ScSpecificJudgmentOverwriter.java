package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.JudgmentOverwriter;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scSpecificJudgmentOverwriter")
public class ScSpecificJudgmentOverwriter implements JudgmentOverwriter<SupremeCourtJudgment> {

    
    
    
    //------------------------ LOGIC --------------------------
    
    
    @Override
    public void overwriteJudgment(SupremeCourtJudgment oldJudgment, SupremeCourtJudgment newJudgment) {
        
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
        
        oldJudgment.getScChambers().stream().forEach(scChamber-> removeChamberFromOld(oldJudgment, newJudgment, scChamber));
        
        newJudgment.getScChambers().stream().forEach(scChamber->addChamberToOld(oldJudgment, scChamber));
      
    }

    
    private void addChamberToOld(SupremeCourtJudgment oldJudgment, SupremeCourtChamber scChamber) {
        
        if (!oldJudgment.containsScChamber(scChamber)) {
        
            oldJudgment.addScChamber(scChamber);
        
        }
        
    }


    private void removeChamberFromOld(SupremeCourtJudgment oldJudgment, SupremeCourtJudgment newJudgment, SupremeCourtChamber scChamber) {
        
        if (!newJudgment.containsScChamber(scChamber)) {
    
            oldJudgment.removeScChamber(scChamber);
        
        }
        
    }

  
}
