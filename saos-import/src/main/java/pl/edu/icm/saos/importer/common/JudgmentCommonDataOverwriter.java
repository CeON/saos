package pl.edu.icm.saos.importer.common;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentSource;
import pl.edu.icm.saos.persistence.model.ReferencedRegulation;

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
        
        
        oldJudgment.setCaseNumber(newJudgment.getCaseNumber());
        oldJudgment.setJudgmentDate(newJudgment.getJudgmentDate());
        oldJudgment.setCourtReporters(Lists.newArrayList(newJudgment.getCourtReporters()));
        oldJudgment.setDecision(newJudgment.getDecision());
        oldJudgment.setJudgmentType(newJudgment.getJudgmentType());
        oldJudgment.setLegalBases(Lists.newArrayList(newJudgment.getLegalBases()));
        oldJudgment.setSummary(newJudgment.getSummary());
        oldJudgment.setTextContent(newJudgment.getTextContent());
        
        overwriteJudgmentSource(oldJudgment, newJudgment);
        
        OverwriterUtils.overwriteReasoning(oldJudgment, newJudgment);
        
        overwriteJudges(oldJudgment, newJudgment);
        
        overwriteReferencedRegulations(oldJudgment, newJudgment);
        
        
    }
    
    

    //------------------------ PRIVATE --------------------------

    
    private void overwriteReferencedRegulations(Judgment oldJudgment, Judgment newJudgment) {
        oldJudgment.removeAllReferencedRegulations();
        for (ReferencedRegulation regulation : newJudgment.getReferencedRegulations()) {
            ReferencedRegulation nRegulation = new ReferencedRegulation();
            nRegulation.setRawText(regulation.getRawText());
            nRegulation.setLawJournalEntry(regulation.getLawJournalEntry());
            oldJudgment.addReferencedRegulation(nRegulation);
        }
    }


    private void overwriteJudges(Judgment oldJudgment, Judgment newJudgment) {
        oldJudgment.removeAllJudges();
        for (Judge judge : newJudgment.getJudges()) {
            Judge nJudge = new Judge();
            nJudge.setName(judge.getName());
            nJudge.setSpecialRoles(Lists.newArrayList(judge.getSpecialRoles()));
            oldJudgment.addJudge(nJudge);
        }
    }
    
    

    
    private void overwriteJudgmentSource(Judgment oldJudgment, Judgment newJudgment) {
        JudgmentSource oldJudgmentSource = oldJudgment.getJudgmentSource();
        JudgmentSource newJudgmentSource = newJudgment.getJudgmentSource();
        oldJudgmentSource.setSourceJudgmentId(newJudgmentSource.getSourceJudgmentId());
        oldJudgmentSource.setPublicationDate(newJudgmentSource.getPublicationDate());
        oldJudgmentSource.setSourceJudgmentUrl(newJudgmentSource.getSourceJudgmentUrl());
        oldJudgmentSource.setSourceType(newJudgmentSource.getSourceType());
        oldJudgmentSource.setPublisher(newJudgmentSource.getPublisher());
        oldJudgmentSource.setReviser(newJudgmentSource.getReviser());
    }
}
