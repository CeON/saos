package pl.edu.icm.saos.persistence.common;

import pl.edu.icm.saos.common.visitor.Visitor;
import pl.edu.icm.saos.persistence.model.*;

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
        if (judgment.getCourtDivision() != null) {
            JpaUtils.initialize(judgment.getCourtDivision());
            JpaUtils.initialize(judgment.getCourtDivision().getCourt());
        }
    }

    
    public void visit(SupremeCourtJudgment judgment) {
        initializeJudgment(judgment);
        if (judgment.getScChamberDivision() != null) {
            JpaUtils.initialize(judgment.getScChamberDivision());
            JpaUtils.initialize(judgment.getScChamberDivision().getScChamber());
        }
        JpaUtils.initialize(judgment.getScChambers());
        JpaUtils.initialize(judgment.getScJudgmentForm());
    }

    public void visit(ConstitutionalTribunalJudgment judgment){
        initializeJudgment(judgment);
        JpaUtils.initialize(judgment.getDissentingOpinions());
        if(judgment.getDissentingOpinions() != null){
            judgment.getDissentingOpinions().forEach(o -> JpaUtils.initialize(o.getAuthors()));
        }
    }
    
    public void visit(NationalAppealChamberJudgment judgment) {
        initializeJudgment(judgment);
    }
    
    
    public void visit(Judge judge) {
        JpaUtils.initialize(judge.getSpecialRoles());
    }


    
    //------------------------ PRIVATE --------------------------
    
    
    private void initializeJudgment(Judgment judgment) {
        JpaUtils.initialize(judgment.getCourtReporters());
        JpaUtils.initialize(judgment.getLegalBases());
        JpaUtils.initialize(judgment.getKeywords());
        JpaUtils.initialize(judgment.getLowerCourtJudgments());
        
    }
    
    
}
