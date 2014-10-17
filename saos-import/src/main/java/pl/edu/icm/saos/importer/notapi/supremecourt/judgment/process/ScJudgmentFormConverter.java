package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

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
    
    
    /**
     * Converts the given judgmentFormName (see: {@link SupremeCourtJudgmentForm#getName()}) to 
     * appropriate {@link JudgmentType}. <br/>
     * The conversion algorithm uses {@link #setJudgmentTypeMap(Map)}. <br/>
     * If a given map key contains the uppercased judgmentFormName, then the {@link JudgmentType} defined in a corresponding map value
     * is returned.
     * If the passed judgmentFormName is blank or cannot be found in the map, then the method returns {@link JudgmentType#SENTENCE}.
     */
    public JudgmentType convertToType(String judgmentFormName) {
        
        if (StringUtils.isBlank(judgmentFormName)) {
            return JudgmentType.SENTENCE;
        }
        
        judgmentFormName = judgmentFormName.trim().toUpperCase(Locale.ROOT);
        
        for (Map.Entry<String, JudgmentType> entry : judgmentTypeMap.entrySet()) {
            if (judgmentFormName.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        return JudgmentType.SENTENCE;
        
    }


    //------------------------ SETTERS --------------------------
    
    public void setJudgmentTypeMap(Map<String, JudgmentType> judgmentTypeMap) {
        this.judgmentTypeMap = judgmentTypeMap;
    }
    
    
   
    
}
