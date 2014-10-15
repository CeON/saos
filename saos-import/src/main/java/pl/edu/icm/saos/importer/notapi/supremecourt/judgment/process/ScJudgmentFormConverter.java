package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;

import com.google.common.collect.Maps;

/**
 * @author Łukasz Dumiszewski
 */
@Service("judgmentFormConverter")
class ScJudgmentFormConverter {

    private Map<String, JudgmentType> judgmentTypeMap = Maps.newHashMap();

    {
        judgmentTypeMap.put("WYROK", JudgmentType.SENTENCE);
        judgmentTypeMap.put("POSTANOWIENIE", JudgmentType.DECISION);
        judgmentTypeMap.put("UCHWAŁA", JudgmentType.RESOLUTION);
        judgmentTypeMap.put("ZARZĄDZENIE", JudgmentType.REGULATION);
    }
    
    
    public JudgmentType convertToType(String judgmentFormName) {
        
        if (StringUtils.isBlank(judgmentFormName)) {
            return JudgmentType.SENTENCE;
        }
        
        judgmentFormName = judgmentFormName.trim().toUpperCase();
        
        for (Map.Entry<String, JudgmentType> entry : judgmentTypeMap.entrySet()) {
            if (judgmentFormName.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        return JudgmentType.SENTENCE;
        
    }
    
    
   
    
}
