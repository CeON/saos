package pl.edu.icm.saos.importer.common;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * Contains methods extracting judgment data from source judgments and returning them in the saos model format. 
 * Each implementation extracts data from a different source judgment class.
 *   
 * @author Łukasz Dumiszewski
 */

public interface JudgmentDataExtractor<JUDGMENT extends Judgment, SOURCE_JUDGMENT> {

    /**
     * Creates specific judgment (depending on the judgment source), for example {@link SupremeCourtJudgment} 
     */
    JUDGMENT createNewJudgment();
    
    List<CourtCase> extractCourtCases(SOURCE_JUDGMENT sourceJudgment);

    String extractTextContent(SOURCE_JUDGMENT sourceJudgment);
    
    DateTime extractPublicationDate(SOURCE_JUDGMENT sourceJudgment);
    
    String extractPublisher(SOURCE_JUDGMENT sourceJudgment);
    
    String extractReviser(SOURCE_JUDGMENT sourceJudgment);
    
    List<Judge> extractJudges(SOURCE_JUDGMENT sourceJudgment);

    List<JudgmentReferencedRegulation> extractReferencedRegulations(SOURCE_JUDGMENT sourceJudgment);

    JudgmentType extractJudgmentType(SOURCE_JUDGMENT sourceJudgment);

    List<String> extractLegalBases(SOURCE_JUDGMENT sourceJudgment);

    String extractSummary(SOURCE_JUDGMENT sourceJudgment);

    String extractDecision(SOURCE_JUDGMENT sourceJudgment);

    List<String> extractCourtReporters(SOURCE_JUDGMENT sourceJudgment);

    LocalDate extractJudgmentDate(SOURCE_JUDGMENT sourceJudgment);

    String extractSourceJudgmentId(SOURCE_JUDGMENT sourceJudgment);
    
    String extractSourceJudgmentUrl(SOURCE_JUDGMENT sourceJudgment);
    
    /**
     * Returns {@link SourceCode} of the judgment source that the given extractor is written for 
     */
    SourceCode getSourceCode();
    
    /**
     * Converts specific judgment attributes that are not part of {@link Judgment} class itself but one of
     * its subclasses (for example {@link CommonCourtJudgment}) 
     */
    void convertSpecific(JUDGMENT judgment, SOURCE_JUDGMENT sourceJudgment);


    
}
