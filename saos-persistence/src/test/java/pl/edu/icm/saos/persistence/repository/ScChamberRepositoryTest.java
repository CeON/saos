package pl.edu.icm.saos.persistence.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Łukasz Dumiszewski
 */

@Category(SlowTest.class)
public class ScChamberRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private ScChamberRepository scChamberRepository;

    @Autowired
    private ScChamberDivisionRepository scDivisionRepository;
    
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

    @Test
    public void findOneAndInitialize__it_should_initialize_chambers_divisions_fields(){
        //given
        String divisionName = "some division name";
        String divisionFullName = "some division full name";

        createScDivision(divisionName, divisionFullName, scChamber);

        //when
        SupremeCourtChamber dbScChamber = scChamberRepository.findOneAndInitialize(scChamber.getId());

        //then
        SupremeCourtChamberDivision division = dbScChamber.getDivisions().get(0);
        assertEquals(divisionName, division.getName());
        assertEquals(divisionFullName, division.getFullName());
    }


    //------------------------ PRIVATE --------------------------
    
    private SupremeCourtChamber createScChamber(String scChamberName) {
        SupremeCourtChamber scChamber = new SupremeCourtChamber();
        scChamber.setName(scChamberName);
        scChamberRepository.save(scChamber);
        return scChamber;
    }

    private SupremeCourtChamberDivision createScDivision(String name, String fullName, SupremeCourtChamber scChamber){
        SupremeCourtChamberDivision scDivision = new SupremeCourtChamberDivision();
        scDivision.setName(name);
        scDivision.setFullName(fullName);

        scChamber.addDivision(scDivision);

        scDivisionRepository.save(scDivision);
        scChamberRepository.save(scChamber);

        return scDivision;
    }
}
