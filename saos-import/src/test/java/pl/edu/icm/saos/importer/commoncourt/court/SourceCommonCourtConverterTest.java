package pl.edu.icm.saos.importer.commoncourt.court;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.importer.commoncourt.court.XmlCommonCourt.Department;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;
import pl.edu.icm.saos.persistence.repository.CcDivisionTypeRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class SourceCommonCourtConverterTest {

    
    private SourceCommonCourtConverter commonCourtConverter = new SourceCommonCourtConverter();
    
    private CcDivisionTypeRepository ccDivisionTypeRepository = mock(CcDivisionTypeRepository.class);
    
    
    private XmlCommonCourt xmlCourt;
    
    private CommonCourtDivisionType divisionTypeCivil = new CommonCourtDivisionType();
    private CommonCourtDivisionType divisionTypeCriminal = new CommonCourtDivisionType();
    
    @Before
    public void before() {
        
        commonCourtConverter.setCcDivisionTypeRepository(ccDivisionTypeRepository);
        
        divisionTypeCivil.setCode("03");
        divisionTypeCriminal.setCode("06");
        
        when(ccDivisionTypeRepository.findByCode(Mockito.eq(divisionTypeCivil.getCode()))).thenReturn(divisionTypeCivil);
        when(ccDivisionTypeRepository.findByCode(Mockito.eq(divisionTypeCriminal.getCode()))).thenReturn(divisionTypeCriminal);
        
        xmlCourt = createXmlCommonCourt();
        
    }

    
    
    @Test
    public void convert() {
        CommonCourt commonCourt = commonCourtConverter.convert(xmlCourt);    
        assertCourt(xmlCourt, commonCourt);
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void convert_INVALID_COURT_CODE() {
        xmlCourt.setId("asas22");
        commonCourtConverter.convert(xmlCourt);    
    }

    
    //------------------------ PRIVATE --------------------------
    
    
    private void assertCourt(XmlCommonCourt xmlCourt, CommonCourt commonCourt) {
        assertEquals(xmlCourt.getId(), commonCourt.getCode());
        assertEquals(xmlCourt.getName(), commonCourt.getName());
        assertEquals(CommonCourtUtils.extractType(commonCourt.getCode()), commonCourt.getType());
        assertDepartments(xmlCourt, commonCourt);
    }



    private void assertDepartments(XmlCommonCourt xmlCourt, CommonCourt commonCourt) {
        
        assertEquals(xmlCourt.getDepartments().size(), commonCourt.getDivisions().size());
        for (Department dept : xmlCourt.getDepartments()) {
            CommonCourtDivision division = commonCourt.getDivision(dept.getId());
            assertNotNull(division);
            assertDivision(dept, division);
        }
    }



    private void assertDivision(Department dept, CommonCourtDivision division) {
        assertEquals(dept.getName(), division.getName());
        if (divisionTypeCivil.getCode().equals(CommonCourtDivisionUtils.tryExtractDivisionTypeCode(dept.getId()))) {
            assertEquals(divisionTypeCivil, division.getType());
        }
        else if (divisionTypeCriminal.getCode().equals(CommonCourtDivisionUtils.tryExtractDivisionTypeCode(dept.getId()))) {
            assertEquals(divisionTypeCriminal, division.getType());
        }
        else {
            assertNull(division.getType());
        }
    }
    
    
    private XmlCommonCourt createXmlCommonCourt() {
        XmlCommonCourt xmlCourt = new XmlCommonCourt();
        xmlCourt.setId("15050500");
        xmlCourt.setName("Sąd Okręgowy w Nowym Jorku Śląskim");
        Department dept1 = new Department("12341203", "Wydział 1 Cywilny");
        Department dept2 = new Department("12341206", "Wydział 2 Karny");
        Department dept3 = new Department("12341224", "Wydział 3 Gospodarczy");
        xmlCourt.setDepartments(Lists.newArrayList(dept1, dept2, dept3));
        return xmlCourt;
    }
    
}
