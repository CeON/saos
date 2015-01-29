package pl.edu.icm.saos.webapp.court;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Łukasz Pawełczak
 *
 */
@Service
public class TestCourtsFactory {

	
	//------------------------ LOGIC --------------------------
	
	public List<SimpleDivision> getSimpleDivisions() {
		SimpleDivision divisionOne = new SimpleDivision();
		SimpleDivision divisionTwo = new SimpleDivision();
		divisionOne.setId(1);
		divisionOne.setName("Wydzial 1");
		divisionTwo.setId(23);
		divisionTwo.setName("Wydział 23 Karny");
		
		return Lists.newArrayList(divisionOne, divisionTwo);
	}

}
