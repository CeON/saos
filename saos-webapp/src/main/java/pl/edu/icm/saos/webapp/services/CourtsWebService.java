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
		CommonCourtComparator<CommonCourt> commonCourtComparator = new CommonCourtComparator<CommonCourt>();
		
		Collections.sort(courts, commonCourtComparator);
		
		return courts;
	}
	
	public List<CommonCourtDivision> getCcDivisions(int courtId) {
		
		List<CommonCourtDivision> divisions = ccDivisionRepository.findAllByCourtId(courtId);
		pl.edu.icm.saos.webapp.division.CcDivisionComparator<CommonCourtDivision> ccDivisionComparator = new pl.edu.icm.saos.webapp.division.CcDivisionComparator<CommonCourtDivision>();
		
		Collections.sort(divisions, ccDivisionComparator);
		
		return divisions;
	}
	
	public List<SupremeCourtChamber> getScChambers() {
		
		List<SupremeCourtChamber> chambers = scChamberRepository.findAll();
		ScChamberComparator<SupremeCourtChamber> scChamberComparator = new ScChamberComparator<SupremeCourtChamber>();
		
		Collections.sort(chambers, scChamberComparator);
		
		return chambers;
	}
	
	public List<SupremeCourtChamberDivision> getScChamberDivisions(int chamberId) {
		
		List<SupremeCourtChamberDivision> chamberDivisions = scChamberDivisionRepository.findAllByScChamberId(chamberId);
		pl.edu.icm.saos.webapp.division.ScChamberDivisionComparator<SupremeCourtChamberDivision> scChamberDivisionComparator = new pl.edu.icm.saos.webapp.division.ScChamberDivisionComparator<SupremeCourtChamberDivision>();
		
		Collections.sort(chamberDivisions, scChamberDivisionComparator);
		
		return chamberDivisions;
	}
	
	
	//------------------------ PRIVATE --------------------------
	
	private class CommonCourtComparator<T> implements Comparator<T> {
		
		public int compare(T courtOne, T courtTwo) {
            return StringComparator.compare(((CommonCourt) courtOne).getName(), ((CommonCourt) courtTwo).getName());
        }  
	} 
	
	private class CcDivisionComparator<T> implements Comparator<T> {
		
		public int compare(T divisionOne, T divisionTwo) {
			return StringComparator.compare(((CommonCourtDivision) divisionOne).getName(), ((CommonCourtDivision) divisionTwo).getName());
        }  
	} 
	
	private class ScChamberComparator<T> implements Comparator<T> {
		
		public int compare(T chamberOne, T chamberTwo) {
			return StringComparator.compare(((SupremeCourtChamber) chamberOne).getName(), ((SupremeCourtChamber) chamberTwo).getName());
        }
	}
	
	private class ScChamberDivisionComparator<T> implements Comparator<T> {
		
		public int compare(T chamberDivisionOne, T chamberDivisionTwo) {
			return StringComparator.compare(((SupremeCourtChamberDivision) chamberDivisionOne).getName(), ((SupremeCourtChamberDivision) chamberDivisionTwo).getName());
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
