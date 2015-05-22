package pl.edu.icm.saos.search.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;

import pl.edu.icm.saos.persistence.model.CommonCourt;

/**
 * @author Łukasz Dumiszewski
 */

public class CcCourtAreaFieldValueCreatorTest {

    
    @InjectMocks
    private CcCourtAreaFieldValueCreator ccCourtAreaFieldValueCreator;
    
    @Mock
    private FieldValuePrefixAdder fieldValuePrefixAdder;
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected=NullPointerException.class)
    public void createCcCourtAreaFieldValue_NULL_Court() {
        
        
        // execute
        ccCourtAreaFieldValueCreator.createCcCourtAreaFieldValue(12L, null);
        
    }
    
    
    @Test
    public void createCcCourtAreaFieldValue() {
        
        // given
        CommonCourt court = new CommonCourt();
        Whitebox.setInternalState(court, "id", 12L);
        court.setName("Sąd Okręgowy w Lublinie");
        String sep = CcCourtAreaFieldValueCreator.CC_COURT_AREA_VALUE_PART_SEPARATOR;
        
        when(fieldValuePrefixAdder.addFieldPrefix("17", "Sąd Okręgowy w Lublinie"+sep+"12")).thenReturn("17_Sąd Okręgowy w Lublinie"+sep+"12");
        
        // execute
        String ccCourtArea = ccCourtAreaFieldValueCreator.createCcCourtAreaFieldValue(17L, court);
        
        // assert
        assertEquals("17_Sąd Okręgowy w Lublinie"+sep+"12", ccCourtArea);
        
    }
    
    
    @Test
    public void createCcCourtAreaFieldValue_parentAreaCourtId_NULL() {
        
        // given
        CommonCourt court = new CommonCourt();
        Whitebox.setInternalState(court, "id", 12L);
        court.setName("Sąd Okręgowy w Lublinie");
        String sep = CcCourtAreaFieldValueCreator.CC_COURT_AREA_VALUE_PART_SEPARATOR;
        String na = CcCourtAreaFieldValueCreator.NULL_PARENT_COURT_ID;
        
        when(fieldValuePrefixAdder.addFieldPrefix(na, "Sąd Okręgowy w Lublinie"+sep+"12")).thenReturn(na+"_Sąd Okręgowy w Lublinie"+sep+"12");
        
        // execute
        String ccCourtArea = ccCourtAreaFieldValueCreator.createCcCourtAreaFieldValue(null, court);
        
        // assert
        assertEquals(na+"_Sąd Okręgowy w Lublinie"+sep+"12", ccCourtArea);
        
    }
    
    
}
