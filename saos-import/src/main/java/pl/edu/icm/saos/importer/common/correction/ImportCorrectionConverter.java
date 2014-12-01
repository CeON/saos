package pl.edu.icm.saos.importer.common.correction;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.correction.model.ChangeOperation;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrectionBuilder;
import pl.edu.icm.saos.persistence.model.Judgment;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("importCorrectionConverter")
public class ImportCorrectionConverter {

    
    
    //------------------------ LOGIC --------------------------
    
    
    public JudgmentCorrection convertToJudgmentCorrection(Judgment judgment, ImportCorrection importCorrection) {
        
        Preconditions.checkNotNull(importCorrection);
        Preconditions.checkNotNull(judgment);
        
        JudgmentCorrectionBuilder jCorrectionBuilder = JudgmentCorrectionBuilder.createFor(judgment);
        
        if (importCorrection.getChangeOperation().equals(ChangeOperation.UPDATE)) {
            jCorrectionBuilder.update(getCorrectedObject(importCorrection, judgment));
        }
        
        else if (importCorrection.getChangeOperation().equals(ChangeOperation.CREATE)) {
            jCorrectionBuilder.create(getCorrectedObject(importCorrection, judgment));
        }
        
        else {
            jCorrectionBuilder.delete(importCorrection.getDeletedObjectClass());
        }
        
        return jCorrectionBuilder.property(importCorrection.getCorrectedProperty())
                                             .oldValue(importCorrection.getOldValue())
                                             .newValue(importCorrection.getNewValue())
                                             .build();
    }


    
    
    public List<JudgmentCorrection> convertToJudgmentCorrections(Judgment judgment, List<ImportCorrection> importCorrections) {
        
        return importCorrections.stream().map(c->convertToJudgmentCorrection(judgment, c)).collect(Collectors.toList());
 
    }
 
    
    //------------------------ PRIVATE --------------------------
    
    



    private DataObject getCorrectedObject(ImportCorrection importCorrection, Judgment judgment) {
        
        DataObject correctedObject = importCorrection.getCorrectedObject();
        
        if (correctedObject == null) {
            correctedObject = judgment;
        }
        
        
        return correctedObject;
    }
 
    
}
