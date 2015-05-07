package pl.edu.icm.saos.webapp.court;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class SimpleCommonCourtConverterTest {
    
    private String[] commonCourtNames = {"Sąd Okręgowy Warszawa-Praga w Warszawie", "Sąd Apelacyjny w Rzeszowie"};
    
    private SimpleCommonCourtConverter simpleCommonCourtConverter = new SimpleCommonCourtConverter();
    
    
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void convertCommonCourts_emptyList() {
        
        // given
        List<CommonCourt> commonCourts = Lists.newArrayList();
    
        // execute
        List<SimpleCommonCourt> convertedSimpleEntities = simpleCommonCourtConverter.convertCommonCourts(commonCourts);
    
        // assert
        assertNotNull(convertedSimpleEntities);
        assertEquals(0, convertedSimpleEntities.size());
    }
    
    
    @Test
    public void convertCommonCourts_NULL() {
        
        // execute
        List<SimpleCommonCourt> convertedSimpleEntities = simpleCommonCourtConverter.convertCommonCourts(null);
    
        // assert
        assertNotNull(convertedSimpleEntities);
        assertEquals(0, convertedSimpleEntities.size());
    }
    
    
    @Test
    public void convertCommonCourts() {
        
        // given
        CommonCourt commonCourtOne = new CommonCourt();
        commonCourtOne.setName(commonCourtNames[0]);
        commonCourtOne.setType(CommonCourtType.DISTRICT);
        Whitebox.setInternalState(commonCourtOne, "id", 83);
        
        CommonCourt commonCourtTwo = new CommonCourt();
        commonCourtTwo.setName(commonCourtNames[1]);
        commonCourtTwo.setType(CommonCourtType.APPEAL);
        Whitebox.setInternalState(commonCourtTwo, "id", 257);
    
        List<CommonCourt> commonCourts = Lists.newArrayList(commonCourtOne, commonCourtTwo);
    
        
        // execute
        List<SimpleCommonCourt> convertedSimpleEntities = simpleCommonCourtConverter.convertCommonCourts(commonCourts);
    
        
        // assert
        assertEquals(commonCourts.size(), convertedSimpleEntities.size());
        
        assertEquals(83, convertedSimpleEntities.get(0).getId());
        assertEquals(commonCourtNames[0], convertedSimpleEntities.get(0).getName());
        assertEquals(CommonCourtType.DISTRICT, convertedSimpleEntities.get(0).getType());
        
        assertEquals(257, convertedSimpleEntities.get(1).getId());
        assertEquals(commonCourtNames[1], convertedSimpleEntities.get(1).getName());
        assertEquals(CommonCourtType.APPEAL, convertedSimpleEntities.get(1).getType());
        
    }
}
