package pl.edu.icm.saos.importer.common.correction;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.model.Judgment;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("importCorrectionConverter")
public class ImportCorrectionConverter {

    
    //------------------------ LOGIC --------------------------
    
    
    public JudgmentCorrection convertToJudgmentCorrection(Judgment judgment, ImportCorrection importCorrection) {
        
        DataObject correctedObject = importCorrection.getCorrectedObject();
        
        if (correctedObject != null) {
            Preconditions.checkArgument(correctedObject.isPersisted());
        }
        
        Class<? extends DataObject> correctedObjectClass = null;
        Integer correctedObjectId = null;
        
        if (correctedObject != null) {
            correctedObjectClass = correctedObject.getClass();
            correctedObjectId = correctedObject.getId();
        }
        
        JudgmentCorrection jCorrection = new JudgmentCorrection(judgment, correctedObjectClass, correctedObjectId, 
                                                                importCorrection.getCorrectedProperty(), importCorrection.getOldValue(), 
                                                                importCorrection.getNewValue());
        
        return jCorrection;
    }
    
    
    public List<JudgmentCorrection> convertToJudgmentCorrections(Judgment judgment, List<ImportCorrection> importCorrections) {
        
        return importCorrections.stream().map(c->convertToJudgmentCorrection(judgment, c)).collect(Collectors.toList());
 
    }
    
}
