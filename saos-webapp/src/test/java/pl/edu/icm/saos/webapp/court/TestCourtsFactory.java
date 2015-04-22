package pl.edu.icm.saos.webapp.court;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

/**
 * 
 * @author Łukasz Pawełczak
 *
 */
@Service
public class TestCourtsFactory {

	
	//------------------------ LOGIC --------------------------
	
	public List<SimpleEntity> getSimpleEntities() {
		SimpleEntity divisionOne = new SimpleEntity();
		SimpleEntity divisionTwo = new SimpleEntity();
		divisionOne.setId(1);
		divisionOne.setName("Wydzial 1");
		divisionTwo.setId(23);
		divisionTwo.setName("Wydział 23 Karny");
		
		return Lists.newArrayList(divisionOne, divisionTwo);
	}

}
