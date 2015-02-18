package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class MeansOfAppealRepositoryTest extends PersistenceTestSupport {

    @Autowired
    private MeansOfAppealRepository meansOfAppealRepository;
    
    
    //------------------------ TEST --------------------------
    
    @Test
    public void findOneByCourtTypeAndNameIgnoreCase_FOUND() {
        // given
        MeansOfAppeal meansOfAppeal = createAndSaveMeansOfAppeal(CourtType.SUPREME, "text");
        
        // execute
        MeansOfAppeal retMeansOfAppeal = meansOfAppealRepository.findOneByCourtTypeAndNameIgnoreCase(CourtType.SUPREME, "text");
        
        // assert
        assertEquals(meansOfAppeal, retMeansOfAppeal);
    }
    
    @Test
    public void findOneByCourtTypeAndNameIgnoreCase_FOUND_IGNORED_CASE() {
        // given
        MeansOfAppeal meansOfAppeal = createAndSaveMeansOfAppeal(CourtType.SUPREME, "text");
        
        // execute
        MeansOfAppeal retMeansOfAppeal = meansOfAppealRepository.findOneByCourtTypeAndNameIgnoreCase(CourtType.SUPREME, "TeXt");
        
        // assert
        assertEquals(meansOfAppeal, retMeansOfAppeal);
    }
    
    @Test
    public void findOneByCourtTypeAndNameIgnoreCase_NOT_FOUND() {
        // given
        createAndSaveMeansOfAppeal(CourtType.SUPREME, "text");
        createAndSaveMeansOfAppeal(CourtType.COMMON, "text2");
        
        // execute
        MeansOfAppeal retMeansOfAppeal = meansOfAppealRepository.findOneByCourtTypeAndNameIgnoreCase(CourtType.COMMON, "text");
        
        // assert
        assertNull(retMeansOfAppeal);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private MeansOfAppeal createAndSaveMeansOfAppeal(CourtType courtType, String name) {
        MeansOfAppeal meansOfAppeal = new MeansOfAppeal(courtType, name);
        meansOfAppealRepository.save(meansOfAppeal);
        
        return meansOfAppeal;
    }
    
}
