package pl.edu.icm.saos.importer.common.converter;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SourceCode;

/**
 * Empty implementation of {@link JudgmentDataExtractor}.
 * Simplifies subclasses of {@link JudgmentDataExtractor} that
 * extracts only few of judgment properties
 * 
 * @author madryk
 */
public class JudgmentDataExtractorAdapter<JUDGMENT extends Judgment, SOURCE_JUDGMENT extends SourceJudgment> implements JudgmentDataExtractor<JUDGMENT, SOURCE_JUDGMENT> {


    //------------------------ LOGIC --------------------------
    
    @Override
    public JUDGMENT createNewJudgment() {
        throw new UnsupportedOperationException("This implementation doesn't support creating new judgments");
    }

    @Override
    public List<CourtCase> extractCourtCases(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    @Override
    public String extractTextContent(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public DateTime extractPublicationDate(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractPublisher(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractReviser(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public List<Judge> extractJudges(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    @Override
    public List<JudgmentReferencedRegulation> extractReferencedRegulations(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    @Override
    public JudgmentType extractJudgmentType(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public List<String> extractLegalBases(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    @Override
    public String extractSummary(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractDecision(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public List<String> extractCourtReporters(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    @Override
    public LocalDate extractJudgmentDate(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractSourceJudgmentId(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractSourceJudgmentUrl(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public SourceCode getSourceCode() {
        return null;
    }

    @Override
    public void convertSpecific(JUDGMENT judgment, SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        
    }

}
