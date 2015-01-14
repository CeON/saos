package pl.edu.icm.saos.importer.common.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.SourceJudge;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;

import com.google.common.collect.Lists;

/**
 * Implementation of {@link JudgmentDataExtractor} that can extract
 * data common to different types of {@link Judgment}s 
 * 
 * @author madryk
 */
@Service
public class CommonJudgmentDataExtractor<JUDGMENT extends Judgment, SOURCE_JUDGMENT extends SourceJudgment> extends JudgmentDataExtractorAdapter<JUDGMENT, SOURCE_JUDGMENT> {


    private JudgeConverter judgeConverter;


    //------------------------ LOGIC --------------------------

    @Override
    public String extractTextContent(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getTextContent();
    }

    @Override
    public DateTime extractPublicationDate(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getSource().getPublicationDateTime();
    }

    @Override
    public List<Judge> extractJudges(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        List<Judge> judges = Lists.newArrayList();
        
        for (SourceJudge scJudge : sourceJudgment.getJudges()) {
            
            List<JudgeRole> roles = scJudge.getSpecialRoles().stream().map(role->JudgeRole.valueOf(role)).collect(Collectors.toList());
            Judge judge = judgeConverter.convertJudge(scJudge.getName(), roles, correctionList);
            
            if (judge != null) {
                judge.setFunction(scJudge.getFunction());
                judges.add(judge);
            }
        
        }
        
        return judges;
    }

    @Override
    public List<String> extractCourtReporters(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getCourtReporters();
    }

    @Override
    public LocalDate extractJudgmentDate(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getJudgmentDate();
    }

    @Override
    public String extractSourceJudgmentId(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getSource().getSourceJudgmentId();
    }

    @Override
    public String extractSourceJudgmentUrl(SOURCE_JUDGMENT sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getSource().getSourceJudgmentUrl();
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgeConverter(JudgeConverter judgeConverter) {
        this.judgeConverter = judgeConverter;
    }

}
