package pl.edu.icm.saos.importer.common.converter;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentResult;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * Contains methods extracting judgment data from source judgments and returning them in the saos model format.
 * If any of extract methods changes the source data then it writes the correction info to the passed
 * {@link ImportCorrectionList} 
 * Each implementation extracts data from a different source judgment class.
 *   
 * @author Łukasz Dumiszewski
 */

public interface JudgmentDataExtractor<JUDGMENT extends Judgment, SOURCE_JUDGMENT> {

    /**
     * Creates specific judgment (depending on the judgment source), for example {@link SupremeCourtJudgment} 
     */
    JUDGMENT createNewJudgment();
    
    List<CourtCase> extractCourtCases(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);

    String extractTextContent(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);
    
    DateTime extractPublicationDate(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);
    
    String extractPublisher(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);
    
    String extractReviser(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);
    
    List<Judge> extractJudges(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);

    List<JudgmentReferencedRegulation> extractReferencedRegulations(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);

    JudgmentType extractJudgmentType(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);

    List<String> extractLegalBases(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);

    String extractSummary(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);

    String extractDecision(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);

    List<String> extractCourtReporters(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);

    LocalDate extractJudgmentDate(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);
    
    LocalDate extractReceiptDate(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);
    
    List<String> extractLowerCourtJudgments(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);
    
    MeansOfAppeal extractMeansOfAppeal(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);
    
    JudgmentResult extractJudgmentResult(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);

    String extractSourceJudgmentId(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);
    
    String extractSourceJudgmentUrl(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);
    
    /**
     * Returns {@link SourceCode} of the judgment source that the given extractor is written for 
     */
    SourceCode getSourceCode();
    
    /**
     * Converts specific judgment attributes that are not part of {@link Judgment} class itself but one of
     * its subclasses (for example {@link CommonCourtJudgment}) 
     */
    void convertSpecific(JUDGMENT judgment, SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList);


    
}
