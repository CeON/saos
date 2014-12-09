package pl.edu.icm.saos.webapp.court;

import java.text.Collator;
import java.util.Comparator;

import pl.edu.icm.saos.webapp.common.WebappConst;

/**
 * @author Łukasz Pawełczak
 *
 */
public abstract class DivisionComparator<T> implements Comparator<T> {

	
	//------------------------ LOGIC --------------------------
	
	/* Compare divisions by roman numbers contained in division name.
	 * If two division names does not contain roman number, compare it alphabetically. */ 
	public int compare(T divisionOne, T divisionTwo) {
		String divisionNameOne = getName(divisionOne); 
		String divisionNameTwo = getName(divisionTwo); 
		int romanNumberOne = getNumber(divisionNameOne);
		int romanNumberTwo = getNumber(divisionNameTwo);
		
		// if both names do not contain roman numbers, compare them using simple alphabetic comparation 
		if (romanNumberOne == 0 && romanNumberTwo == 0) {
			return Collator.getInstance(WebappConst.LOCALE_PL).compare(divisionNameOne, divisionNameTwo);
		} else {
			return (romanNumberOne - romanNumberTwo);
		}
	}  
	
	protected abstract String getName(T division);
	
	protected abstract int getNumber(String name);

}
