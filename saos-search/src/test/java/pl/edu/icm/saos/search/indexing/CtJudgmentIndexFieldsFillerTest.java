package pl.edu.icm.saos.search.indexing;

import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Lists;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * @author madryk
 */
@RunWith(DataProviderRunner.class)
public class CtJudgmentIndexFieldsFillerTest {

    private CtJudgmentIndexFieldsFiller ccJudgmentIndexingFiller = new CtJudgmentIndexFieldsFiller();
    
    private SolrFieldAdder<JudgmentIndexField> fieldAdder = new SolrFieldAdder<JudgmentIndexField>();
    
    
    @DataProvider
    public static Object[][] ctJudgmentsFieldsData() {
        SolrInputFieldFactory fieldFactory = new SolrInputFieldFactory();
        
        
        long idValue = 1;

        // basic
        ConstitutionalTribunalJudgment basicJudgment = new ConstitutionalTribunalJudgment();
        Whitebox.setInternalState(basicJudgment, "id", idValue);

        List<SolrInputField> basicFields = Lists.newArrayList(
                fieldFactory.create("databaseId", idValue),
                fieldFactory.create("courtType", "CONSTITUTIONAL_TRIBUNAL"));
        
        
        // dissenting opinions
        ConstitutionalTribunalJudgment dissentingOpinionsJudgment = new ConstitutionalTribunalJudgment();
        
        ConstitutionalTribunalJudgmentDissentingOpinion firstDissentingOpinion = new ConstitutionalTribunalJudgmentDissentingOpinion();
        firstDissentingOpinion.setTextContent("firstOpinionText");
        firstDissentingOpinion.addAuthor("Adam Nowak");
        firstDissentingOpinion.addAuthor("Jan Kowalski");
        dissentingOpinionsJudgment.addDissentingOpinion(firstDissentingOpinion);
        
        ConstitutionalTribunalJudgmentDissentingOpinion secondDissentingOpinion = new ConstitutionalTribunalJudgmentDissentingOpinion();
        secondDissentingOpinion.setTextContent("secondOpinionText");
        secondDissentingOpinion.addAuthor("Sławomir Zieliński");
        dissentingOpinionsJudgment.addDissentingOpinion(secondDissentingOpinion);
        
        List<SolrInputField> dissentingOpinionsFields = Lists.newArrayList(
                fieldFactory.create("ctDissentingOpinion", "firstOpinionText", "secondOpinionText", "Adam Nowak", "Jan Kowalski", "Sławomir Zieliński"),
                fieldFactory.create("ctDissentingOpinionAuthor", "Adam Nowak", "Jan Kowalski", "Sławomir Zieliński"));
        
        return new Object[][] {
                { basicJudgment, basicFields },
                { dissentingOpinionsJudgment, dissentingOpinionsFields },
        };
    }
    
    @Before
    public void setUp() {
        ccJudgmentIndexingFiller.setFieldAdder(fieldAdder);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    @UseDataProvider("ctJudgmentsFieldsData")
    public void fillFields(ConstitutionalTribunalJudgment givenJudgment, List<SolrInputField> expectedFields) {
        
        // given
        SolrInputDocument doc = new SolrInputDocument();
        JudgmentIndexingData indexingData = new JudgmentIndexingData();
        indexingData.setJudgment(givenJudgment);
        
        // execute
        ccJudgmentIndexingFiller.fillFields(doc, indexingData);
        
        // assert
        expectedFields.forEach(expectedField -> assertFieldValues(doc, expectedField)); 
    }

}
