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
    public void testSaveAndGet() {
        Assert.assertEquals(0, commonCourtRepository.count());
        
        CommonCourt commonCourt = new CommonCourt();
        commonCourtRepository.save(commonCourt);
        
        Assert.assertEquals(1, commonCourtRepository.count());
        
    }
    
}
