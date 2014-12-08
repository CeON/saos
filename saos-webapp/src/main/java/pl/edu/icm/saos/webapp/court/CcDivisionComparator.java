package pl.edu.icm.saos.webapp.court;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.webapp.common.RomanNumberConverter;

/**
 * @author Łukasz Pawełczak
 *
 */
public class CcDivisionComparator extends DivisionComparator<CommonCourtDivision> {

	
	//------------------------ LOGIC --------------------------
	
	@Override
	protected String getName(CommonCourtDivision division) {
		return division.getName();
	}
	
	/* Get arabic number from division name. Division name must contain roman as first word, if not method returns 0. */
	@Override
	protected int getNumber(String name) {
		String[] array = name.split(" ", 2);
		
		if (array.length < 2 || !RomanNumberConverter.isRomanNumber(array[0])) {
			return 0;
		} else {
			return RomanNumberConverter.decode(array[0]);
		}
	}
	
}
