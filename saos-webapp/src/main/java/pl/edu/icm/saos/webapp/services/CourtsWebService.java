package pl.edu.icm.saos.webapp.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;
import pl.edu.icm.saos.webapp.common.StringComparator;


/**
 * @author Łukasz Pawełczak
 *
 */
@Service
public class CourtsWebService {

	@Autowired
    private CommonCourtRepository commonCourtRepository;
	
	@Autowired
    private CcDivisionRepository ccDivisionRepository;
	
    @Autowired
    private ScChamberRepository scChamberRepository;
    
    @Autowired
    private ScChamberDivisionRepository scChamberDivisionRepository;
    
	
	//------------------------ LOGIC --------------------------
	
	public List<CommonCourt> getCommonCourts() {
		
		List<CommonCourt> courts = commonCourtRepository.findAll();
		CommonCourtComparator commonCourtComparator = new CommonCourtComparator();
		
		Collections.sort(courts, commonCourtComparator);
		
		return courts;
	}
	
	public List<CommonCourtDivision> getCcDivisions(int courtId) {
		
		List<CommonCourtDivision> divisions = ccDivisionRepository.findAllByCourtId(courtId);
		pl.edu.icm.saos.webapp.division.CcDivisionComparator ccDivisionComparator = new pl.edu.icm.saos.webapp.division.CcDivisionComparator();
		
		Collections.sort(divisions, ccDivisionComparator);
		
		return divisions;
	}
	
	public List<SupremeCourtChamber> getScChambers() {
		
		List<SupremeCourtChamber> chambers = scChamberRepository.findAll();
		ScChamberComparator scChamberComparator = new ScChamberComparator();
		
		Collections.sort(chambers, scChamberComparator);
		
		return chambers;
	}
	
	public List<SupremeCourtChamberDivision> getScChamberDivisions(int chamberId) {
		
		List<SupremeCourtChamberDivision> chamberDivisions = scChamberDivisionRepository.findAllByScChamberId(chamberId);
		pl.edu.icm.saos.webapp.division.ScChamberDivisionComparator scChamberDivisionComparator = new pl.edu.icm.saos.webapp.division.ScChamberDivisionComparator();
		
		Collections.sort(chamberDivisions, scChamberDivisionComparator);
		
		return chamberDivisions;
	}
	
	
	//------------------------ PRIVATE --------------------------
	
	private class CommonCourtComparator implements Comparator<CommonCourt> {
		
		public int compare(CommonCourt courtOne, CommonCourt courtTwo) {
            return StringComparator.compare(courtOne.getName(), courtTwo.getName());
        }  
	} 
	
	private class CcDivisionComparator implements Comparator<CommonCourtDivision> {
		
		public int compare(CommonCourtDivision divisionOne, CommonCourtDivision divisionTwo) {
			return StringComparator.compare(divisionOne.getName(), divisionTwo.getName());
        }  
	} 
	
	private class ScChamberComparator implements Comparator<SupremeCourtChamber> {
		
		public int compare(SupremeCourtChamber chamberOne, SupremeCourtChamber chamberTwo) {
			return StringComparator.compare(chamberOne.getName(), chamberTwo.getName());
        }
	}
	
	private class ScChamberDivisionComparator implements Comparator<SupremeCourtChamberDivision> {
		
		public int compare(SupremeCourtChamberDivision chamberDivisionOne, SupremeCourtChamberDivision chamberDivisionTwo) {
			return StringComparator.compare(chamberDivisionOne.getName(), chamberDivisionTwo.getName());
        }
	}
	
	
	//------------------------ SETTERS --------------------------
	
	//For unit tests
	public void setCommonCourtRepository(CommonCourtRepository ccRepository) {
		this.commonCourtRepository = ccRepository;
	}
	
	//For unit tests
	public void setCcDivisionRepository(CcDivisionRepository ccDivisionRepository) {
		this.ccDivisionRepository = ccDivisionRepository;
	}
	
	//For unit tests
	public void setScChamberRepository(ScChamberRepository scChamberRepository) {
		this.scChamberRepository = scChamberRepository;
	}
	
	//For unit tests
	public void setScChamberDivisionRepository(ScChamberDivisionRepository scChamberDivisionRepository) {
		this.scChamberDivisionRepository = scChamberDivisionRepository;
	}
}
