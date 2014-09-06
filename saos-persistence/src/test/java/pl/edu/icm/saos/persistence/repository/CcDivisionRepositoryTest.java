package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

/**
 * @author Ĺ�ukasz Dumiszewski
 */
@Category(SlowTest.class)
public class CcDivisionRepositoryTest extends PersistenceTestSupport {
    
    @Autowired
    private CcDivisionRepository ccDivisionRepository;
    
    @Autowired
    private CommonCourtRepository commonCourtRepository;
    
    private CommonCourt court = new CommonCourt();
    
    private CommonCourt courtEmpty = new CommonCourt();
    
    private List<CommonCourtDivision> courtDivisions = Lists.newArrayList();
    
    private Map<String, CommonCourt> commonCourtMap = new HashMap<String, CommonCourt>(); 
    
    @Before
    public void before() {
    	super.before();
    	initializeTests();
    }
    
    @Test
    public void findByCourtIdAndCode_NotFound() {
    	String code = "25";
    	
        assertNull(ccDivisionRepository.findOneByCourtIdAndCode(commonCourtMap.get(code).getId(), code));
    }

    @Test
    public void findByCourtIdAndCode_Found() {
        String code = "23";
        CommonCourt testCourt = commonCourtMap.get(code);
        
        CommonCourtDivision testDivision = ccDivisionRepository.findOneByCourtIdAndCode(testCourt.getId(), code); 
        
        assertNotNull(testDivision);
    	assertEquals(courtDivisions.get(0), testDivision);
    }

    @Test
    public void findAllByCourtId_NotFound() {
    	List<CommonCourtDivision> emptyDivisions = ccDivisionRepository.findAllByCourtId(commonCourtMap.get("25").getId());
    	
	     assertNotNull(emptyDivisions);
	     assertTrue(emptyDivisions.isEmpty());
    }
    
    @Test
    public void findAllByCourtId_Found() {
    	List<CommonCourtDivision> testDivisions = ccDivisionRepository.findAllByCourtId(commonCourtMap.get("23").getId()); 

    	assertNotNull(testDivisions);
    	assertEquals(courtDivisions.size(), testDivisions.size());
    	assertEquals(courtDivisions, testDivisions);
    }
    
    
    private void initializeTests() {
    	commonCourtMap.put("23", court);
    	commonCourtRepository.save(court);
        
        courtDivisions.add(createAndSaveCCDivision("23", court));
        courtDivisions.add(createAndSaveCCDivision("24", court));
        
        commonCourtMap.put("25", courtEmpty);
    	commonCourtRepository.save(courtEmpty);
    }
    
    private CommonCourtDivision createAndSaveCCDivision(String hashCode, CommonCourt court) {
    	CommonCourtDivision ccDivision = new CommonCourtDivision();
    	ccDivision.setCourt(court);
    	ccDivision.setCode(hashCode);
        ccDivisionRepository.save(ccDivision);
        return ccDivision;
    }
    

    
}