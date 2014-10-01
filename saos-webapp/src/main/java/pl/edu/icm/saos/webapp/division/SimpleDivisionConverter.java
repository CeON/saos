package pl.edu.icm.saos.webapp.division;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

/**
 * Service for converting list of CommonCourtDivision into SimpleDivision list.  
 * 
 * @author Łukasz Pawełczak
 */
@Service
public class SimpleDivisionConverter {
	
	
	public List<SimpleDivision> convertDivisions(List<CommonCourtDivision> ccDivisions) {
		return ccDivisions.stream()
			.map(ccDivision -> convert(ccDivision))
			.collect(Collectors.toList());
	}
	
	private SimpleDivision convert(CommonCourtDivision commonCourtDivision) {
		SimpleDivision simpleDivision = new SimpleDivision();
		simpleDivision.setId(Integer.toString(commonCourtDivision.getId()));
		simpleDivision.setName(commonCourtDivision.getName());
		return simpleDivision;
	}

}
