package pl.edu.icm.saos.search.indexing;

import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

import java.util.Collections;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.edu.icm.saos.persistence.builder.BuildersFactory;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Lists;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

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
        
        // basic
        SupremeCourtJudgment basicJudgment = BuildersFactory.supremeCourtJugmentWrapper(1)
                .textContent("some content")
                .build();
        List<SolrInputField> basicFields = Lists.newArrayList(
                fieldFactory.create("databaseId", 1),
                fieldFactory.create("content", "some content"));
        
        // personnel type
        SupremeCourtJudgment personnelTypeJudgment = BuildersFactory.supremeCourtJugmentWrapper(1)
                .personnelType(PersonnelType.THREE_PERSON)
                .build();
        List<SolrInputField> personnelTypeFields = Collections.singletonList(
                fieldFactory.create("personnelType", "THREE_PERSON"));

        // chambers
        SupremeCourtChamber firstChamber = BuildersFactory.supremeCourtChamber(11).name("ABC").build();
        SupremeCourtChamber secondChamber = BuildersFactory.supremeCourtChamber(12).name("DEF").build();
        SupremeCourtChamberDivision division = BuildersFactory.supremeCourtChamberDivision(111).build();
        
        SupremeCourtJudgment chambersJudgment = BuildersFactory.supremeCourtJugmentWrapper(1)
                .chamber(firstChamber)
                .chamber(secondChamber)
                .division(division)
                .build();
        List<SolrInputField> chambersFields = Lists.newArrayList(
                fieldFactory.create("courtType", "SUPREME"),
//                fieldFactory.create("courtChamber", "11|chamberName1", "12|chamberName2"), // TODO uncomment when it will be available
                fieldFactory.create("courtChamberId", 11, 12),
//                fieldFactory.create("courtChamberName", "chamberName1", "chamberName2"), // TODO uncomment when it will be available
                fieldFactory.create("courtChamberDivisionId", 111)
//                fieldFactory.create("courtChamberDivisionName", "divisionName") // TODO uncomment when it will be available
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
    
    @Test
    @UseDataProvider("scJudgmentsFieldData")
    public void fillFields(SupremeCourtJudgment givenJudgment, List<SolrInputField> expectedFields) {
        SolrInputDocument doc = new SolrInputDocument();
        scJudgmentIndexFieldsFiller.fillFields(doc, givenJudgment);
        
        expectedFields.forEach(expectedField -> assertFieldValues(doc, expectedField));
    }
}
