package pl.edu.icm.saos.persistence.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class ScChamberDivisionRepositoryTest extends PersistenceTestSupport {

    @Autowired
    private ScChamberDivisionRepository scChamberDivisionRepository;
    
    @Autowired
    private ScChamberRepository scChamberRepository;
    
    private SupremeCourtChamberDivision division1;
    
    private SupremeCourtChamber chamberOne;
    
    private SupremeCourtChamberDivision chamberDivisionOne;
    
    @Before
    public void before() {
        
        division1 = createScChamberDivision("division 1 xxmmxxm");
        createScChamberDivision("division 2 xxmccmxxm");
        createScChamberDivision("division 3 xxmmxxm");
        
        initScChamberForTests(); //adds one chamber division
    }
    
    
    @Test
    public void deleteAll() {
        assertEquals(4, scChamberDivisionRepository.count());
        scChamberDivisionRepository.deleteAll();
        assertEquals(0, scChamberDivisionRepository.count());
    }
    
    @Test
    public void findOneByFullName_FOUND() {
        
        SupremeCourtChamberDivision dbScDivision = scChamberDivisionRepository.findOneByFullName(division1.getFullName());
        
        assertEquals(division1.getId(), dbScDivision.getId());
    }

    @Test
    public void findOneByFullName_NOT_FOUND() {
        
        SupremeCourtChamberDivision dbScDivision = scChamberDivisionRepository.findOneByFullName(division1.getFullName()+"xxx");
        
        assertNull(dbScDivision);
    }

    @Test
    public void findAllByScChamberId_FOUND() {
    	List<SupremeCourtChamberDivision> scChamberDivisions = scChamberDivisionRepository.findAllByScChamberId(chamberOne.getId());
    	
    	assertEquals(1, scChamberDivisions.size());
    	assertEquals(chamberDivisionOne.getId(), scChamberDivisions.get(0).getId());
    }
    
    @Test
    public void findAllByScChamberId_NOT_FOUND() {
    	List<SupremeCourtChamberDivision> emptyChamberDivisions = scChamberDivisionRepository.findAllByScChamberId(77);
    	
	    assertNotNull(emptyChamberDivisions);
	    assertTrue(emptyChamberDivisions.isEmpty());
    }

    //------------------------ PRIVATE --------------------------
    
    private SupremeCourtChamberDivision createScChamberDivision(String divisionFullName) {
        SupremeCourtChamber scChamber = new SupremeCourtChamber();
        scChamber.setName(divisionFullName);
        scChamberRepository.save(scChamber);
        
        SupremeCourtChamberDivision division = new SupremeCourtChamberDivision();
        division.setFullName(divisionFullName);
        division.setName(divisionFullName);
        division.setScChamber(scChamber);
        scChamberDivisionRepository.save(division);
        return division;
    }
    
    private void initScChamberForTests() {
    	chamberOne = new SupremeCourtChamber();
    	chamberOne.setName("zxc");
    	scChamberRepository.save(chamberOne);
    	
    	chamberDivisionOne = new SupremeCourtChamberDivision();
    	chamberDivisionOne.setFullName("Wydział karny I");
    	chamberDivisionOne.setName("Wydział karny I");
    	chamberDivisionOne.setScChamber(chamberOne);
        scChamberDivisionRepository.save(chamberDivisionOne);
    }
}
