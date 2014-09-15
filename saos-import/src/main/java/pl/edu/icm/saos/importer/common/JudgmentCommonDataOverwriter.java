package pl.edu.icm.saos.importer.common;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Service("judgmentCommonDataOverwriter")
public class JudgmentCommonDataOverwriter implements JudgmentOverwriter<Judgment> {

    
    public final void overwriteJudgment(Judgment oldJudgment, Judgment newJudgment) {
        Preconditions.checkNotNull(oldJudgment);
        Preconditions.checkNotNull(newJudgment);
        
        
        overwriteCourtCases(oldJudgment, newJudgment);
        oldJudgment.setJudgmentDate(newJudgment.getJudgmentDate());
        overwriteCourtReporters(oldJudgment, newJudgment);
        oldJudgment.setDecision(newJudgment.getDecision());
        oldJudgment.setJudgmentType(newJudgment.getJudgmentType());
        overwriteLegalBases(oldJudgment, newJudgment);
        oldJudgment.setSummary(newJudgment.getSummary());
        oldJudgment.setTextContent(newJudgment.getTextContent());
        
        OverwriterUtils.overwriteSourceInfo(oldJudgment.getSourceInfo(), newJudgment.getSourceInfo());
        
        OverwriterUtils.overwriteReasoning(oldJudgment, newJudgment);
        
        overwriteJudges(oldJudgment, newJudgment);
        
        overwriteReferencedRegulations(oldJudgment, newJudgment);
        
        
    }



    

    //------------------------ PRIVATE --------------------------

    
    private void overwriteCourtReporters(Judgment oldJudgment, Judgment newJudgment) {
        
        for (String oldCourtReporter : oldJudgment.getCourtReporters()) {
            if (!newJudgment.containsCourtReporter(oldCourtReporter)) {
                oldJudgment.removeCourtReporter(oldCourtReporter);
            }
        }
        for (String courtReporter : newJudgment.getCourtReporters()) {
            if (!oldJudgment.containsCourtReporter(courtReporter)) {
                oldJudgment.addCourtReporter(courtReporter);
            }
        }
    }
    
    private void overwriteLegalBases(Judgment oldJudgment, Judgment newJudgment) {
        
        for (String oldLegalBase : oldJudgment.getLegalBases()) {
            if (!newJudgment.containsLegalBase(oldLegalBase)) {
                oldJudgment.removeLegalBase(oldLegalBase);
            }
        }
        for (String legalBase : newJudgment.getLegalBases()) {
            if (!oldJudgment.containsLegalBase(legalBase)) {
                oldJudgment.addLegalBase(legalBase);
            }
        }
    }
    
    private void overwriteReferencedRegulations(Judgment oldJudgment, Judgment newJudgment) {
        
        for (JudgmentReferencedRegulation oldRegulation : oldJudgment.getReferencedRegulations()) {
            if (!newJudgment.containsReferencedRegulation(oldRegulation)) {
                oldJudgment.removeReferencedRegulation(oldRegulation);
            }
        }
        
        for (JudgmentReferencedRegulation regulation : newJudgment.getReferencedRegulations()) {
            if (oldJudgment.containsReferencedRegulation(regulation)) {
                continue;
            }
            JudgmentReferencedRegulation nRegulation = new JudgmentReferencedRegulation();
            nRegulation.setRawText(regulation.getRawText());
            nRegulation.setLawJournalEntry(regulation.getLawJournalEntry());
            oldJudgment.addReferencedRegulation(nRegulation);
        }
    }
    


    private void overwriteJudges(Judgment oldJudgment, Judgment newJudgment) {
        for (Judge oldJudge : oldJudgment.getJudges()) {
            if (!newJudgment.containsJudge(oldJudge.getName())) {
                oldJudgment.removeJudge(oldJudge);
            }
        }
        for (Judge judge : newJudgment.getJudges()) {
            Judge nJudge = new Judge();
            nJudge.setName(judge.getName());
            nJudge.setSpecialRoles(Lists.newArrayList(judge.getSpecialRoles()));
            if (!oldJudgment.containsJudge(nJudge.getName())) {
                oldJudgment.addJudge(nJudge);    
            } else {
                oldJudgment.getJudge(nJudge.getName()).setSpecialRoles(nJudge.getSpecialRoles());
            }
            
        }
    }
    
    
    private void overwriteCourtCases(Judgment oldJudgment, Judgment newJudgment) {
        for (CourtCase courtCase : oldJudgment.getCourtCases()) {
            if (!newJudgment.containsCourtCase(courtCase.getCaseNumber())) {
                oldJudgment.removeCourtCase(courtCase);
            }
        }
        for (CourtCase courtCase : newJudgment.getCourtCases()) {
            CourtCase newCourtCase = new CourtCase(courtCase.getCaseNumber());
            if (!oldJudgment.containsCourtCase(newCourtCase.getCaseNumber())) {
                oldJudgment.addCourtCase(newCourtCase); 
            } 
        }
    }
       
   
}
