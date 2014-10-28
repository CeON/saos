package pl.edu.icm.saos.persistence.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class CommonCourtRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private CommonCourtRepository commonCourtRepository;

    @Autowired
    private CcDivisionTypeRepository ccDivisionTypeRepository;

    @Autowired
    private CcDivisionRepository ccDivisionRepository;
    
    
    @Test
    public void saveAndGet() {
        Assert.assertEquals(0, commonCourtRepository.count());
        
        CommonCourt commonCourt = new CommonCourt();
        commonCourt.setCode("XYZ");
        commonCourtRepository.save(commonCourt);
        
        Assert.assertEquals(1, commonCourtRepository.count());
        
    }
    
    
        
    @Test
    public void findOneByCode_NotFound() {
        CommonCourt commonCourt = commonCourtRepository.findOneByCode("15120000");
        Assert.assertNull(commonCourt);
        
    }
    

    @Test
    public void findByCourtCode() {
        CommonCourt commonCourt = new CommonCourt();
        commonCourt.setCode("15120000");
        commonCourtRepository.save(commonCourt);
        
        CommonCourt dbCommonCourt = commonCourtRepository.findOneByCode(commonCourt.getCode());
        Assert.assertNotNull(dbCommonCourt);
        Assert.assertEquals(commonCourt, dbCommonCourt);
        
    }

    @Test
    public void findOneAndInitialize(){
        //given
        CommonCourt commonCourt = new CommonCourt();
        commonCourt.setCode("15120000");
        commonCourt = commonCourtRepository.save(commonCourt);

        CommonCourtDivisionType divisionType = new CommonCourtDivisionType();
        divisionType.setCode("15");
        divisionType.setName("Wydzial Pracy");
        divisionType = ccDivisionTypeRepository.save(divisionType);

        CommonCourtDivision division = new CommonCourtDivision();
        String divisionName = "I Wydział Pracy";
        division.setCode("0000503");
        division.setName(divisionName);
        division.setType(divisionType);
        division.setCourt(commonCourt);
        ccDivisionRepository.save(division);

        //when
        CommonCourt actual = commonCourtRepository.findOneAndInitialize(commonCourt.getId());

        //then
        Assert.assertEquals(1, actual.getDivisions().size());
        Assert.assertEquals(divisionName, actual.getDivisions().get(0).getName());
    }
    
}
