package pl.edu.icm.saos.webapp.division;

import java.util.List;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

import com.google.common.collect.Lists;

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
		List<SimpleDivision> simpleDivisions = Lists.newArrayList();
		
		for (CommonCourtDivision ccDivision : ccDivisions) {
			simpleDivisions.add(convert(ccDivision));
		}
		
		return simpleDivisions;
	}
	
	
}
