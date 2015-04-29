package pl.edu.icm.saos.webapp.common.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.webapp.court.CcListService;
import pl.edu.icm.saos.webapp.court.ScListService;

import com.google.common.base.Preconditions;

/**
 * Creator of model court data.
 * 
 * @author Łukasz Dumiszewski
 */
@Service("courtDataModelCreator")
public class CourtDataModelCreator {

    
    private CcListService ccListService;
    
    private ScListService scListService;

    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Adds court data to the given model. Which court data will be added depends on the passed
     * court criteria. <br/>
     * The added data can be: <br/>
     * <ul>
     * <li> list of common courts, key: commonCourts, value: {@link CcListService#findCommonCourts()} </li>
     * <li> list of common court divisions, key: commonCourtDivisions, value: {@link CcListService#findCcDivisions(long)} </li>
     * <li> list of supreme court chambers, key: supremeChambers, value: {@link ScListService#findScChambers()} </li>
     * <li> list of supreme court chamber divisions, key: supremeChamberDivisions, value: {@link ScListService#findScChamberDivisions(long)} </li>
     * </ul> 
     */
    public void addCourtDataToModel(CourtCriteria courtCriteria, ModelMap model) {
        
        Preconditions.checkNotNull(courtCriteria);
        Preconditions.checkNotNull(model);
        
        addCommonCourtsToModel(courtCriteria, model);
        
        addSupremeCourtChambersToModel(courtCriteria, model);
        
    }
    
    
    
    //------------------------ PRIVATE --------------------------
    
    private void addCommonCourtsToModel(CourtCriteria courtCriteria, ModelMap model) {
        
        if (CourtType.COMMON.equals(courtCriteria.getCourtType())) {
            
            model.addAttribute("commonCourts", ccListService.findCommonCourts());
            
            if (courtCriteria.getCcCourtId() != null) {
                model.addAttribute("commonCourtDivisions", ccListService.findCcDivisions(courtCriteria.getCcCourtId()));
            }
        }
    }
    
    private void addSupremeCourtChambersToModel(CourtCriteria courtCriteria, ModelMap model) {
        
        if (CourtType.SUPREME.equals(courtCriteria.getCourtType())) {
            
            model.addAttribute("supremeChambers", scListService.findScChambers());
            
            if (courtCriteria.getScCourtChamberId() != null) {
                model.addAttribute("supremeChamberDivisions", scListService.findScChamberDivisions(courtCriteria.getScCourtChamberId()));
            }
        }
    }


    //------------------------ SETTERS --------------------------

    @Autowired
    public void setCcListService(CcListService ccListService) {
        this.ccListService = ccListService;
    }

    @Autowired
    public void setScListService(ScListService scListService) {
        this.scListService = scListService;
    }
    
}
