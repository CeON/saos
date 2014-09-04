package pl.edu.icm.saos.persistence.common;

import pl.edu.icm.saos.common.visitor.Visitor;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;

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
        JpaUtils.initialize(judgment.getKeywords());
    }

    
    public void visit(Judge judge) {
        JpaUtils.initialize(judge.getSpecialRoles());
    }


    public void visit(CommonCourt court){
        JpaUtils.initialize(court.getDivisions());
    }
    
    
    
    //------------------------ PRIVATE --------------------------
    
    
    private void initializeJudgment(Judgment judgment) {
        JpaUtils.initialize(judgment.getCourtReporters());
        JpaUtils.initialize(judgment.getLegalBases());
        
    }
    
    
}
