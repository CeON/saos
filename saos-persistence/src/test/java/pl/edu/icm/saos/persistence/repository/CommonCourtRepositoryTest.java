package pl.edu.icm.saos.persistence.repository;

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
    
}
