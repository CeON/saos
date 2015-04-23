package pl.edu.icm.saos.webapp.court;

import static pl.edu.icm.saos.webapp.common.CacheNames.DICTIONARIES;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentFormRepository;
import pl.edu.icm.saos.webapp.common.WebappConst;


/**
 * Service that provides methods for finding lists of supreme courts,
 * supreme court division chambers and supreme court judgment forms.   
 * 
 * @author Łukasz Pawełczak
 *
 */
@Service
public class ScListService {

	
	private ScChamberRepository scChamberRepository;
	
	private ScChamberDivisionRepository scChamberDivisionRepository;
	
	private ScJudgmentFormRepository scJudgmentFormRepository;
	
	private SimpleEntityConverter simpleEntityConverter;
	
	
	//------------------------ LOGIC --------------------------
	
	/**
	 * Finds all supreme court chambers. Returned list is sorted by {@link ScChamberComparator}.
	 * 
	 * @return list of {@link pl.edu.icm.saos.webapp.court.SimpleEntity}
	 */
	@Cacheable(DICTIONARIES)
	public List<SimpleEntity> findScChambers() {
		
		List<SupremeCourtChamber> chambers = scChamberRepository.findAll();
		
		Collections.sort(chambers, new ScChamberComparator());
		
		return simpleEntityConverter.convertScChambers(chambers);
	}
	
	/**
	 * Finds all supreme court chamber divisions by supreme court chamber id.
	 * Returned list is sorted by {@link ScChamberDivisionComparator}.
	 * 
	 * @param chamberId - chamber division id
	 * @return list of {@link pl.edu.icm.saos.webapp.court.SimpleEntity}
	 */
	@Cacheable(DICTIONARIES)
    public List<SimpleEntity> findScChamberDivisions(long chamberId) {
	    
		List<SupremeCourtChamberDivision> chamberDivisions = scChamberDivisionRepository.findAllByScChamberId(chamberId);
		
		Collections.sort(chamberDivisions, new ScChamberDivisionComparator());
		
		return simpleEntityConverter.convertScChamberDivisions(chamberDivisions);
	}
	
	/**
	 * Finds all supreme court judgment forms.
	 * 
	 * @return list of {@link pl.edu.icm.saos.webapp.court.SimpleEntity}
	 */
	@Cacheable(DICTIONARIES)
    public List<SimpleEntity> findScJudgmentForms() {
	    	return simpleEntityConverter.convertScJudgmentForms(scJudgmentFormRepository.findAll());
	}
	
	
	
	
	//------------------------ PRIVATE --------------------------
	
	private class ScChamberComparator implements Comparator<SupremeCourtChamber> {
		
		public int compare(SupremeCourtChamber chamberOne, SupremeCourtChamber chamberTwo) {
			return Collator.getInstance(WebappConst.LOCALE_PL).compare(chamberOne.getName(), chamberTwo.getName());
		}
	}
	
	
	//------------------------ SETTERS --------------------------
	
	@Autowired
	public void setScChamberRepository(ScChamberRepository scChamberRepository) {
		this.scChamberRepository = scChamberRepository;
	}
	
	@Autowired
	public void setScChamberDivisionRepository(ScChamberDivisionRepository scChamberDivisionRepository) {
		this.scChamberDivisionRepository = scChamberDivisionRepository;
	}
	
	@Autowired
	public void setScJudgmentFormRepository(ScJudgmentFormRepository scJudgmentFormRepository) {
	    	this.scJudgmentFormRepository = scJudgmentFormRepository;
	}
	
	@Autowired
	public void setSimpleEntityConverter(SimpleEntityConverter simpleEntityConverter) {
		this.simpleEntityConverter = simpleEntityConverter;
	}

   
}
