package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

import com.google.common.collect.Maps;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scJudgmentFormNameNormalizer")
class ScJudgmentFormNameNormalizer {

    
    private Map<String, String> normalizedNameMap = Maps.newHashMap();

    {
        normalizedNameMap.put("wyciąg z protokołu", "wyrok");
        normalizedNameMap.put("orzeczenie", "wyrok");
    }
    
    
    /**
     * Normalizes the {@link SupremeCourtJudgmentForm} name, changes it to a proper value, e.g.
     * orzecznie -> wyrok 
     */
    public String normalize(String judgmentFormName) {
        
        if (StringUtils.isBlank(judgmentFormName)) {
            return null;
        }
        
        judgmentFormName = judgmentFormName.trim();
        
        for (Map.Entry<String, String> entry : normalizedNameMap.entrySet()) {
            if (StringUtils.containsIgnoreCase(judgmentFormName, entry.getKey())) {
                return judgmentFormName.replace(entry.getKey(), entry.getValue());
            }
        }
        
        return judgmentFormName;
    }
    
}
