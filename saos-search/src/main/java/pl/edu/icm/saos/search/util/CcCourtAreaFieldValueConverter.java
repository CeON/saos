package pl.edu.icm.saos.search.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.value.CcCourtArea;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.base.Preconditions;

/**
 * 
 *  Converter of cc court area field value into {@link CcCourtArea}
 *  
 *  @see CcCourtAreaFieldValueCreator
 *  @see JudgmentIndexField#CC_DISTRICT_AREA
 *  @see JudgmentIndexField#CC_REGION_AREA
 *  @see JudgmentIndexField#CC_APPEAL_AREA
 *  
 * @author Łukasz Dumiszewski
 */
@Service("ccCourtAreaFieldValueConverter")
public class CcCourtAreaFieldValueConverter {

    @Autowired
    private FieldValuePrefixAdder fieldValuePrefixAdder;
    
    
    /**
     * Converts ccCourtAreaFieldValue into {@link CcCourtArea}
     */
    public CcCourtArea convert(String ccCourtAreaFieldValue) {
        
        Preconditions.checkNotNull(ccCourtAreaFieldValue);
    
        String courtAreaStr = fieldValuePrefixAdder.extractFieldValue(ccCourtAreaFieldValue);
        
        String name = courtAreaStr.split(CcCourtAreaFieldValueCreator.CC_COURT_AREA_VALUE_PART_SEPARATOR)[0];
        
        long courtId = Long.parseLong(courtAreaStr.split(CcCourtAreaFieldValueCreator.CC_COURT_AREA_VALUE_PART_SEPARATOR)[1]);
        
        
        CcCourtArea ccCourtArea = new CcCourtArea();
        ccCourtArea.setCourtId(courtId);
        ccCourtArea.setName(name);
        
        return ccCourtArea;
        
    }

    
}
