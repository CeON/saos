package pl.edu.icm.saos.importer.notapi.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.converter.JudgeConverter;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.SourceJudge;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;

import com.google.common.collect.Lists;

/**
 * Helper class for extracting {@link Judge}s from {@link SourceJudgment}
 * 
 * @author madryk
 */
@Service
public class SourceJudgeExtractorHelper {

    private JudgeConverter judgeConverter;
    
    
    //------------------------ LOGIC --------------------------
    
    
    public List<Judge> extractJudges(SourceJudgment sourceJudgment, ImportCorrectionList correctionList) {
        List<Judge> judges = Lists.newArrayList();
        
        for (SourceJudge sourceJudge : sourceJudgment.getJudges()) {
            
            List<JudgeRole> roles = sourceJudge.getSpecialRoles().stream().map(role->JudgeRole.valueOf(role)).collect(Collectors.toList());
            Judge judge = judgeConverter.convertJudge(sourceJudge.getName(), roles, correctionList);
            
            if (judge != null) {
                judge.setFunction(sourceJudge.getFunction());
                judges.add(judge);
            }
        
        }
        
        return judges;
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setJudgeConverter(JudgeConverter judgeConverter) {
        this.judgeConverter = judgeConverter;
    }
}
