package pl.edu.icm.saos.search.indexing;

import com.google.common.collect.Lists;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import java.util.List;

import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

/**
 * @author madryk
 */
@RunWith(DataProviderRunner.class)
public class ScJudgmentIndexFieldsFillerTest {

    private ScJudgmentIndexFieldsFiller scJudgmentIndexFieldsFiller = new ScJudgmentIndexFieldsFiller();
    
    private SolrFieldAdder<JudgmentIndexField> solrFieldAdder = new SolrFieldAdder<JudgmentIndexField>();
    
    @DataProvider
    public static Object[][] scJudgmentsFieldData() {
        SolrInputFieldFactory fieldFactory = new SolrInputFieldFactory();
        //constants
        final String textContent = "some content";
        final int scJudgmentId = 1;
        final PersonnelType personnelType = PersonnelType.THREE_PERSON;


        // basic
        SupremeCourtJudgment basicJudgment = new SupremeCourtJudgment();
        basicJudgment.setTextContent(textContent);
        Whitebox.setInternalState(basicJudgment, "id", scJudgmentId);

        List<SolrInputField> basicFields = Lists.newArrayList(
                fieldFactory.create("databaseId", scJudgmentId),
                fieldFactory.create("content", textContent));
        
        // personnel type
        SupremeCourtJudgment personnelTypeJudgment = new SupremeCourtJudgment();
        personnelTypeJudgment.setPersonnelType(personnelType);
        Whitebox.setInternalState(personnelTypeJudgment, "id", scJudgmentId);

        List<SolrInputField> personnelTypeFields = Lists.newArrayList(
                fieldFactory.create("databaseId", scJudgmentId),
                fieldFactory.create("scPersonnelType", personnelType.name()));

        // chambers
        SupremeCourtChamber firstChamber = new SupremeCourtChamber();
        Whitebox.setInternalState(firstChamber, "id", 11);
        firstChamber.setName("ABC");

        SupremeCourtChamber secondChamber = new SupremeCourtChamber();
        Whitebox.setInternalState(secondChamber, "id", 12);
        secondChamber.setName("DEF");

        SupremeCourtChamberDivision division = new SupremeCourtChamberDivision();
        Whitebox.setInternalState(division, "id", 111);
        division.setName("GHI");
        division.setFullName("full GHI");
        division.setScChamber(firstChamber);

        SupremeCourtJudgment chambersJudgment = new SupremeCourtJudgment();
        Whitebox.setInternalState(chambersJudgment, "id", scJudgmentId);
        chambersJudgment.addScChamber(firstChamber);
        chambersJudgment.addScChamber(secondChamber);
        chambersJudgment.setScChamberDivision(division);

        List<SolrInputField> chambersFields = Lists.newArrayList(
                fieldFactory.create("courtType", "SUPREME"),
                fieldFactory.create("scCourtChamber", "11|ABC", "12|DEF"),
                fieldFactory.create("scCourtChamberId", 11, 12),
                fieldFactory.create("scCourtChamberName", "ABC", "DEF"),
                fieldFactory.create("scCourtChamberDivisionId", 111),
                fieldFactory.create("scCourtChamberDivisionName", "GHI"),
                fieldFactory.create("scCourtDivisionsChamberId", 11),
                fieldFactory.create("scCourtDivisionsChamberName", "ABC")
        );
        
        return new Object[][] {
                { basicJudgment, basicFields },
                { personnelTypeJudgment, personnelTypeFields },
                { chambersJudgment, chambersFields },
        };
    }
    
    @Before
    public void setUp() {
        scJudgmentIndexFieldsFiller.setFieldAdder(solrFieldAdder);
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    @UseDataProvider("scJudgmentsFieldData")
    public void fillFields(SupremeCourtJudgment givenJudgment, List<SolrInputField> expectedFields) {
        SolrInputDocument doc = new SolrInputDocument();
        scJudgmentIndexFieldsFiller.fillFields(doc, givenJudgment);
        
        expectedFields.forEach(expectedField -> assertFieldValues(doc, expectedField));
    }
}
