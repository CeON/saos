package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class CcDivisionTypeRepositoryTest extends PersistenceTestSupport {
    
    @Autowired
    private CcDivisionTypeRepository ccDivisionTypeRepository;
    
    
    @Test
    public void findByCode_NotFound() {
        assertNull(ccDivisionTypeRepository.findByCode("12"));
    }

    @Test
    public void findByCode_Found() {
        CommonCourtDivisionType ccDivisionType = new CommonCourtDivisionType();
        ccDivisionType.setCode("12");
        ccDivisionTypeRepository.save(ccDivisionType);
        assertNotNull(ccDivisionTypeRepository.findByCode("12"));
    }

}
