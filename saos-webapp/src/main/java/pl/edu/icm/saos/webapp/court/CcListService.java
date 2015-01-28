package pl.edu.icm.saos.webapp.court;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;
import pl.edu.icm.saos.webapp.common.WebappConst;

/**
 * Service that finds and provides lists of common courts and common court divisions.   
 * @author Łukasz Pawełczak
 *
 */
@Service
public class CcListService {

		
	private CommonCourtRepository commonCourtRepository;
	
	private CcDivisionRepository ccDivisionRepository;
	
	private SimpleDivisionConverter simpleDivisionConverter;
	    
		
	//------------------------ LOGIC --------------------------
	
	/**
	 * Finds and returns all common courts.
	 * 
	 * @return list of {@link pl.edu.icm.saos.persistence.model.CommonCourt}
	 */
	public List<CommonCourt> findCommonCourts() {
		
		List<CommonCourt> courts = commonCourtRepository.findAll();
		
		Collections.sort(courts, new CommonCourtComparator());
		
		return courts;
	}
	
	/**
	 * Finds list of common court division by common court id. 
	 * Returned list is sorted with {@link CcDivisionComparator}.
	 * 
	 * @param courtId - common court id
	 * @return list of {@link pl.edu.icm.saos.persistence.model.CommonCourtDivision}
	 */
	public List<SimpleDivision> findCcDivisions(long courtId) {
		
		List<CommonCourtDivision> courtDivisions = ccDivisionRepository.findAllByCourtId(courtId);
		
		Collections.sort(courtDivisions, new CcDivisionComparator());
		
		return simpleDivisionConverter.convertCcDivisions(courtDivisions);
	}
	
	
	//------------------------ PRIVATE --------------------------
	
	private class CommonCourtComparator implements Comparator<CommonCourt> {
			
		public int compare(CommonCourt courtOne, CommonCourt courtTwo) {
			return Collator.getInstance(WebappConst.LOCALE_PL).compare(courtOne.getName(), courtTwo.getName());
		}  
	} 
	
	
	//------------------------ SETTERS --------------------------
	
	@Autowired
	public void setCommonCourtRepository(CommonCourtRepository ccRepository) {
		this.commonCourtRepository = ccRepository;
	}
	
	@Autowired
	public void setCcDivisionRepository(CcDivisionRepository ccDivisionRepository) {
		this.ccDivisionRepository = ccDivisionRepository;
	}
	
	@Autowired
	public void setSimpleDivisionConverter(SimpleDivisionConverter simpleDivisionConverter) {
		this.simpleDivisionConverter = simpleDivisionConverter;
	}

}
