package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

import com.google.common.collect.Maps;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scJudgmentFormNameNormalizer")
class ScJudgmentFormNameNormalizer {

    private final static String CASE_INSENSITIVE_REGEX_PREFIX = "(?i)";
    
    private Map<String, String> normalizedNameMap = Maps.newHashMap();

    {
        normalizedNameMap.put("wyciąg z protokołu", "Wyrok");
        normalizedNameMap.put("orzeczenie", "Wyrok");
    }
    
    
    /**
     * Normalizes the {@link SupremeCourtJudgmentForm} name, changes it to a proper value, e.g.
     * orzeczenie -> wyrok, according to {@link #setNormalizedNameMap(Map)}
     */
    public String normalize(String judgmentFormName) {
        
        if (StringUtils.isBlank(judgmentFormName)) {
            return null;
        }
        
                
        judgmentFormName = adjust(judgmentFormName);
        
        for (Map.Entry<String, String> entry : normalizedNameMap.entrySet()) {
            if (StringUtils.containsIgnoreCase(judgmentFormName, entry.getKey())) {
                return judgmentFormName.replaceAll(CASE_INSENSITIVE_REGEX_PREFIX + entry.getKey(), entry.getValue());
            }
        }
        
        return judgmentFormName;
    }

    /**
     * Says whether judgmentFormName is subject to change by normalization, see: {@link #normalize(String)}
     */
    public boolean isChangedByNormalization(String judgmentFormName) {
        
        String adjustedJudgmentFormName = adjust(judgmentFormName);
        String normalizedJudgmentFormName = normalize(judgmentFormName);
        
        if (adjustedJudgmentFormName == null) {
            if (normalizedJudgmentFormName == null) {
                return false;
            } 
        }
        
        return !adjustedJudgmentFormName.equals(normalizedJudgmentFormName);
    }

    

    //------------------------ PRIVATE --------------------------
    
    private String adjust(String judgmentFormName) {
        String adjustedJudgmentFormName = judgmentFormName;
        
        adjustedJudgmentFormName = StringUtils.trim(adjustedJudgmentFormName);
        adjustedJudgmentFormName = WordUtils.capitalize(judgmentFormName);
        
        return adjustedJudgmentFormName;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setNormalizedNameMap(Map<String, String> normalizedNameMap) {
        this.normalizedNameMap = normalizedNameMap;
    }
    
}
