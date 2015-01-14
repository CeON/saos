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

/**
 * Implementation of {@link JudgmentDataExtractor} that delegates extracting
 * data of judgment to {@link #setCommonJudgmentDataExtractor(JudgmentDataExtractor)}
 * and {@link #setSpecificJudgmentDataExtractor(JudgmentDataExtractor)}.
 * 
 * @author madryk
 */
public class DelegatingJudgmentDataExtractor<JUDGMENT extends Judgment, SOURCE_JUDGMENT extends SourceJudgment> implements JudgmentDataExtractor<JUDGMENT, SOURCE_JUDGMENT> {

    private JudgmentDataExtractor<Judgment, SourceJudgment> commonJudgmentDataExtractor;
    
    private JudgmentDataExtractor<JUDGMENT, SOURCE_JUDGMENT> specificJudgmentDataExtractor;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public JUDGMENT createNewJudgment() {
        return specificJudgmentDataExtractor.createNewJudgment();
    }

    @Override
    public List<CourtCase> extractCourtCases(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        List<CourtCase> courtCases = specificJudgmentDataExtractor.extractCourtCases(sourceJudgment, correctionList);
        if (courtCases.isEmpty()) {
            courtCases = commonJudgmentDataExtractor.extractCourtCases(sourceJudgment, correctionList);
        }
        
        return courtCases;
    }

    @Override
    public String extractTextContent(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String textContent = specificJudgmentDataExtractor.extractTextContent(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(textContent)) {
            textContent = commonJudgmentDataExtractor.extractTextContent(sourceJudgment, correctionList);
        }
        
        return textContent;
    }

    @Override
    public DateTime extractPublicationDate(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        DateTime publicationDate = specificJudgmentDataExtractor.extractPublicationDate(sourceJudgment, correctionList);
        if (publicationDate == null) {
            publicationDate = commonJudgmentDataExtractor.extractPublicationDate(sourceJudgment, correctionList);
        }
        
        return publicationDate;
    }

    @Override
    public String extractPublisher(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String publisher = specificJudgmentDataExtractor.extractPublisher(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(publisher)) {
            publisher = commonJudgmentDataExtractor.extractPublisher(sourceJudgment, correctionList);
        }
        
        return publisher;
    }

    @Override
    public String extractReviser(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String reviser = specificJudgmentDataExtractor.extractReviser(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(reviser)) {
            reviser = commonJudgmentDataExtractor.extractReviser(sourceJudgment, correctionList);
        }
        
        return reviser;
    }

    @Override
    public List<Judge> extractJudges(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        List<Judge> judges = specificJudgmentDataExtractor.extractJudges(sourceJudgment, correctionList);
        if (judges.isEmpty()) {
            judges = commonJudgmentDataExtractor.extractJudges(sourceJudgment, correctionList);
        }
        
        return judges;
    }

    @Override
    public List<JudgmentReferencedRegulation> extractReferencedRegulations(
            SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        List<JudgmentReferencedRegulation> referencedRegulations =
                specificJudgmentDataExtractor.extractReferencedRegulations(sourceJudgment, correctionList);
        
        if (referencedRegulations.isEmpty()) {
            referencedRegulations = commonJudgmentDataExtractor.extractReferencedRegulations(sourceJudgment, correctionList);
        }
        
        return referencedRegulations;
    }

    @Override
    public JudgmentType extractJudgmentType(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        JudgmentType judgmentType = specificJudgmentDataExtractor.extractJudgmentType(sourceJudgment, correctionList);
        if (judgmentType == null) {
            judgmentType = commonJudgmentDataExtractor.extractJudgmentType(sourceJudgment, correctionList);
        }
        
        return judgmentType;
    }

    @Override
    public List<String> extractLegalBases(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        List<String> legalBases = specificJudgmentDataExtractor.extractLegalBases(sourceJudgment, correctionList);
        if (legalBases.isEmpty()) {
            legalBases = commonJudgmentDataExtractor.extractLegalBases(sourceJudgment, correctionList);
        }
        
        return legalBases;
    }

    @Override
    public String extractSummary(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String summary = specificJudgmentDataExtractor.extractSummary(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(summary)) {
            summary = commonJudgmentDataExtractor.extractSummary(sourceJudgment, correctionList);
        }
        
        return summary;
    }

    @Override
    public String extractDecision(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String decision = specificJudgmentDataExtractor.extractDecision(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(decision)) {
            decision = commonJudgmentDataExtractor.extractDecision(sourceJudgment, correctionList);
        }
        
        return decision;
    }

    @Override
    public List<String> extractCourtReporters(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        List<String> courtReporters = specificJudgmentDataExtractor.extractCourtReporters(sourceJudgment, correctionList);
        if (courtReporters.isEmpty()) {
            courtReporters = commonJudgmentDataExtractor.extractCourtReporters(sourceJudgment, correctionList);
        }
        
        return courtReporters;
    }

    @Override
    public LocalDate extractJudgmentDate(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        LocalDate judgmentDate = specificJudgmentDataExtractor.extractJudgmentDate(sourceJudgment, correctionList);
        if (judgmentDate == null) {
            judgmentDate = commonJudgmentDataExtractor.extractJudgmentDate(sourceJudgment, correctionList);
        }
        
        return judgmentDate;
    }

    @Override
    public String extractSourceJudgmentId(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String sourceJudgmentId = specificJudgmentDataExtractor.extractSourceJudgmentId(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(sourceJudgmentId)) {
            sourceJudgmentId = commonJudgmentDataExtractor.extractSourceJudgmentId(sourceJudgment, correctionList);
        }
        
        return sourceJudgmentId;
    }

    @Override
    public String extractSourceJudgmentUrl(SOURCE_JUDGMENT sourceJudgment,
            ImportCorrectionList correctionList) {
        String sourceJudgmentUrl = specificJudgmentDataExtractor.extractSourceJudgmentUrl(sourceJudgment, correctionList);
        if (StringUtils.isEmpty(sourceJudgmentUrl)) {
            sourceJudgmentUrl = commonJudgmentDataExtractor.extractSourceJudgmentUrl(sourceJudgment, correctionList);
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

    
    //------------------------ SETTERS --------------------------
    
    public void setCommonJudgmentDataExtractor(
            JudgmentDataExtractor<Judgment, SourceJudgment> commonJudgmentDataExtractor) {
        this.commonJudgmentDataExtractor = commonJudgmentDataExtractor;
    }

    public void setSpecificJudgmentDataExtractor(
            JudgmentDataExtractor<JUDGMENT, SOURCE_JUDGMENT> specificJudgmentDataExtractor) {
        this.specificJudgmentDataExtractor = specificJudgmentDataExtractor;
    }

}
