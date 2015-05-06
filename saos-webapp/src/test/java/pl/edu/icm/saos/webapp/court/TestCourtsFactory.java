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
		SimpleEntity entityTwo = new SimpleEntity();
		entityOne.setId(1);
		entityOne.setName("Wydzial 1");
		entityTwo.setId(23);
		entityTwo.setName("Wydział 23 Karny");
		
		return Lists.newArrayList(entityOne, entityTwo);
	}

	
	public List<SimpleCommonCourt> createSimpleCommonCourts() {
        SimpleCommonCourt courtOne = new SimpleCommonCourt();
        SimpleCommonCourt courtTwo = new SimpleCommonCourt();
        courtOne.setId(1);
        courtOne.setName("Court 1");
        courtTwo.setId(23);
        courtTwo.setName("Court 23");
        
        return Lists.newArrayList(courtOne, courtTwo);
    }
}
