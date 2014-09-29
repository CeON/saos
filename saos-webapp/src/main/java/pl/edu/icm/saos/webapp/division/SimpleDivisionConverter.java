package pl.edu.icm.saos.webapp.division;

import java.util.List;
import java.util.stream.Collectors;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

/**
 * @author Łukasz Pawełczak
 */
public final class SimpleDivisionConverter {
	
	public static SimpleDivision convert(CommonCourtDivision commonCourtDivision) {
		SimpleDivision simpleDivision = new SimpleDivision();
		simpleDivision.setId(Integer.toString(commonCourtDivision.getId()));
		simpleDivision.setName(commonCourtDivision.getName());
		return simpleDivision;
	}
	
	public static List<SimpleDivision> convertDivisions(List<CommonCourtDivision> ccDivisions) {
		return ccDivisions.stream()
			.map(ccDivision -> convert(ccDivision))
			.collect(Collectors.toList());
	}

}
