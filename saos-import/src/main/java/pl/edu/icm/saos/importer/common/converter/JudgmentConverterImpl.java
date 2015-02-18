package pl.edu.icm.saos.importer.common.converter;

import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createDelete;

import java.util.List;

import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.common.correction.ImportCorrection;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
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
    public final JudgmentWithCorrectionList<JUDGMENT> convertJudgment(SOURCE_JUDGMENT sourceJudgment) {

        JUDGMENT judgment = judgmentDataExtractor.createNewJudgment();
        
        ImportCorrectionList correctionList = new ImportCorrectionList();
        
        convertCourtCases(judgment, sourceJudgment, correctionList);
        
        judgment.setJudgmentDate(judgmentDataExtractor.extractJudgmentDate(sourceJudgment, correctionList));
        
        convertCourtReporters(judgment, sourceJudgment, correctionList);
        
        judgment.setDecision(judgmentDataExtractor.extractDecision(sourceJudgment, correctionList));
        
        judgment.setSummary(judgmentDataExtractor.extractSummary(sourceJudgment, correctionList));
        
        convertLegalBases(judgment, sourceJudgment, correctionList);
        
        judgment.setTextContent(judgmentDataExtractor.extractTextContent(sourceJudgment, correctionList));
        
        judgment.setSourceInfo(extractSourceInfo(sourceJudgment, correctionList));
        
        judgment.setJudgmentType(judgmentDataExtractor.extractJudgmentType(sourceJudgment, correctionList));
        
        convertReferencedRegulations(judgment, sourceJudgment, correctionList);
        
        convertJudges(judgment, sourceJudgment, correctionList);
        
        judgment.setReceiptDate(judgmentDataExtractor.extractReceiptDate(sourceJudgment, correctionList));
        
        convertLowerCourtJudgments(judgment, sourceJudgment, correctionList);
        
        judgment.setMeansOfAppeal(judgmentDataExtractor.extractMeansOfAppeal(sourceJudgment, correctionList));
        
        judgment.setJudgmentResult(judgmentDataExtractor.extractJudgmentResult(sourceJudgment, correctionList));
        
                
        judgmentDataExtractor.convertSpecific(judgment, sourceJudgment, correctionList);
        
        JudgmentWithCorrectionList<JUDGMENT> judgmentWithCorrectionList = new JudgmentWithCorrectionList<>(judgment, correctionList);
        
        return judgmentWithCorrectionList;
    }

 

    
    //------------------------ PRIVATE --------------------------
    
        
    private JudgmentSourceInfo extractSourceInfo(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        JudgmentSourceInfo judgmentSource = new JudgmentSourceInfo();
        judgmentSource.setSourceJudgmentId(judgmentDataExtractor.extractSourceJudgmentId(sourceJudgment, correctionList));
        judgmentSource.setSourceJudgmentUrl(judgmentDataExtractor.extractSourceJudgmentUrl(sourceJudgment, correctionList));
        judgmentSource.setPublicationDate(judgmentDataExtractor.extractPublicationDate(sourceJudgment, correctionList));
        judgmentSource.setSourceCode(judgmentDataExtractor.getSourceCode());
        judgmentSource.setReviser(judgmentDataExtractor.extractReviser(sourceJudgment, correctionList));
        judgmentSource.setPublisher(judgmentDataExtractor.extractPublisher(sourceJudgment, correctionList));
        return judgmentSource;
    }

    
    private void convertCourtCases(JUDGMENT judgment, SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        List<CourtCase> courtCases = judgmentDataExtractor.extractCourtCases(sourceJudgment, correctionList);
        for (CourtCase courtCase : courtCases) {
            if(!judgment.containsCourtCase(courtCase.getCaseNumber())) {
                judgment.addCourtCase(courtCase);
            }
        }
    }
    
    private void convertJudges(JUDGMENT judgment, SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        List<Judge> judges = judgmentDataExtractor.extractJudges(sourceJudgment, correctionList);
        for (Judge judge : judges) {
            if (!judgment.containsJudge(judge.getName())) {
                judgment.addJudge(judge);
            } else {
                ImportCorrection importCorrection = createDelete(Judge.class).oldValue(judge.getName()).newValue(null).build();
                correctionList.addCorrection(importCorrection);
            }
        }
    }


    private void convertReferencedRegulations(JUDGMENT judgment, SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        List<JudgmentReferencedRegulation> regulations = judgmentDataExtractor.extractReferencedRegulations(sourceJudgment, correctionList);
        for (JudgmentReferencedRegulation regulation : regulations) {
            if (!judgment.containsReferencedRegulation(regulation)) {
                judgment.addReferencedRegulation(regulation);
            }
        }
    }


    private void convertLegalBases(JUDGMENT judgment, SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        List<String> legalBases = judgmentDataExtractor.extractLegalBases(sourceJudgment, correctionList);
        for (String legalBase : legalBases) {
            if (!judgment.containsLegalBase(legalBase)) {
                judgment.addLegalBase(legalBase);
            }
        }
    }


    private void convertCourtReporters(JUDGMENT judgment, SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        List<String> courtReporters = judgmentDataExtractor.extractCourtReporters(sourceJudgment, correctionList);
        for (String courtReporter : courtReporters) {
            if (!judgment.containsCourtReporter(courtReporter)) {
                judgment.addCourtReporter(courtReporter);
            }
        }
    }
    
    
    private void convertLowerCourtJudgments(JUDGMENT judgment, SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        List<String> lowerCourtJudgments = judgmentDataExtractor.extractLowerCourtJudgments(sourceJudgment, correctionList);
        for (String lowerCourtJudgment : lowerCourtJudgments) {
            if (!judgment.containsLowerCourtJudgment(lowerCourtJudgment)) {
                judgment.addLowerCourtJudgment(lowerCourtJudgment);
            }
        }
    }




    //------------------------ SETTERS --------------------------

    public void setJudgmentDataExtractor(JudgmentDataExtractor<JUDGMENT, SOURCE_JUDGMENT> judgmentDataExtractor) {
        this.judgmentDataExtractor = judgmentDataExtractor;
    }

    
    
    
    
}
