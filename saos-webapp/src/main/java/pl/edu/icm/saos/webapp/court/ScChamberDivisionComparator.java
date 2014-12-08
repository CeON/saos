package pl.edu.icm.saos.webapp.court;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.webapp.common.RomanNumberConverter;

/**
 * @author Łukasz Pawełczak
 *
 */
public class ScChamberDivisionComparator extends DivisionComparator<SupremeCourtChamberDivision> {

	
	//------------------------ LOGIC --------------------------
	
	@Override
	protected String getName(SupremeCourtChamberDivision division) {
		return division.getName();
	}
	
	/* Get arabic number from chamber division name. Division name must contain roman as last word, if not method returns 0. */
	@Override
	protected int getNumber(String name) {
		
		String lastWord = name.substring(name.lastIndexOf(" ") + 1);
		
		if (!RomanNumberConverter.isRomanNumber(lastWord) || lastWord.compareTo("") == 0) {
			return 0;
		} else {
			return RomanNumberConverter.decode(lastWord);
		}
		
	}
}
