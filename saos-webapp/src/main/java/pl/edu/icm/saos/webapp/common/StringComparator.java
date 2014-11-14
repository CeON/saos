package pl.edu.icm.saos.webapp.common;

import java.text.Collator;
import java.util.Locale;

/**
 * @author Łukasz Pawełczak
 *
 */
public class StringComparator {
	
	
	//------------------------ LOGIC --------------------------
	
	public static int compare(String stringOne, String stringTwo) {
		Collator collator = Collator.getInstance(new Locale("pl", "PL"));
		return collator.compare(stringOne, stringTwo);
	}
}
