package pl.edu.icm.saos.persistence.correction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * 
 * Service facilitating the savings of {@link JudgmentCorrection}s 
 * 
 * @author Łukasz Dumiszewski
 */

@Service("judgmentCorrectionSaveService")
public class JudgmentCorrectionSaveService {

    
    private JudgmentCorrectionRepository judgmentCorrectionRepository;
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Creates {@link JudgmentCorrection} from the passed arguments and saves it using
     * {@link JudgmentCorrectionRepository#save(JudgmentCorrection)}.<br/><br/>
     * Use this method if you want to save the correction of a simple and direct judgment attribute, 
     * e.g. {@link Judgment#getJudgmentType()}.
     * 
     * 
     * @param judgment judgment that the correction relates to
     * @param property corrected property
     * @param oldValue old value of the corrected property
     * @param newValue new value of the corrected property
     */
    public void saveCorrection(Judgment judgment, CorrectedProperty property, String oldValue, String newValue) {
        
        JudgmentCorrection judgmentCorrection = new JudgmentCorrection(judgment, null, null, property, oldValue, newValue);
        
        judgmentCorrectionRepository.save(judgmentCorrection);
    }
    
    
    
    /**
     * Creates {@link JudgmentCorrection} from the passed arguments and saves it using
     * {@link JudgmentCorrectionRepository#save(JudgmentCorrection)}. <br/><br/>
     * Use this method if you want to save the correction of a judgment attribute that is a separate
     * database entity, e.g. {@link Judgment#getJudges()}
     * 
     * @param judgment judgment that the correction relates to
     * @param correctedObjectClass class of the corrected attribute of the specified judgment
     * @param correctedObjectId id of the corrected object, see: {@link DataObject#getId()}
     * @param property corrected property
     * @param oldValue old value of the corrected property
     * @param newValue new value of the corrected property
     */
    public void saveCorrection(Judgment judgment, Class<? extends DataObject> correctedObjectClass, int correctedObjectId, CorrectedProperty property, String oldValue, String newValue) {
        
        JudgmentCorrection judgmentCorrection = new JudgmentCorrection(judgment, correctedObjectClass, correctedObjectId, property, oldValue, newValue);
        
        judgmentCorrectionRepository.save(judgmentCorrection);
    }

    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentCorrectionRepository(JudgmentCorrectionRepository judgmentCorrectionRepository) {
        this.judgmentCorrectionRepository = judgmentCorrectionRepository;
    }
    
    
}
