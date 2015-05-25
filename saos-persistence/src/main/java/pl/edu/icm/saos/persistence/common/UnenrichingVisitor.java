package pl.edu.icm.saos.persistence.common;

import java.util.List;
import java.util.stream.Collectors;

import pl.edu.icm.saos.common.visitor.Visitor;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.ReferencedCourtCase;

/**
 * Visitor removing generated objects
 * @author madryk
 */
public class UnenrichingVisitor implements Visitor {

    
    //------------------------ LOGIC --------------------------
    
    public void visit(DataObject dataObject) {
        // do nothing
    }
    
    public void visit(Judgment judgment) {
        
        removeGeneratedRefRegulations(judgment);
        
        removeGeneratedRefCourtCases(judgment);
        
        judgment.setMaxMoneyAmount(null);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void removeGeneratedRefRegulations(Judgment judgment) {
        
        for (JudgmentReferencedRegulation generatedRefRegulation : filterGenerated(judgment.getReferencedRegulations())) {
            judgment.removeReferencedRegulation(generatedRefRegulation);
        }
    }
    
    private void removeGeneratedRefCourtCases(Judgment judgment) {
        
        for (ReferencedCourtCase generatedRefCourtCase : filterGenerated(judgment.getReferencedCourtCases())) {
            judgment.removeReferencedCourtCase(generatedRefCourtCase);
        }
    }
    
    private <T extends Generatable> List<T> filterGenerated(List<T> generatableObjects) {
        return generatableObjects.stream().filter(o -> o.isGenerated()).collect(Collectors.toList());
    }
}
