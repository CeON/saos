package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.JudgmentMeansOfAppealCreator;
import pl.edu.icm.saos.importer.common.JudgmentResultCreator;
import pl.edu.icm.saos.importer.common.converter.JudgmentDataExtractor;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgeExtractorHelper;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment.SourceCtDissentingOpinion;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentResult;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;
import pl.edu.icm.saos.persistence.model.SourceCode;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@Service("sourceCtJudgmentExtractor")
public class SourceCtJudgmentExtractor implements JudgmentDataExtractor<ConstitutionalTribunalJudgment, SourceCtJudgment> {
    
    private JudgmentMeansOfAppealCreator judgmentMeansOfAppealCreator;
    
    private JudgmentResultCreator judgmentResultCreator;
    
    private SourceJudgeExtractorHelper sourceJudgeExtractorHelper;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public ConstitutionalTribunalJudgment createNewJudgment() {
        return new ConstitutionalTribunalJudgment();
    }

    @Override
    public List<CourtCase> extractCourtCases(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList(new CourtCase(sourceJudgment.getCaseNumber()));
    }

    @Override
    public String extractTextContent(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getTextContent();
    }

    @Override
    public DateTime extractPublicationDate(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractPublisher(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractReviser(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public List<Judge> extractJudges(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgeExtractorHelper.extractJudges(sourceJudgment, correctionList);
    }

    @Override
    public List<JudgmentReferencedRegulation> extractReferencedRegulations(
            SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    @Override
    public JudgmentType extractJudgmentType(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return JudgmentType.valueOf(sourceJudgment.getJudgmentType());
    }

    @Override
    public List<String> extractLegalBases(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    @Override
    public String extractSummary(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractDecision(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public List<String> extractCourtReporters(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getCourtReporters();
    }

    @Override
    public LocalDate extractJudgmentDate(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getJudgmentDate();
    }
    
    @Override
    public LocalDate extractReceiptDate(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getReceiptDate();
    }

    @Override
    public List<String> extractLowerCourtJudgments(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList(sourceJudgment.getLowerCourtJudgments());
    }

    @Override
    public MeansOfAppeal extractMeansOfAppeal(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        if (StringUtils.isEmpty(sourceJudgment.getMeansOfAppeal())) {
            return null;
        }
        return judgmentMeansOfAppealCreator.fetchOrCreateMeansOfAppeal(CourtType.CONSTITUTIONAL_TRIBUNAL, sourceJudgment.getMeansOfAppeal());
    }

    @Override
    public JudgmentResult extractJudgmentResult(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        if (StringUtils.isEmpty(sourceJudgment.getJudgmentResult())) {
            return null;
        }
        return judgmentResultCreator.fetchOrCreateJudgmentResult(CourtType.CONSTITUTIONAL_TRIBUNAL, sourceJudgment.getJudgmentResult());
    }

    @Override
    public String extractSourceJudgmentId(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getSource().getSourceJudgmentId();
    }

    @Override
    public String extractSourceJudgmentUrl(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getSource().getSourceJudgmentUrl();
    }

    @Override
    public SourceCode getSourceCode() {
        return SourceCode.CONSTITUTIONAL_TRIBUNAL;
    }

    @Override
    public void convertSpecific(ConstitutionalTribunalJudgment judgment,
            SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        
        extractDissentingOpinions(judgment, sourceJudgment).forEach(o -> judgment.addDissentingOpinion(o));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private List<ConstitutionalTribunalJudgmentDissentingOpinion> extractDissentingOpinions(
            ConstitutionalTribunalJudgment judgment, SourceCtJudgment sourceJudgment) {
        
        List<ConstitutionalTribunalJudgmentDissentingOpinion> dissentingOpinions = Lists.newArrayList();
        
        for (SourceCtDissentingOpinion sourceDissentingOpinion : sourceJudgment.getDissentingOpinions()) {
            ConstitutionalTribunalJudgmentDissentingOpinion dissentingOpinion = extractDissentingOpinion(sourceDissentingOpinion);
            dissentingOpinions.add(dissentingOpinion);
        }
        
        return dissentingOpinions;
    }
    
    private ConstitutionalTribunalJudgmentDissentingOpinion extractDissentingOpinion(
            SourceCtDissentingOpinion sourceDissentingOpinion) {
        ConstitutionalTribunalJudgmentDissentingOpinion dissentingOpinion = 
                new ConstitutionalTribunalJudgmentDissentingOpinion();
        sourceDissentingOpinion.getAuthors().forEach(a -> dissentingOpinion.addAuthor(a));
        dissentingOpinion.setTextContent(sourceDissentingOpinion.getTextContent());
        
        return dissentingOpinion;
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setJudgmentMeansOfAppealCreator(JudgmentMeansOfAppealCreator judgmentMeansOfAppealCreator) {
        this.judgmentMeansOfAppealCreator = judgmentMeansOfAppealCreator;
    }

    @Autowired
    public void setJudgmentResultCreator(JudgmentResultCreator judgmentResultCreator) {
        this.judgmentResultCreator = judgmentResultCreator;
    }

    @Autowired
    public void setSourceJudgeExtractorHelper(SourceJudgeExtractorHelper sourceJudgeExtractorHelper) {
        this.sourceJudgeExtractorHelper = sourceJudgeExtractorHelper;
    }

}
