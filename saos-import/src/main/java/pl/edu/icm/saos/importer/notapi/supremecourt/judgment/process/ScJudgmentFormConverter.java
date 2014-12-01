package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createUpdate;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

import com.google.common.collect.Maps;

/**
 * @author Łukasz Dumiszewski
 */
@Service("judgmentFormConverter")
class ScJudgmentFormConverter {

    
    private ScJudgmentFormNameNormalizer scJudgmentFormNameNormalizer;
    
    
    private Map<String, JudgmentType> judgmentTypeMap = Maps.newHashMap();

    {
        judgmentTypeMap.put("WYROK", JudgmentType.SENTENCE);
        judgmentTypeMap.put("POSTANOWIENIE", JudgmentType.DECISION);
        judgmentTypeMap.put("UCHWAŁA", JudgmentType.RESOLUTION);
        judgmentTypeMap.put("ZARZĄDZENIE", JudgmentType.REGULATION);
    }
    
    
    /**
     * Converts the given judgmentFormName (see: {@link SupremeCourtJudgmentForm#getName()}) to 
     * appropriate {@link JudgmentType}. <br/><br/>
     * Before the actual conversion, the judgmentFormName is normalized by {@link ScJudgmentFormNameNormalizer#normalize(String)}
     * <br/><br/>
     * The conversion algorithm uses {@link #setJudgmentTypeMap(Map)}. <br/>
     * If a given map key contains the uppercased judgmentFormName, then the {@link JudgmentType} defined in a corresponding map value
     * is returned.
     * If the passed and normalized judgmentFormName cannot be found in the map then the method returns {@link JudgmentType#SENTENCE}
     * <br/>
     * A an appropriate correction is added to the correctionList if the judgmentFormName has been changed by normalization
     * or cannot be found in the map.
     **/
    public JudgmentType convertToJudgmentType(String judgmentFormName, ImportCorrectionList correctionList) {
        
        JudgmentType judgmentType = null;
        
        String normalizedJudgmentFormName = scJudgmentFormNameNormalizer.normalize(judgmentFormName);
        
        judgmentType = getJudgmentType(normalizedJudgmentFormName);
        
        if (judgmentType == null || scJudgmentFormNameNormalizer.isChangedByNormalization(judgmentFormName)) {
            judgmentType = judgmentType==null? JudgmentType.SENTENCE: judgmentType;
            
            correctionList.addCorrection(createUpdate(null)
                                             .ofProperty(CorrectedProperty.JUDGMENT_TYPE)
                                             .oldValue(StringUtils.trim(judgmentFormName))
                                             .newValue(judgmentType.name())
                                             .build());
        }
        
        return judgmentType;
        
    }

    
    //------------------------ PRIVATE --------------------------
    
    private JudgmentType getJudgmentType(String normalizedJudgmentFormName) {
        if (StringUtils.isNotBlank(normalizedJudgmentFormName)) {
            for (Map.Entry<String, JudgmentType> entry : judgmentTypeMap.entrySet()) {
                if (normalizedJudgmentFormName.toUpperCase(Locale.ROOT).contains(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }


    //------------------------ SETTERS --------------------------
    
    public void setJudgmentTypeMap(Map<String, JudgmentType> judgmentTypeMap) {
        this.judgmentTypeMap = judgmentTypeMap;
    }

    @Autowired
    public void setScJudgmentFormNameNormalizer(ScJudgmentFormNameNormalizer scJudgmentFormNameNormalizer) {
        this.scJudgmentFormNameNormalizer = scJudgmentFormNameNormalizer;
    }
    
    
   
    
}
