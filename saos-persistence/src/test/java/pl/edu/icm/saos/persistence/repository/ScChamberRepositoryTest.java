package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;

/**
 * @author Łukasz Dumiszewski
 */

@Category(SlowTest.class)
public class ScChamberRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private ScChamberRepository scChamberRepository;
    
    private SupremeCourtChamber scChamber;
    
    @Before
    public void before() {
        
        scChamber = createScChamber("chamber 2 xxmccmxxm");
        createScChamber("chamber 3 xxmmxxm");
    }
    
    
    @Test
    public void findOneByName_FOUND() {
        
        SupremeCourtChamber dbScChamber = scChamberRepository.findOneByName(scChamber.getName());
        
        assertEquals(scChamber.getId(), dbScChamber.getId());
    }


    @Test
    public void findOneByFullName_NOT_FOUND() {
        
        SupremeCourtChamber dbScChamber = scChamberRepository.findOneByName(scChamber.getName()+"xxx");
        
        assertNull(dbScChamber);
    }


    //------------------------ PRIVATE --------------------------
    
    private SupremeCourtChamber createScChamber(String scChamberName) {
        SupremeCourtChamber scChamber = new SupremeCourtChamber();
        scChamber.setName(scChamberName);
        scChamberRepository.save(scChamber);
        return scChamber;
    }
}
