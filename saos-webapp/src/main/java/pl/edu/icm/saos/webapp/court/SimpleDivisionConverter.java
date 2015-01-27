package pl.edu.icm.saos.webapp.court;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;

/**
 * Service for converting list of CommonCourtDivision into SimpleDivision list.  
 * 
 * @author Łukasz Pawełczak
 */
@Service
public class SimpleDivisionConverter {
	
	
	//------------------------ LOGIC --------------------------
	
	public List<SimpleDivision> convertCcDivisions(List<CommonCourtDivision> ccDivisions) {
		return ccDivisions.stream()
			.map(ccDivision -> convertFromCcDivision(ccDivision))
			.collect(Collectors.toList());
	}
	
	public List<SimpleDivision> convertScChamberDivisions(List<SupremeCourtChamberDivision> scChamberDivisions) {
		return scChamberDivisions.stream()
			.map(scChamberDivision -> convertFromScChamberDivision(scChamberDivision))
			.collect(Collectors.toList());
	}
	
	
	//------------------------ PRIVATE --------------------------
	
	private SimpleDivision convertFromCcDivision(CommonCourtDivision commonCourtDivision) {
		SimpleDivision simpleDivision = new SimpleDivision();
		simpleDivision.setId(Long.toString(commonCourtDivision.getId())); // TODO: why SimpleDivision.id is String, not long?
		simpleDivision.setName(commonCourtDivision.getName());
		return simpleDivision;
	}

	private SimpleDivision convertFromScChamberDivision(SupremeCourtChamberDivision supremeCourtChamberDivision) {
		SimpleDivision simpleDivision = new SimpleDivision();
		simpleDivision.setId(Long.toString(supremeCourtChamberDivision.getId()));
		simpleDivision.setName(supremeCourtChamberDivision.getName());
		return simpleDivision;
	}	
}
