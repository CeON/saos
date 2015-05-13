package pl.edu.icm.saos.webapp.analysis.request.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentGlobalFilter;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;

/**
 * @author Łukasz Dumiszewski
 */

public class CcCourtChartXSettingsGeneratorTest {

    @InjectMocks
    private CcCourtChartXSettingsGenerator xsettingsGenerator = new CcCourtChartXSettingsGenerator();
    
    @Mock
    private CommonCourtRepository commonCourtRepository;
    
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    
    
    @Test(expected = NullPointerException.class)
    public void handles_NULL() {
        
        // execute
        xsettingsGenerator.handles(null);
        
    }

    
    @Test
    public void handles_FALSE() {
        
        // execute
        xsettingsGenerator.handles(ChartCode.AGGREGATED_MAIN_CHART);
        
    }

    
    @Test
    public void handles_TRUE() {
        
        // execute
        xsettingsGenerator.handles(ChartCode.CC_COURT_CHART);
        
    }

    
    @Test(expected = NullPointerException.class)
    public void canGenerateXSettings_NULL() {
        
        // execute
        xsettingsGenerator.canGenerateXSettings(null);
        
    }
    
    
    @Test
    public void canGenerateXSettings_FALSE_EmptyCourtCriteria() {
        
        // given
        JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
        
        // execute
        assertFalse(xsettingsGenerator.canGenerateXSettings(globalFilter));
        
    }
    
    
    @Test
    public void canGenerateXSettings_FALSE_CourtTypeNotCommon() {
        
        // given
        JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
        globalFilter.getCourtCriteria().setCourtType(CourtType.CONSTITUTIONAL_TRIBUNAL);
        
        // execute
        assertFalse(xsettingsGenerator.canGenerateXSettings(globalFilter));
        
    }
    
    
    @Test
    public void canGenerateXSettings_FALSE_CourtTypeCommon_CourtIdNotNull_IncludeDependentFALSE() {
        
        // given
        JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
        globalFilter.getCourtCriteria().setCourtType(CourtType.COMMON);
        globalFilter.getCourtCriteria().setCcCourtId(12L);
        globalFilter.getCourtCriteria().setCcIncludeDependentCourtJudgments(false);
        
        // execute
        assertFalse(xsettingsGenerator.canGenerateXSettings(globalFilter));
        
    }
    
    @Test
    public void canGenerateXSettings_FALSE_CourtTypeCommon_CourtIdNotNull_IncludeDependentTRUE_District() {
        
        // given
        JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
        globalFilter.getCourtCriteria().setCourtType(CourtType.COMMON);
        globalFilter.getCourtCriteria().setCcCourtId(12L);
        globalFilter.getCourtCriteria().setCcIncludeDependentCourtJudgments(true);
        
        CommonCourt commonCourt = new CommonCourt();
        commonCourt.setType(CommonCourtType.DISTRICT);
        when(commonCourtRepository.findOne(12L)).thenReturn(commonCourt);
        
        // execute
        assertFalse(xsettingsGenerator.canGenerateXSettings(globalFilter));
        
    }
    
    @Test
    public void canGenerateXSettings_TRUE_CourtTypeCommon_CourtIdNull() {
        
        // given
        JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
        globalFilter.getCourtCriteria().setCourtType(CourtType.COMMON);
        globalFilter.getCourtCriteria().setCcCourtId(null);
        
        
        // execute
        assertTrue(xsettingsGenerator.canGenerateXSettings(globalFilter));
        
    }
    
    @Test
    public void canGenerateXSettings_TRUE_CourtTypeCommon_CourtIdNotNull_IncludeDependentTRUE_NotDistrict() {
        
        // given
        JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
        globalFilter.getCourtCriteria().setCourtType(CourtType.COMMON);
        globalFilter.getCourtCriteria().setCcCourtId(12L);
        globalFilter.getCourtCriteria().setCcIncludeDependentCourtJudgments(true);
        
        CommonCourt commonCourt = new CommonCourt();
        commonCourt.setType(CommonCourtType.APPEAL);
        when(commonCourtRepository.findOne(12L)).thenReturn(commonCourt);
        
        // execute
        assertTrue(xsettingsGenerator.canGenerateXSettings(globalFilter));
        
    }
    
    
    @Test(expected = NullPointerException.class)
    public void generateXSettings_NULL() {
        
        // execute
        xsettingsGenerator.generateXSettings(null);
        
    }
    

    @Test(expected = IllegalArgumentException.class)
    public void generateXSettings_CanGenerateFALSE() {
        
        // execute
        xsettingsGenerator.generateXSettings(new JudgmentGlobalFilter());
        
    }

    
    @Test
    public void generateXSettings_CourtTypeCommon_CourtIdNull() {
     
        // given
        
        JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
        globalFilter.getCourtCriteria().setCourtType(CourtType.COMMON);
        globalFilter.getCourtCriteria().setCcCourtId(null);
        
        
        // execute
        
        XSettings xsettings = xsettingsGenerator.generateXSettings(globalFilter);
        
        
        // assert
        
        assertNotNull(xsettings);
        assertEquals(XField.CC_APPEAL, xsettings.getField());
        assertTrue(StringUtils.isBlank(xsettings.getFieldValuePrefix()));
        
    }
    
    
    @Test
    public void generateXSettings_CourtTypeCommon_CourtIdNotNull_APPEAL() {
     
        // given
        
        JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
        globalFilter.getCourtCriteria().setCourtType(CourtType.COMMON);
        globalFilter.getCourtCriteria().setCcIncludeDependentCourtJudgments(true);
        globalFilter.getCourtCriteria().setCcCourtId(12L);
        
        CommonCourt commonCourt = new CommonCourt();
        Whitebox.setInternalState(commonCourt, "id", 12L);
        commonCourt.setType(CommonCourtType.APPEAL);
        when(commonCourtRepository.findOne(12L)).thenReturn(commonCourt);
        
        
        // execute
        
        XSettings xsettings = xsettingsGenerator.generateXSettings(globalFilter);
        
        
        // assert
        
        assertNotNull(xsettings);
        assertEquals(XField.CC_REGION, xsettings.getField());
        assertEquals("12", xsettings.getFieldValuePrefix());
        
    }
    
    
    
    @Test
    public void generateXSettings_CourtTypeCommon_CourtIdNotNull_REGIONAL() {
     
        // given
        
        JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
        globalFilter.getCourtCriteria().setCourtType(CourtType.COMMON);
        globalFilter.getCourtCriteria().setCcIncludeDependentCourtJudgments(true);
        globalFilter.getCourtCriteria().setCcCourtId(12L);
        
        CommonCourt commonCourt = new CommonCourt();
        Whitebox.setInternalState(commonCourt, "id", 12L);
        commonCourt.setType(CommonCourtType.REGIONAL);
        when(commonCourtRepository.findOne(12L)).thenReturn(commonCourt);
        
        
        // execute
        
        XSettings xsettings = xsettingsGenerator.generateXSettings(globalFilter);
        
        
        // assert
        
        assertNotNull(xsettings);
        assertEquals(XField.CC_DISTRICT, xsettings.getField());
        assertEquals("12", xsettings.getFieldValuePrefix());
        
    }
}
