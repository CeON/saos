package pl.edu.icm.saos.webapp.common.search;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.ui.ModelMap;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.webapp.court.CcListService;
import pl.edu.icm.saos.webapp.court.ScListService;
import pl.edu.icm.saos.webapp.court.SimpleCommonCourt;
import pl.edu.icm.saos.webapp.court.SimpleEntity;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class CourtDataModelCreatorTest {

    @InjectMocks
    private CourtDataModelCreator courtDataModelCreator;
    
    @Mock
    private CcListService ccListService;
    
    @Mock
    private ScListService scListService;

    @Mock
    private ModelMap model;
    
    private CourtCriteria courtCriteria = new CourtCriteria();
    
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void addCourtDataToModel_CourtCriteria_Null() {
        
        // execute
        courtDataModelCreator.addCourtDataToModel(null, model);
        
    }
    
    
    
    @Test(expected = NullPointerException.class)
    public void addCourtDataToModel_Model_Null() {
        
        // execute
        courtDataModelCreator.addCourtDataToModel(courtCriteria, null);
        
    }
    
    

    @Test
    public void addCourtDataToModel_CourtType_NOT_SET() {
        
        // execute
        courtDataModelCreator.addCourtDataToModel(courtCriteria, model);
        
        // assert
        verifyZeroInteractions(model, ccListService, scListService);
    }
    
    
    @Test
    public void addCourtDataToModel_CourtType_COMMON_CourtId_NULL() {
        
        // given
        courtCriteria.setCourtType(CourtType.COMMON);
        List<SimpleCommonCourt> commonCourts = createSimpleCommonCourts();
        when(ccListService.findCommonCourts()).thenReturn(commonCourts);
        
        // execute
        courtDataModelCreator.addCourtDataToModel(courtCriteria, model);
        
        // assert
        verify(model).addAttribute("commonCourts", commonCourts);
        verify(ccListService).findCommonCourts();
        verifyNoMoreInteractions(model, ccListService);
        verifyZeroInteractions(scListService);
    }
    
    

    @Test
    public void addCourtDataToModel_CourtType_COMMON_CourtId_NOT_NULL() {
        
        // given
        courtCriteria.setCourtType(CourtType.COMMON);
        courtCriteria.setCcCourtId(12L);
        
        List<SimpleCommonCourt> commonCourts = createSimpleCommonCourts();
        when(ccListService.findCommonCourts()).thenReturn(commonCourts);
        
        List<SimpleEntity> commonCourtDivisions = createSimpleEntities();
        when(ccListService.findCcDivisions(courtCriteria.getCcCourtId())).thenReturn(commonCourtDivisions);
        
        // execute
        courtDataModelCreator.addCourtDataToModel(courtCriteria, model);
        
        // assert
        verify(model).addAttribute("commonCourts", commonCourts);
        verify(model).addAttribute("commonCourtDivisions", commonCourtDivisions);
        verify(ccListService).findCommonCourts();
        verify(ccListService).findCcDivisions(courtCriteria.getCcCourtId());
        verifyNoMoreInteractions(model, ccListService);
        verifyZeroInteractions(scListService);
    }
    
    
    @Test
    public void addCourtDataToModel_CourtType_SUPREME_ChamberId_NULL() {
        
        // given
        courtCriteria.setCourtType(CourtType.SUPREME);
        List<SimpleEntity> supremeChambers = createSimpleEntities();
        when(scListService.findScChambers()).thenReturn(supremeChambers);
        
        // execute
        courtDataModelCreator.addCourtDataToModel(courtCriteria, model);
        
        // assert
        verify(model).addAttribute("supremeChambers", supremeChambers);
        verify(scListService).findScChambers();
        verifyNoMoreInteractions(model, scListService);
        verifyZeroInteractions(ccListService);
    }
    
    


    @Test
    public void addCourtDataToModel_CourtType_SUPREME_ChamberId_NOT_NULL() {
        
        // given
        courtCriteria.setCourtType(CourtType.SUPREME);
        courtCriteria.setScCourtChamberId(120L);
        
        List<SimpleEntity> supremeChambers = createSimpleEntities();
        when(scListService.findScChambers()).thenReturn(supremeChambers);
        
        List<SimpleEntity> supremeChamberDivisions = createSimpleEntities();
        when(scListService.findScChamberDivisions(courtCriteria.getScCourtChamberId())).thenReturn(supremeChamberDivisions);
        
        
        // execute
        courtDataModelCreator.addCourtDataToModel(courtCriteria, model);
        
        // assert
        verify(model).addAttribute("supremeChambers", supremeChambers);
        verify(model).addAttribute("supremeChamberDivisions", supremeChamberDivisions);
        verify(scListService).findScChambers();
        verify(scListService).findScChamberDivisions(courtCriteria.getScCourtChamberId());
        verifyNoMoreInteractions(model, scListService);
        verifyZeroInteractions(ccListService);
    }
    
    


    //------------------------ PRIVATE --------------------------
    
    private List<SimpleEntity> createSimpleEntities() {
        List<SimpleEntity> simpleEntities = Lists.newArrayList(mock(SimpleEntity.class), mock(SimpleEntity.class));
        return simpleEntities;
    }
    
    private List<SimpleCommonCourt> createSimpleCommonCourts() {
        List<SimpleCommonCourt> simpleEntities = Lists.newArrayList(mock(SimpleCommonCourt.class), mock(SimpleCommonCourt.class));
        return simpleEntities;
    }
    
}
