package pl.edu.icm.saos.importer.common.overwriter;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent;

import com.google.common.base.Preconditions;

/**
 * Implementation of {@link JudgmentOverwriter} that overwrites all judgment data accessible from {@link Judgment}
 * 
 * @author Łukasz Dumiszewski
 */
@Service("commonJudgmentOverwriter")
public class CommonJudgmentOverwriter implements JudgmentOverwriter<Judgment> {

    
    @Override
    public final void overwriteJudgment(Judgment oldJudgment, Judgment newJudgment, ImportCorrectionList correctionList) {
        
        Preconditions.checkNotNull(oldJudgment);
        Preconditions.checkNotNull(newJudgment);
        
        overwriteCourtCases(oldJudgment, newJudgment);
        oldJudgment.setJudgmentDate(newJudgment.getJudgmentDate());
        overwriteCourtReporters(oldJudgment, newJudgment);
        oldJudgment.setDecision(newJudgment.getDecision());
        oldJudgment.setJudgmentType(newJudgment.getJudgmentType());
        overwriteLegalBases(oldJudgment, newJudgment);
        oldJudgment.setSummary(newJudgment.getSummary());
        
        overwriteJudgmentTextContent(oldJudgment.getTextContent(), newJudgment.getTextContent());
        
        overwriteSourceInfo(oldJudgment.getSourceInfo(), newJudgment.getSourceInfo());
        
        overwriteJudges(oldJudgment, newJudgment, correctionList);
        
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
    


    private void overwriteJudges(Judgment oldJudgment, Judgment newJudgment, ImportCorrectionList correctionList) {
        for (Judge oldJudge : oldJudgment.getJudges()) {
            if (!newJudgment.containsJudge(oldJudge.getName())) {
                oldJudgment.removeJudge(oldJudge);
            }
        }
        for (Judge nJudge : newJudgment.getJudges()) {
            if (!oldJudgment.containsJudge(nJudge.getName())) {
                oldJudgment.addJudge(nJudge);    
            } else {
                Judge oldJudge = oldJudgment.getJudge(nJudge.getName());
                oldJudge.setSpecialRoles(nJudge.getSpecialRoles());
                correctionList.changeCorrectedObject(nJudge, oldJudge);
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
       
    
    private void overwriteSourceInfo(JudgmentSourceInfo oldJudgmentSource, JudgmentSourceInfo newJudgmentSource) {
        oldJudgmentSource.setSourceJudgmentId(newJudgmentSource.getSourceJudgmentId());
        oldJudgmentSource.setPublicationDate(newJudgmentSource.getPublicationDate());
        oldJudgmentSource.setSourceJudgmentUrl(newJudgmentSource.getSourceJudgmentUrl());
        oldJudgmentSource.setSourceCode(newJudgmentSource.getSourceCode());
        oldJudgmentSource.setPublisher(newJudgmentSource.getPublisher());
        oldJudgmentSource.setReviser(newJudgmentSource.getReviser());
    }
    
    
    private void overwriteJudgmentTextContent(JudgmentTextContent oldTextContent, JudgmentTextContent newTextContent) {
        oldTextContent.setRawTextContent(newTextContent.getRawTextContent());
        oldTextContent.setType(newTextContent.getType());
        oldTextContent.setPath(newTextContent.getPath());
    }
   
}
