package pl.edu.icm.saos.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class CommonCourtHierarchyUpdaterTest extends PersistenceTestSupport {

    @Autowired
    private CommonCourtHierarchyUpdater commonCourtHierarchyUpdater;
    
    @Autowired
    private CommonCourtRepository commonCourtRepository;
    
    
    @Test
    public void updateHierarchy() {
        createCourt("15050000", CommonCourtType.APPEAL, "Sąd Apelacyjny 1");
        createCourt("15050500", CommonCourtType.REGIONAL, "Sąd Okręgowy 1_1");
        createCourt("15050505", CommonCourtType.DISTRICT, "Sąd Rejonowy 1_1_1");
        createCourt("15050510", CommonCourtType.DISTRICT, "Sąd Rejonowy 1_1_2");
        createCourt("15051000", CommonCourtType.REGIONAL, "Sąd Okręgowy 1_2");
        createCourt("15051010", CommonCourtType.DISTRICT, "Sąd Rejonowy 1_2_1");
        
        createCourt("15100000", CommonCourtType.APPEAL, "Sąd Apelacyjny 2");
        createCourt("15100500", CommonCourtType.REGIONAL, "Sąd Okręgowy 2_1");
        createCourt("15100505", CommonCourtType.DISTRICT, "Sąd Rejonowy 2_1_1");
        createCourt("15100510", CommonCourtType.DISTRICT, "Sąd Rejonowy 2_1_2");
        createCourt("15101000", CommonCourtType.REGIONAL, "Sąd Okręgowy 2_2");
        createCourt("15101010", CommonCourtType.DISTRICT, "Sąd Rejonowy 2_2_1");
        
        CommonCourt court = commonCourtRepository.findOneByCode("15050500");
        assertNotNull(court);
        assertNull(court.getParentCourt());
        
        commonCourtHierarchyUpdater.updateCommonCourtHierarchy();
        
        court = commonCourtRepository.findOneByCode("15050500");
        assertNotNull(court);
        assertNotNull(court.getParentCourt());
        
        CommonCourt parent = commonCourtRepository.findOneByCode("15050000");
        assertEquals(parent.getId(), court.getParentCourt().getId());
        
        
    }


    private void createCourt(String code, CommonCourtType type, String name) {
        CommonCourt court = new CommonCourt();
        court.setCode(code);
        court.setType(type);
        court.setName(name);
        commonCourtRepository.save(court);
        
    }
    
}
