package pl.edu.icm.saos.webapp.division;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.webapp.common.ConvertRomanNumerals;

/**
 * @author Łukasz Pawełczak
 *
 */
public class CcDivisionComparator<T> extends DivisionComparator<T> {

	@Override
	protected String getName(T division) {
		return ((CommonCourtDivision) division).getName();
	}
	
	/* Get arabic number from division name. Division name must contain roman as first word, if not method returns 0. */
	@Override
	protected int getNumber(String name) {
		String[] array = name.split(" ", 2);
		
		if (array.length < 2 || !ConvertRomanNumerals.isRomanNumeral(array[0])) {
			return 0;
		} else {
			return ConvertRomanNumerals.decode(array[0]);
		}
	}
	
}
