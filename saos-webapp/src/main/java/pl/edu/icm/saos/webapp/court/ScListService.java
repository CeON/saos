package pl.edu.icm.saos.webapp.court;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;
import pl.edu.icm.saos.webapp.common.WebappConst;


/**
 * Service that provides methods for finding lists of supreme courts
 * and supreme court division chambers.   
 * @author Łukasz Pawełczak
 *
 */
@Service
public class ScListService {

	
	private ScChamberRepository scChamberRepository;
	
	private ScChamberDivisionRepository scChamberDivisionRepository;
	
	private SimpleDivisionConverter simpleDivisionConverter;
	
	
	//------------------------ LOGIC --------------------------
	
	/**
	 * Find all supreme court chambers. Returned list is sorted by {@link ScChamberComparator}.
	 * 
	 * @return list of {@link pl.edu.icm.saos.persistence.model.SupremeCourtChamber}
	 */
	public List<SupremeCourtChamber> findScChambers() {
		
		List<SupremeCourtChamber> chambers = scChamberRepository.findAll();
		
		Collections.sort(chambers, new ScChamberComparator());
		
		return chambers;
	}
	
	/**
	 * Find all supreme court chamber divisions by supreme court chamber id.
	 * Returned list is sorted by {@link ScChamberDivisionComparator}.
	 * 
	 * @param chamberId - chamber division id
	 * @return list of {@link pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision}
	 */
	public List<SimpleDivision> findScChamberDivisions(int chamberId) {
	
		List<SupremeCourtChamberDivision> chamberDivisions = scChamberDivisionRepository.findAllByScChamberId(chamberId);
		
		Collections.sort(chamberDivisions, new ScChamberDivisionComparator());
		
		return simpleDivisionConverter.convertScChamberDivisions(chamberDivisions);
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
	public void setSimpleDivisionConverter(SimpleDivisionConverter simpleDivisionConverter) {
		this.simpleDivisionConverter = simpleDivisionConverter;
	}

}
