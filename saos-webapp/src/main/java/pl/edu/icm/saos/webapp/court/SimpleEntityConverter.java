package pl.edu.icm.saos.webapp.court;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

/**
 * Service for converting list of CommonCourtDivision into SimpleDivision list.  
 * 
 * @author Łukasz Pawełczak
 */
@Service
public class SimpleEntityConverter {
	
    	
    //------------------------ LOGIC --------------------------
    
    public List<SimpleEntity> convertCommonCourts(List<CommonCourt> commonCourts) {
        return commonCourts.stream()
        	.map(commonCourt -> convertFromCommonCourt(commonCourt))
        	.collect(Collectors.toList());
    }
        
    public List<SimpleEntity> convertCcDivisions(List<CommonCourtDivision> ccDivisions) {
        return ccDivisions.stream()
        	.map(ccDivision -> convertFromCcDivision(ccDivision))
        	.collect(Collectors.toList());
    }
    
    public List<SimpleEntity> convertScChambers(List<SupremeCourtChamber> scChambers) {
        return scChambers.stream()
        	.map(scChamber -> convertFromScChamber(scChamber))
        	.collect(Collectors.toList());
    }
    
    public List<SimpleEntity> convertScChamberDivisions(List<SupremeCourtChamberDivision> scChamberDivisions) {
        return scChamberDivisions.stream()
        	.map(scChamberDivision -> convertFromScChamberDivision(scChamberDivision))
        	.collect(Collectors.toList());
    }
    
    public List<SimpleEntity> convertScJudgmentForms(List<SupremeCourtJudgmentForm> scJudgmentForms) {
	return scJudgmentForms.stream()
		.map(scJudgmentForm -> convertFromScJudgmentForm(scJudgmentForm))
		.collect(Collectors.toList());
    }
    	
    	
    //------------------------ PRIVATE --------------------------
    	
    private SimpleEntity convertFromCommonCourt(CommonCourt commonCourt) {
        SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.setId(commonCourt.getId());
        simpleEntity.setName(commonCourt.getName());
        return simpleEntity;
    }
    	
    private SimpleEntity convertFromCcDivision(CommonCourtDivision commonCourtDivision) {
        SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.setId(commonCourtDivision.getId());
        simpleEntity.setName(commonCourtDivision.getName());
        return simpleEntity;
    }
    
    private SimpleEntity convertFromScChamber(SupremeCourtChamber scChamber) {
        SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.setId(scChamber.getId());
        simpleEntity.setName(scChamber.getName());
        return simpleEntity;
    }
    
    private SimpleEntity convertFromScChamberDivision(SupremeCourtChamberDivision supremeCourtChamberDivision) {
        SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.setId(supremeCourtChamberDivision.getId());
        simpleEntity.setName(supremeCourtChamberDivision.getName());
        return simpleEntity;
    }
    
    private SimpleEntity convertFromScJudgmentForm(SupremeCourtJudgmentForm supremeCourtJudgmentForm) {
        SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.setId(supremeCourtJudgmentForm.getId());
        simpleEntity.setName(supremeCourtJudgmentForm.getName());
        return simpleEntity;
    }
}
