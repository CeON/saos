package pl.edu.icm.saos.importer.commoncourt.court;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

/**
 * @author Łukasz Dumiszewski
 */

@Service("commonCourtOverwriter")
class CommonCourtOverwriter {

    
    /**
     * Overwrites oldCommonCourt data with newCommonCourt data.</br>
     * Does not overwrite fields inherited from {@link DataObject} and {@link CommonCourt#getCode()}, {@link CommonCourtDivision#getCode()},
     * {@link CommonCourt#getParentCourt()}.</br>
     * Divisions that are present in oldCommonCourt and are missing in newCommonCourt are marked as inactive,
     * see: {@link CommonCourtDivision#markInactive()}. 
     * 
     */
    public void overwrite(CommonCourt oldCommonCourt, CommonCourt newCommonCourt) {
        
        oldCommonCourt.setName(newCommonCourt.getName());
        oldCommonCourt.setType(newCommonCourt.getType());
                
        overwriteDepartments(oldCommonCourt, newCommonCourt);
        
    }

    
    //------------------------ PRIVATE --------------------------
    
    
    private void overwriteDepartments(CommonCourt oldCommonCourt, CommonCourt newCommonCourt) {
        for (CommonCourtDivision oldDivision : oldCommonCourt.getDivisions()) {
            if (!newCommonCourt.hasDivision(oldDivision.getCode())) {
                oldDivision.markInactive();
            }
        }
        
        for (CommonCourtDivision newDivision : newCommonCourt.getDivisions()) {
            CommonCourtDivision division = oldCommonCourt.getDivision(newDivision.getCode());
            if (division == null) {
                division = new CommonCourtDivision();
                oldCommonCourt.addDivision(division);
            }
            division.setCode(newDivision.getCode());
            division.setName(newDivision.getName());
            division.setType(newDivision.getType());
        }
    }
    
}
