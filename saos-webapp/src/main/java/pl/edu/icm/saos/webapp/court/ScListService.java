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
 * @author Łukasz Pawełczak
 *
 */
@Service
public class ScListService {

	
	private ScChamberRepository scChamberRepository;
	
	private ScChamberDivisionRepository scChamberDivisionRepository;
	
	private SimpleDivisionConverter simpleDivisionConverter;
	
	
	//------------------------ LOGIC --------------------------
	
	public List<SupremeCourtChamber> findScChambers() {
		
		List<SupremeCourtChamber> chambers = scChamberRepository.findAll();
		ScChamberComparator scChamberComparator = new ScChamberComparator();
		
		Collections.sort(chambers, scChamberComparator);
		
		return chambers;
	}
	
	public List<SimpleDivision> findScChamberDivisions(int chamberId) {
	
		List<SupremeCourtChamberDivision> chamberDivisions = scChamberDivisionRepository.findAllByScChamberId(chamberId);
		ScChamberDivisionComparator scChamberDivisionComparator = new ScChamberDivisionComparator();
		
		Collections.sort(chamberDivisions, scChamberDivisionComparator);
		
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
