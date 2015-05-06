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
	
	public List<SimpleEntity> createSimpleEntities() {
		SimpleEntity entityOne = new SimpleEntity();
		SimpleEntity divisionTwo = new SimpleEntity();
		entityOne.setId(1);
		entityOne.setName("Wydzial 1");
		divisionTwo.setId(23);
		divisionTwo.setName("Wydział 23 Karny");
		
		return Lists.newArrayList(entityOne, divisionTwo);
	}

	
	public List<SimpleCommonCourt> createSimpleCommonCourts() {
        SimpleCommonCourt entityOne = new SimpleCommonCourt();
        SimpleCommonCourt divisionTwo = new SimpleCommonCourt();
        entityOne.setId(1);
        entityOne.setName("Court 1");
        divisionTwo.setId(23);
        divisionTwo.setName("Court 23");
        
        return Lists.newArrayList(entityOne, divisionTwo);
    }
}
