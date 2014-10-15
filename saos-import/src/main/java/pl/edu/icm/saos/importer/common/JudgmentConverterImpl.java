package pl.edu.icm.saos.importer.common;

import java.util.List;

import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;

/**
 * An abstract judgment converter. <br/>
 * Implementations of this class can focus on converting specific simple
 * properties without paying attention on setting them in Judgment.
 * 
 * @author Łukasz Dumiszewski
 */

public class JudgmentConverterImpl<JUDGMENT extends Judgment, SOURCE_JUDGMENT> implements JudgmentConverter<JUDGMENT, SOURCE_JUDGMENT> {

    
    private JudgmentDataExtractor<JUDGMENT, SOURCE_JUDGMENT> judgmentDataExtractor;
    
    
    
    /**
     * A template method that converts sourceJudgment into JUDGMENT
     */
    public final JUDGMENT convertJudgment(SOURCE_JUDGMENT sourceJudgment) {

        JUDGMENT judgment = judgmentDataExtractor.createNewJudgment();
       
        convertCourtCases(judgment, sourceJudgment);
        
        judgment.setJudgmentDate(judgmentDataExtractor.extractJudgmentDate(sourceJudgment));
        
        convertCourtReporters(judgment, sourceJudgment);
        
        judgment.setDecision(judgmentDataExtractor.extractDecision(sourceJudgment));
        
        judgment.setSummary(judgmentDataExtractor.extractSummary(sourceJudgment));
        
        convertLegalBases(judgment, sourceJudgment);
        
        judgment.setTextContent(judgmentDataExtractor.extractTextContent(sourceJudgment));
        
        judgment.setSourceInfo(extractSourceInfo(sourceJudgment));
        
        judgment.setJudgmentType(judgmentDataExtractor.extractJudgmentType(sourceJudgment));
        
        convertReferencedRegulations(judgment, sourceJudgment);
        
        convertJudges(judgment, sourceJudgment);
        
                
        judgmentDataExtractor.convertSpecific(judgment, sourceJudgment);
        
        
        return judgment;
    }

 

    
    //------------------------ PRIVATE --------------------------
    
        
    private JudgmentSourceInfo extractSourceInfo(SOURCE_JUDGMENT sourceJudgment) {
        JudgmentSourceInfo judgmentSource = new JudgmentSourceInfo();
        judgmentSource.setSourceJudgmentId(judgmentDataExtractor.extractSourceJudgmentId(sourceJudgment));
        judgmentSource.setSourceJudgmentUrl(judgmentDataExtractor.extractSourceJudgmentUrl(sourceJudgment));
        judgmentSource.setPublicationDate(judgmentDataExtractor.extractPublicationDate(sourceJudgment));
        judgmentSource.setSourceCode(judgmentDataExtractor.getSourceCode());
        judgmentSource.setReviser(judgmentDataExtractor.extractReviser(sourceJudgment));
        judgmentSource.setPublisher(judgmentDataExtractor.extractPublisher(sourceJudgment));
        return judgmentSource;
    }

    
    private void convertCourtCases(JUDGMENT judgment, SOURCE_JUDGMENT sourceJudgment) {
        List<CourtCase> courtCases = judgmentDataExtractor.extractCourtCases(sourceJudgment);
        for (CourtCase courtCase : courtCases) {
            if(!judgment.containsCourtCase(courtCase.getCaseNumber())) {
                judgment.addCourtCase(courtCase);
            }
        }
    }
    
    private void convertJudges(JUDGMENT judgment, SOURCE_JUDGMENT sourceJudgment) {
        List<Judge> judges = judgmentDataExtractor.extractJudges(sourceJudgment);
        for (Judge judge : judges) {
            if (!judgment.containsJudge(judge.getName())) {
                judgment.addJudge(judge);
            }
        }
    }


    private void convertReferencedRegulations(JUDGMENT judgment,
            SOURCE_JUDGMENT sourceJudgment) {
        List<JudgmentReferencedRegulation> regulations = judgmentDataExtractor.extractReferencedRegulations(sourceJudgment);
        for (JudgmentReferencedRegulation regulation : regulations) {
            if (!judgment.containsReferencedRegulation(regulation)) {
                judgment.addReferencedRegulation(regulation);
            }
        }
    }


    private void convertLegalBases(JUDGMENT judgment,
            SOURCE_JUDGMENT sourceJudgment) {
        List<String> legalBases = judgmentDataExtractor.extractLegalBases(sourceJudgment);
        for (String legalBase : legalBases) {
            if (!judgment.containsLegalBase(legalBase)) {
                judgment.addLegalBase(legalBase);
            }
        }
    }


    private void convertCourtReporters(JUDGMENT judgment,
            SOURCE_JUDGMENT sourceJudgment) {
        List<String> courtReporters = judgmentDataExtractor.extractCourtReporters(sourceJudgment);
        for (String courtReporter : courtReporters) {
            if (!judgment.containsCourtReporter(courtReporter)) {
                judgment.addCourtReporter(courtReporter);
            }
        }
    }




    //------------------------ SETTERS --------------------------

    public void setJudgmentDataExtractor(JudgmentDataExtractor<JUDGMENT, SOURCE_JUDGMENT> judgmentDataExtractor) {
        this.judgmentDataExtractor = judgmentDataExtractor;
    }

    
    
    
    
}
