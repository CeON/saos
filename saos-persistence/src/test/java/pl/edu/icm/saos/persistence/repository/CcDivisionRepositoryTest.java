package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class CcDivisionRepositoryTest extends PersistenceTestSupport {
    
    @Autowired
    private CcDivisionRepository ccDivisionRepository;
    
    @Autowired
    private CommonCourtRepository commonCourtRepository;
    
    @Test
    public void findByCourtIdAndCode_NotFound() {
        assertNull(ccDivisionRepository.findOneByCourtIdAndCode(23, "12"));
    }

    @Test
    public void findByCourtIdAndCode_Found() {
        CommonCourt court = new CommonCourt();
        commonCourtRepository.save(court);
        
        CommonCourtDivision ccDivision = new CommonCourtDivision();
        ccDivision.setCourt(court);
        ccDivision.setCode("12");
        ccDivisionRepository.save(ccDivision);
        assertNotNull(ccDivisionRepository.findOneByCourtIdAndCode(ccDivision.getCourt().getId(), ccDivision.getCode()));
    }

}