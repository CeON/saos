package pl.edu.icm.saos.importer.common.converter;

import java.util.List;

import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SourceCode;

public class JudgmentDataExtractorAdapter<JUDGMENT extends Judgment, SOURCE_JUDGMENT extends SourceJudgment> implements JudgmentDataExtractor<JUDGMENT, SOURCE_JUDGMENT> {

    @Override
    public JUDGMENT createNewJudgment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<CourtCase> extractCourtCases(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    @Override
    public String extractTextContent(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public DateTime extractPublicationDate(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractPublisher(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractReviser(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public List<Judge> extractJudges(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    @Override
    public List<JudgmentReferencedRegulation> extractReferencedRegulations(
            SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    @Override
    public JudgmentType extractJudgmentType(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public List<String> extractLegalBases(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    @Override
    public String extractSummary(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractDecision(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public List<String> extractCourtReporters(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    @Override
    public LocalDate extractJudgmentDate(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractSourceJudgmentId(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractSourceJudgmentUrl(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public SourceCode getSourceCode() {
        return null; // ???
    }

    @Override
    public void convertSpecific(JUDGMENT judgment,
            SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        
    }

}
