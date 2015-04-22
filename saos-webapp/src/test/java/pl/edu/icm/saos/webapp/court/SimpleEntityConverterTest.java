package pl.edu.icm.saos.webapp.court;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Pawełczak
 */
public class SimpleEntityConverterTest {

    private SimpleEntityConverter simpleEntityConverter = new SimpleEntityConverter();
	
    private String[] commonCourtNames = {"Sąd Okręgowy Warszawa-Praga w Warszawie", "Sąd Apelacyjny w Rzeszowie"};
		
    private String[] ccDivisionNames = {"Wydział prawa I", "Wydział karny II"};
	
    private String[] scChamberNames = {"Izba Cywilna", "Izba Karna"};
	
    private String[] scDivisionChamberNames = {"Wydział Odwoławczo-Kasacyjny", "Wydział II"};
	
    private String[] scJudgmentFormNames = {"wyrok SN SD", "uchwała siedmiu sędziów SN"};
	
    
    //------------------------ TESTS --------------------------
	
    @Test
    public void convertCommonCourts_emptyList() {
	//given
	List<CommonCourt> commonCourts = Lists.newArrayList();
	
	//when
	List<SimpleEntity> convertedSimpleEntities = simpleEntityConverter.convertCommonCourts(commonCourts);
	
	//then
	assertNotNull(convertedSimpleEntities);
	assertEquals(0, convertedSimpleEntities.size());
    }
	
    @Test
    public void convertCommonCourts_Same() {
	//given
	long idOne = 83;
	long idTwo = 257;
	CommonCourt commonCourtOne = new CommonCourt();
	commonCourtOne.setName(commonCourtNames[0]);
	Whitebox.setInternalState(commonCourtOne, "id", idOne);
	CommonCourt commonCourtTwo = new CommonCourt();
	commonCourtTwo.setName(commonCourtNames[1]);
	Whitebox.setInternalState(commonCourtTwo, "id", idTwo);
	
	List<CommonCourt> commonCourts = Lists.newArrayList(commonCourtOne, commonCourtTwo);
	
	//when
	List<SimpleEntity> convertedSimpleEntities = simpleEntityConverter.convertCommonCourts(commonCourts);
	
	//then
	assertEquals(commonCourts.size(), convertedSimpleEntities.size());
	assertEquals(idOne, convertedSimpleEntities.get(0).getId());
	assertEquals(idTwo, convertedSimpleEntities.get(1).getId());
	assertEquals(commonCourtNames[0], convertedSimpleEntities.get(0).getName());
	assertEquals(commonCourtNames[1], convertedSimpleEntities.get(1).getName());
    }
	
    @Test
    public void convertCcDivisions_emptyList() {
	//given
	List<CommonCourtDivision> ccDivisions = Lists.newArrayList();
	
	//when
	List<SimpleEntity> convertedSimpleEntities = simpleEntityConverter.convertCcDivisions(ccDivisions);
	
	//then
	assertNotNull(convertedSimpleEntities);
	assertEquals(0, convertedSimpleEntities.size());
	}
	
    @Test
    public void convertCcDivisions_Same() {
	//given
	long idOne = 111;
	long idTwo = 222;
	CommonCourtDivision ccDivisionOne = new CommonCourtDivision();
	ccDivisionOne.setName(ccDivisionNames[0]);
	Whitebox.setInternalState(ccDivisionOne, "id", idOne);
	CommonCourtDivision ccDivisionTwo = new CommonCourtDivision();
	ccDivisionTwo.setName(ccDivisionNames[1]);
	Whitebox.setInternalState(ccDivisionTwo, "id", idTwo);
	
	List<CommonCourtDivision> ccDivisions = Arrays.asList(ccDivisionOne, ccDivisionTwo);
	
	//when
	List<SimpleEntity> convertedSimpleEntities = simpleEntityConverter.convertCcDivisions(ccDivisions);
	
	//then
	assertEquals(ccDivisionNames.length, convertedSimpleEntities.size());
	assertEquals(idOne, convertedSimpleEntities.get(0).getId());
	assertEquals(idTwo, convertedSimpleEntities.get(1).getId());
	assertEquals(ccDivisionNames[0], convertedSimpleEntities.get(0).getName());
	assertEquals(ccDivisionNames[1], convertedSimpleEntities.get(1).getName());
    }
	
	
    @Test
    public void convertScChambers_emptyList() {
	//given
	List<SupremeCourtChamber> scChambers = Lists.newArrayList();
	
	//when
	List<SimpleEntity> convertedSimpleEntities = simpleEntityConverter.convertScChambers(scChambers);
	
	//then
	assertNotNull(convertedSimpleEntities);
	assertEquals(0, convertedSimpleEntities.size());
    }
	
    @Test
    public void convertScChambers_Same() {
	//given
	long idOne = 97;
	long idTwo = 101;
	SupremeCourtChamber scChamberOne = new SupremeCourtChamber();
	scChamberOne.setName(scChamberNames[0]);
	Whitebox.setInternalState(scChamberOne, "id", idOne);
	SupremeCourtChamber scChamberTwo = new SupremeCourtChamber();
	scChamberTwo.setName(scChamberNames[1]);
	Whitebox.setInternalState(scChamberTwo, "id", idTwo);
	
	List<SupremeCourtChamber> scChambers = Lists.newArrayList(scChamberOne, scChamberTwo);
	
	//when
	List<SimpleEntity> convertedSimpleEntities = simpleEntityConverter.convertScChambers(scChambers);
	
	//then
	assertEquals(scChambers.size(), convertedSimpleEntities.size());
	assertEquals(idOne, convertedSimpleEntities.get(0).getId());
	assertEquals(idTwo, convertedSimpleEntities.get(1).getId());
	assertEquals(scChamberNames[0], convertedSimpleEntities.get(0).getName());
	assertEquals(scChamberNames[1], convertedSimpleEntities.get(1).getName());
    }
	
	
    @Test
    public void convertScDivisionChambers_emptyList() {
	//given
	List<SupremeCourtChamberDivision> scChamberDivisions = Lists.newArrayList();
	
	//when
	List<SimpleEntity> convertedSimpleEntities = simpleEntityConverter.convertScChamberDivisions(scChamberDivisions);
	
	//then
	assertNotNull(convertedSimpleEntities);
	assertEquals(0, convertedSimpleEntities.size());
    }
	
    @Test
    public void convertScDivisionChambers_Same() {
	//given
	long idOne = 111;
	long idTwo = 222;
	SupremeCourtChamberDivision scChamberDivisionOne = new SupremeCourtChamberDivision();
	scChamberDivisionOne.setName(scDivisionChamberNames[0]);
	Whitebox.setInternalState(scChamberDivisionOne, "id", idOne);
	SupremeCourtChamberDivision scChamberDivisionTwo = new SupremeCourtChamberDivision();
	scChamberDivisionTwo.setName(scDivisionChamberNames[1]);
	Whitebox.setInternalState(scChamberDivisionTwo, "id", idTwo);
	
	List<SupremeCourtChamberDivision> scChamberDivisions =  Arrays.asList(scChamberDivisionOne, scChamberDivisionTwo);
	
	//when
	List<SimpleEntity> convertedSimpleEntities = simpleEntityConverter.convertScChamberDivisions(scChamberDivisions);
	
	//then
	assertEquals(scDivisionChamberNames.length, convertedSimpleEntities.size());
	assertEquals(idOne, convertedSimpleEntities.get(0).getId());
	assertEquals(idTwo, convertedSimpleEntities.get(1).getId());
	assertEquals(scDivisionChamberNames[0], convertedSimpleEntities.get(0).getName());
	assertEquals(scDivisionChamberNames[1], convertedSimpleEntities.get(1).getName());
    }
	
	
    @Test
    public void convertScJudgmentForms_emptyList() {
	//given
	List<SupremeCourtJudgmentForm> scJudgmentForms = Lists.newArrayList();
	  	
	//when
	List<SimpleEntity> convertedSimpleEntities = simpleEntityConverter.convertScJudgmentForms(scJudgmentForms);
	  	
	//then
	assertNotNull(convertedSimpleEntities);
	assertEquals(0, convertedSimpleEntities.size());
	}
	
    @Test
    public void convertScJudgmentForms_same() {
	//given
	long idOne = 1;
	long idTwo = 2;
	
	SupremeCourtJudgmentForm scJudgmentFormOne = new SupremeCourtJudgmentForm();
	scJudgmentFormOne.setName(scJudgmentFormNames[0]);
	Whitebox.setInternalState(scJudgmentFormOne, "id", idOne);
	SupremeCourtJudgmentForm scJudgmentFormTwo = new SupremeCourtJudgmentForm();
	scJudgmentFormTwo.setName(scJudgmentFormNames[1]);
	Whitebox.setInternalState(scJudgmentFormTwo, "id", idTwo);
	
	List<SupremeCourtJudgmentForm> scJudgmentForms =  Arrays.asList(scJudgmentFormOne, scJudgmentFormTwo);
	
	//when
	List<SimpleEntity> convertedSimpleEntities = simpleEntityConverter.convertScJudgmentForms(scJudgmentForms);
		
	//then
	assertEquals(scJudgmentFormNames.length, convertedSimpleEntities.size());
	assertEquals(idOne, convertedSimpleEntities.get(0).getId());
	assertEquals(idTwo, convertedSimpleEntities.get(1).getId());
	assertEquals(scJudgmentFormNames[0], convertedSimpleEntities.get(0).getName());
	assertEquals(scJudgmentFormNames[1], convertedSimpleEntities.get(1).getName());
    }
    
}
