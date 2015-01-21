package pl.edu.icm.saos.search.indexing;

import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Lists;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * @author madryk
 */
@RunWith(DataProviderRunner.class)
public class NacJudgmentIndexFieldsFillerTest {

    private NacJudgmentIndexFieldsFiller nacJudgmentIndexingFiller = new NacJudgmentIndexFieldsFiller();
    
    private SolrFieldAdder<JudgmentIndexField> fieldAdder = new SolrFieldAdder<JudgmentIndexField>();
    
    
    @DataProvider
    public static Object[][] nacJudgmentsFieldsData() {
        SolrInputFieldFactory fieldFactory = new SolrInputFieldFactory();
        
        
        int idValue = 1;

        NationalAppealChamberJudgment basicJudgment = new NationalAppealChamberJudgment();
        Whitebox.setInternalState(basicJudgment, "id", idValue);

        List<SolrInputField> basicFields = Lists.newArrayList(
                fieldFactory.create("databaseId", idValue),
                fieldFactory.create("courtType", CourtType.NATIONAL_APPEAL_CHAMBER.name()));
        
        
        return new Object[][] {
                { basicJudgment, basicFields },
        };
    }
    
    @Before
    public void setUp() {
        nacJudgmentIndexingFiller.setFieldAdder(fieldAdder);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    @UseDataProvider("nacJudgmentsFieldsData")
    public void fillFields(NationalAppealChamberJudgment givenJudgment, List<SolrInputField> expectedFields) {
        
        // given
        SolrInputDocument doc = new SolrInputDocument();
        
        // execute
        nacJudgmentIndexingFiller.fillFields(doc, givenJudgment);
        
        // assert
        expectedFields.forEach(expectedField -> assertFieldValues(doc, expectedField)); 
    }

}
