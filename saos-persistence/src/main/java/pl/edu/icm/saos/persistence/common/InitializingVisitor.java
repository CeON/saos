package pl.edu.icm.saos.persistence.common;

import pl.edu.icm.saos.common.visitor.Visitor;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * Contains methods initializing lazy objects
 * 
 * @author Łukasz Dumiszewski
 */

public class InitializingVisitor implements Visitor {

    
    public void visit(DataObject dataObject) {
        JpaUtils.initialize(dataObject);
        
    }
    
    public void visit(CommonCourtJudgment judgment) {
        initializeJudgment(judgment);
        JpaUtils.initialize(judgment.getCourtDivision());
        JpaUtils.initialize(judgment.getCourtDivision().getCourt());
    }

    
    public void visit(SupremeCourtJudgment judgment) {
        initializeJudgment(judgment);
        JpaUtils.initialize(judgment.getScChamberDivision());
        JpaUtils.initialize(judgment.getScChamberDivision().getScChamber());
        JpaUtils.initialize(judgment.getScChambers());
        JpaUtils.initialize(judgment.getScJudgmentForm());
    }
    
    
    public void visit(Judge judge) {
        JpaUtils.initialize(judge.getSpecialRoles());
    }


    
    //------------------------ PRIVATE --------------------------
    
    
    private void initializeJudgment(Judgment judgment) {
        JpaUtils.initialize(judgment.getCourtReporters());
        JpaUtils.initialize(judgment.getLegalBases());
        JpaUtils.initialize(judgment.getKeywords());
        
    }
    
    
}
