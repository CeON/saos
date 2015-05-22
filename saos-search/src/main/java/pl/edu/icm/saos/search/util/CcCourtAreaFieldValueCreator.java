package pl.edu.icm.saos.search.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.base.Preconditions;

/**
 *  Creator of common court area field values
 *  
 *  @see JudgmentIndexField#CC_DISTRICT_AREA
 *  @see JudgmentIndexField#CC_REGION_AREA
 *  @see JudgmentIndexField#CC_APPEAL_AREA
 *  
 * @author Łukasz Dumiszewski
 */
@Service("ccCourtAreaFieldValueCreator")
public class CcCourtAreaFieldValueCreator {

    
    public static final String CC_COURT_AREA_VALUE_PART_SEPARATOR = "###";
    
    public static final String NULL_PARENT_COURT_ID = "NA";
    
    
    @Autowired
    private FieldValuePrefixAdder fieldValuePrefixAdder;
    
    
        
    /**
     * Creates common court area field value string. The string will hold 
     * {@link CommonCourt#getName()} and {@link CommonCourt#getId()} prefixed with parentAreaCourtId. <br/>
     * If the parentAreaCourtId == null then {@link #NULL_PARENT_COURT_ID} will be added as the parentAreaCourtId.
     *  
     * @see CcCourtAreaFieldValueConverter#extractCourtId(String)
     * @see CcCourtAreaFieldValueConverter#extractAreaName(String)
     */
    public String createCcCourtAreaFieldValue(Long parentAreaCourtId, CommonCourt ccCourt) {
        
        Preconditions.checkNotNull(ccCourt);
        
        String ccCourtArea =  ccCourt.getName() + CC_COURT_AREA_VALUE_PART_SEPARATOR + ccCourt.getId();
        
        String parentAreaCourtIdStr = calcParentAreaCourtId(parentAreaCourtId);
            
        ccCourtArea = fieldValuePrefixAdder.addFieldPrefix(parentAreaCourtIdStr, ccCourtArea);
        
        return ccCourtArea;
        
    }

    
    
    //------------------------ PRIVATE --------------------------

    
    private String calcParentAreaCourtId(Long parentAreaCourtId) {
        
        String parentAreaCourtIdStr = NULL_PARENT_COURT_ID;
        
        if (parentAreaCourtId != null) {
            parentAreaCourtIdStr = "" + parentAreaCourtId;
        }
        
        return parentAreaCourtIdStr;
    }
    
   
}
