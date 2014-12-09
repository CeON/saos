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
 * @author Łukasz Pawełczak
 *
 */
@Service
public class CcListService {

		
	private CommonCourtRepository commonCourtRepository;
	
	private CcDivisionRepository ccDivisionRepository;
	
	private SimpleDivisionConverter simpleDivisionConverter;
	    
		
	//------------------------ LOGIC --------------------------
	
	public List<CommonCourt> findCommonCourts() {
		
		List<CommonCourt> courts = commonCourtRepository.findAll();
		CommonCourtComparator commonCourtComparator = new CommonCourtComparator();
		
		Collections.sort(courts, commonCourtComparator);
		
		return courts;
	}
	
	public List<SimpleDivision> findCcDivisions(int courtId) {
		
		List<CommonCourtDivision> courtDivisions = ccDivisionRepository.findAllByCourtId(courtId);
		CcDivisionComparator ccDivisionComparator = new CcDivisionComparator();
		
		Collections.sort(courtDivisions, ccDivisionComparator);
		
		simpleDivisionConverter.getClass();
		
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
