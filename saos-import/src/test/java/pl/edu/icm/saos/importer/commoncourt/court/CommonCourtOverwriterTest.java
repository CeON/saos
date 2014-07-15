package pl.edu.icm.saos.importer.commoncourt.court;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;

/**
 * @author Łukasz Dumiszewski
 */

public class CommonCourtOverwriterTest {

    
    
    private CommonCourtOverwriter commonCourtOverwriter = new CommonCourtOverwriter();

    
    private CommonCourt oldCourt = new CommonCourt();
    private CommonCourt newCourt = new CommonCourt();
    
    
    @Test
    public void overwrite_Name() {
        String oldName = "THE NAME";
        oldCourt.setName(oldName);
        
        String newName = oldName + " NEW";
        newCourt.setName(newName);
        
        commonCourtOverwriter.overwrite(oldCourt, newCourt);
        
        assertEquals(newName, oldCourt.getName());
        assertEquals(newName, newCourt.getName());
                
    }
    
    @Test
    public void overwrite_Type() {
        oldCourt.setType(CommonCourtType.DISTRICT);
        newCourt.setType(CommonCourtType.REGIONAL);
        
        commonCourtOverwriter.overwrite(oldCourt, newCourt);
        
        assertEquals(CommonCourtType.REGIONAL, oldCourt.getType());
        assertEquals(CommonCourtType.REGIONAL, newCourt.getType());
                
    }
    
    @Test
    public void overwrite_ParentCourt() {
        oldCourt.setParentCourt(newCourt);
        newCourt.setParentCourt(null);
        
        commonCourtOverwriter.overwrite(oldCourt, newCourt);
        
        assertEquals(newCourt, oldCourt.getParentCourt());
        assertNull(newCourt.getParentCourt());
                
    }
    
    @Test
    public void overwrite_Code() {
        oldCourt.setCode("XX");
        newCourt.setCode("YY");
        
        commonCourtOverwriter.overwrite(oldCourt, newCourt);
        
        assertEquals("XX", oldCourt.getCode());
        assertEquals("YY", newCourt.getCode());
                
    }
    
    
    @Test
    public void overwrite_Departments() {
        
        CommonCourtDivisionType civilType = new CommonCourtDivisionType();
        civilType.setCode("03");
        civilType.setName("civil");
        
        CommonCourtDivisionType criminalType = new CommonCourtDivisionType();
        civilType.setCode("06");
        civilType.setName("criminal");
        
        
        
        CommonCourtDivision division1 = createDivision("05", "Cywilny", civilType);
        CommonCourtDivision division2 = createDivision("10", "Karny", criminalType);
        
        newCourt.addDivision(division1);
        newCourt.addDivision(division2);
       
        CommonCourtDivision division3 = createDivision("05", "Karny Nieletnich", criminalType);
        CommonCourtDivision division4 = createDivision("15", "Karny Nieletnich", criminalType);
        
        oldCourt.addDivision(division3);
        oldCourt.addDivision(division4);
        
        commonCourtOverwriter.overwrite(oldCourt, newCourt);
        
        assertEquals(2, newCourt.getDivisions().size());
        assertDivision(division1, newCourt, newCourt.getDivisions().get(0));
        assertDivision(division2, newCourt, newCourt.getDivisions().get(1));
        
        assertEquals(3, oldCourt.getDivisions().size());
        assertDivision(division1, oldCourt, oldCourt.getDivisions().get(0));
        assertDivision(division2, oldCourt, oldCourt.getDivisions().get(2));
        
        assertDivision(division4, oldCourt, oldCourt.getDivisions().get(1));
        assertFalse(division4.isActive());
                
    }
    
    //------------------------ PRIVATE --------------------------
    
    private void assertDivision(CommonCourtDivision expected, CommonCourt expectedCourt, CommonCourtDivision actual) {
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expectedCourt, actual.getCourt());
    }
    
    private CommonCourtDivision createDivision(String code, String name, CommonCourtDivisionType divisionType) {
        CommonCourtDivision division = new CommonCourtDivision();
        division.setCode(code);
        division.setName(name);
        division.setType(divisionType);
        return division;
    }
    
}
