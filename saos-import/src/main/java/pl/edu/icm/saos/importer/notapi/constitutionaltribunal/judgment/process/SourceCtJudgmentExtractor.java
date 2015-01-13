package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process;

import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.converter.JudgeConverter;
import pl.edu.icm.saos.importer.common.converter.JudgmentDataExtractor;
import pl.edu.icm.saos.importer.common.converter.JudgmentDataExtractorAdapter;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.SourceJudge;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment.SourceCtDissentingOpinion;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SourceCode;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@Service("sourceCtJudgmentExtractor")
public class SourceCtJudgmentExtractor extends JudgmentDataExtractorAdapter<ConstitutionalTribunalJudgment, SourceCtJudgment> {

    private JudgeConverter judgeConverter;
    
    
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
    public JudgmentType extractJudgmentType(SourceCtJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return JudgmentType.valueOf(sourceJudgment.getJudgmentType());
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
    public void setJudgeConverter(JudgeConverter judgeConverter) {
        this.judgeConverter = judgeConverter;
    }

}
