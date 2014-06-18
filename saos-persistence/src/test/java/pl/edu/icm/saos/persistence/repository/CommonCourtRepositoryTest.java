package pl.edu.icm.saos.persistence.repository;

import javax.persistence.NoResultException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CommonCourt;

/**
 * @author Łukasz Dumiszewski
 */

public class CommonCourtRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private CommonCourtRepository commonCourtRepository;
    
    
    @Test
    public void saveAndGet() {
        Assert.assertEquals(0, commonCourtRepository.count());
        
        CommonCourt commonCourt = new CommonCourt();
        commonCourtRepository.save(commonCourt);
        
        Assert.assertEquals(1, commonCourtRepository.count());
        
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void findByCourtCode_InvalidCode() {
        commonCourtRepository.getOneByCode("1200000220");
    }

    
    @Test(expected=NoResultException.class)
    public void findByCourtCode_NoResultException() {
        CommonCourt commonCourt = commonCourtRepository.getOneByCode("15120000");
        Assert.assertNull(commonCourt);
        
    }
    

    @Test
    public void findByCourtCode() {
        CommonCourt commonCourt = new CommonCourt();
        commonCourt.setAppealCourtCode("12");
        commonCourt.setRegionalCourtCode("00");
        commonCourt.setDistrictCourtCode("00");
        commonCourtRepository.save(commonCourt);
        
        CommonCourt dbCommonCourt = commonCourtRepository.getOneByCode("15120000");
        Assert.assertNotNull(dbCommonCourt);
        Assert.assertEquals(commonCourt, dbCommonCourt);
        
    }
    
}
