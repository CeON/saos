package pl.edu.icm.saos.importer.common.converter;

import static pl.edu.icm.saos.common.util.StringTools.toRootLowerCase;
import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createDelete;
import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createUpdate;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.util.PersonNameNormalizer;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Service
public class JudgeConverter {

    
    private JudgeNameNormalizer judgeNameNormalizer;

    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts judgeName to Judge. Set specialRoles in created Judge ({@link Judge#setSpecialRoles(List)}).<br/>
     * Uses {@link JudgeNameNormalizer} to correct judgeName.
     * If the normalizedJudgeName is different than judgeName then a relevant correction 
     * is added to correctionList.
     * @return created {@link Judge} or null if the judge cannot be created (if the judge name after normalization is empty)
     * @throws IllegalArgumentException if judgeName is blank
     * 
     */
    public Judge convertJudge(String judgeName, List<JudgeRole> specialRoles, ImportCorrectionList correctionList) {
        
        Preconditions.checkNotNull(correctionList);
        
        String normalizedJudgeName = judgeNameNormalizer.normalize(judgeName);
        
        if (StringUtils.isBlank(normalizedJudgeName)) {
            correctionList.addCorrection(createDelete(Judge.class).oldValue(judgeName).newValue(null).build());
            return null;
        }
        
        Judge judge = new Judge(normalizedJudgeName, specialRoles);
        
        if (!(PersonNameNormalizer.unify(judgeName).equals(toRootLowerCase(normalizedJudgeName)))) {
            correctionList.addCorrection(createUpdate(judge)
                                         .ofProperty(CorrectedProperty.NAME).oldValue(judgeName).newValue(normalizedJudgeName)
                                         .build());
        }
        
        return judge;
    }
    
    
    /**
     * Invokes {@link #convertJudge(String, List, ImportCorrectionList)} with empty role list. 
     */
    public Judge convertJudge(String judgeName, ImportCorrectionList correctionList) {
        
        return convertJudge(judgeName, Lists.newArrayList(), correctionList);
    
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgeNameNormalizer(JudgeNameNormalizer judgeNameNormalizer) {
        this.judgeNameNormalizer = judgeNameNormalizer;
    }
    
}
