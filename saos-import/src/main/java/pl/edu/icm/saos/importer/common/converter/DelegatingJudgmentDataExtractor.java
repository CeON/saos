package pl.edu.icm.saos.importer.common.converter;

import java.util.List;

import org.jadira.usertype.spi.utils.lang.StringUtils;
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

public class DelegatingJudgmentDataExtractor<JUDGMENT extends Judgment, SOURCE_JUDGMENT extends SourceJudgment> implements JudgmentDataExtractor<JUDGMENT, SOURCE_JUDGMENT> {

    private CommonJudgmentDataExtractor<Judgment, SourceJudgment> commonJudgmentDataExtractor;
    
    private JudgmentDataExtractor<JUDGMENT, SOURCE_JUDGMENT> specificJudgmentDataExtractor;
    
    
    @Override
    public JUDGMENT createNewJudgment() {
        return specificJudgmentDataExtractor.createNewJudgment();
    }

    @Override
    public List<CourtCase> extractCourtCases(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        List<CourtCase> courtCases = commonJudgmentDataExtractor.extractCourtCases(sourceJudgment, correctionList);
        if (courtCases.isEmpty()) {
            courtCases = specificJudgmentDataExtractor.extractCourtCases(sourceJudgment, correctionList);
        }
        
        return courtCases;
    }

    @Override
    public String extractTextContent(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String textContent = commonJudgmentDataExtractor.extractTextContent(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(textContent)) {
            textContent = specificJudgmentDataExtractor.extractTextContent(sourceJudgment, correctionList);
        }
        
        return textContent;
    }

    @Override
    public DateTime extractPublicationDate(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        DateTime publicationDate = commonJudgmentDataExtractor.extractPublicationDate(sourceJudgment, correctionList);
        if (publicationDate == null) {
            publicationDate = specificJudgmentDataExtractor.extractPublicationDate(sourceJudgment, correctionList);
        }
        
        return publicationDate;
    }

    @Override
    public String extractPublisher(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String publisher = commonJudgmentDataExtractor.extractPublisher(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(publisher)) {
            publisher = specificJudgmentDataExtractor.extractPublisher(sourceJudgment, correctionList);
        }
        
        return publisher;
    }

    @Override
    public String extractReviser(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String reviser = commonJudgmentDataExtractor.extractReviser(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(reviser)) {
            reviser = specificJudgmentDataExtractor.extractReviser(sourceJudgment, correctionList);
        }
        
        return reviser;
    }

    @Override
    public List<Judge> extractJudges(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        List<Judge> judges = commonJudgmentDataExtractor.extractJudges(sourceJudgment, correctionList);
        if (judges.isEmpty()) {
            judges = specificJudgmentDataExtractor.extractJudges(sourceJudgment, correctionList);
        }
        
        return judges;
    }

    @Override
    public List<JudgmentReferencedRegulation> extractReferencedRegulations(
            SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        List<JudgmentReferencedRegulation> referencedRegulations = commonJudgmentDataExtractor.extractReferencedRegulations(sourceJudgment, correctionList);
        if (referencedRegulations.isEmpty()) {
            referencedRegulations = specificJudgmentDataExtractor.extractReferencedRegulations(sourceJudgment, correctionList);
        }
        
        return referencedRegulations;
    }

    @Override
    public JudgmentType extractJudgmentType(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        JudgmentType judgmentType = commonJudgmentDataExtractor.extractJudgmentType(sourceJudgment, correctionList);
        if (judgmentType == null) {
            judgmentType = specificJudgmentDataExtractor.extractJudgmentType(sourceJudgment, correctionList);
        }
        
        return judgmentType;
    }

    @Override
    public List<String> extractLegalBases(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        List<String> legalBases = commonJudgmentDataExtractor.extractLegalBases(sourceJudgment, correctionList);
        if (legalBases.isEmpty()) {
            legalBases = specificJudgmentDataExtractor.extractLegalBases(sourceJudgment, correctionList);
        }
        
        return legalBases;
    }

    @Override
    public String extractSummary(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String summary = commonJudgmentDataExtractor.extractSummary(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(summary)) {
            summary = specificJudgmentDataExtractor.extractSummary(sourceJudgment, correctionList);
        }
        
        return summary;
    }

    @Override
    public String extractDecision(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String decision = commonJudgmentDataExtractor.extractDecision(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(decision)) {
            decision = specificJudgmentDataExtractor.extractDecision(sourceJudgment, correctionList);
        }
        
        return decision;
    }

    @Override
    public List<String> extractCourtReporters(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        List<String> courtReporters = commonJudgmentDataExtractor.extractCourtReporters(sourceJudgment, correctionList);
        if (courtReporters.isEmpty()) {
            courtReporters = specificJudgmentDataExtractor.extractCourtReporters(sourceJudgment, correctionList);
        }
        
        return courtReporters;
    }

    @Override
    public LocalDate extractJudgmentDate(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        LocalDate judgmentDate = commonJudgmentDataExtractor.extractJudgmentDate(sourceJudgment, correctionList);
        if (judgmentDate == null) {
            judgmentDate = specificJudgmentDataExtractor.extractJudgmentDate(sourceJudgment, correctionList);
        }
        
        return judgmentDate;
    }

    @Override
    public String extractSourceJudgmentId(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String sourceJudgmentId = commonJudgmentDataExtractor.extractSourceJudgmentId(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(sourceJudgmentId)) {
            sourceJudgmentId = specificJudgmentDataExtractor.extractSourceJudgmentId(sourceJudgment, correctionList);
        }
        
        return sourceJudgmentId;
    }

    @Override
    public String extractSourceJudgmentUrl(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String sourceJudgmentUrl = commonJudgmentDataExtractor.extractSourceJudgmentUrl(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(sourceJudgmentUrl)) {
            sourceJudgmentUrl = specificJudgmentDataExtractor.extractSourceJudgmentUrl(sourceJudgment, correctionList);
        }
        
        return sourceJudgmentUrl;
    }

    @Override
    public SourceCode getSourceCode() {
        return specificJudgmentDataExtractor.getSourceCode();
    }

    @Override
    public void convertSpecific(JUDGMENT judgment,
            SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        specificJudgmentDataExtractor.convertSpecific(judgment, sourceJudgment, correctionList);
    }

    public void setCommonJudgmentDataExtractor(
            CommonJudgmentDataExtractor<Judgment, SourceJudgment> commonJudgmentDataExtractor) {
        this.commonJudgmentDataExtractor = commonJudgmentDataExtractor;
    }

    public void setSpecificJudgmentDataExtractor(
            JudgmentDataExtractor<JUDGMENT, SOURCE_JUDGMENT> specificJudgmentDataExtractor) {
        this.specificJudgmentDataExtractor = specificJudgmentDataExtractor;
    }

}
