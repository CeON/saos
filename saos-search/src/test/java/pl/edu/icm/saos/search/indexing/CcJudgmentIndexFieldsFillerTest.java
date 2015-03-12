package pl.edu.icm.saos.search.indexing;

import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Lists;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * @author madryk
 */
@RunWith(DataProviderRunner.class)
public class CcJudgmentIndexFieldsFillerTest {

    private CcJudgmentIndexFieldsFiller ccJudgmentIndexingProcessor = new CcJudgmentIndexFieldsFiller();
    
    private SolrFieldAdder<JudgmentIndexField> fieldAdder = new SolrFieldAdder<JudgmentIndexField>();
    
    @DataProvider
    public static Object[][] ccJudgmentsFieldsData() {
        SolrInputFieldFactory fieldFactory = new SolrInputFieldFactory();
        
        // basic
        String textContent = "some content";
        long idValue = 1;

        CommonCourtJudgment basicJudgment = new CommonCourtJudgment();
        Whitebox.setInternalState(basicJudgment, "id", idValue);
        basicJudgment.setRawTextContent(textContent);

        List<SolrInputField> basicFields = Lists.newArrayList(
                fieldFactory.create("databaseId", idValue),
                fieldFactory.create("content", textContent));
        
        
        // keywords
        JudgmentKeyword keyword1 = new JudgmentKeyword(CourtType.COMMON, UUID.randomUUID().toString());
        JudgmentKeyword keyword2 = new JudgmentKeyword(CourtType.COMMON, UUID.randomUUID().toString());
        
        CommonCourtJudgment keywordsJudgment = new CommonCourtJudgment();
        keywordsJudgment.addKeyword(keyword1);
        keywordsJudgment.addKeyword(keyword2);
        
        List<SolrInputField> keywordsFields = Collections.singletonList(
                fieldFactory.create("keyword", keyword1.getPhrase(), keyword2.getPhrase()));

        
        
        // common court
        CommonCourt commonCourt = new CommonCourt();
        Whitebox.setInternalState(commonCourt, "id", 123);
        commonCourt.setCode("15200000");
        commonCourt.setName("Sąd Apelacyjny w Krakowie");
        commonCourt.setType(CommonCourtType.APPEAL);

        CommonCourtDivision commonCourtDivision = new CommonCourtDivision();
        Whitebox.setInternalState(commonCourtDivision, "id", 816);
        commonCourtDivision.setCode("0000503");
        commonCourtDivision.setName("I Wydział Cywilny");
        commonCourtDivision.setCourt(commonCourt);

        CommonCourtJudgment commonCourtJudgment = new CommonCourtJudgment();
        Whitebox.setInternalState(commonCourtJudgment, "id", idValue);
        commonCourtJudgment.setCourtDivision(commonCourtDivision);

        List<SolrInputField> commonCourtFields = Lists.newArrayList(
                fieldFactory.create("courtType", "COMMON"),
                fieldFactory.create("ccCourtType", "APPEAL"),
                fieldFactory.create("ccCourtId", 123l),
                fieldFactory.create("ccCourtCode", "15200000"),
                fieldFactory.create("ccCourtName", "Sąd Apelacyjny w Krakowie"),
                fieldFactory.create("ccCourtDivisionId", 816l),
                fieldFactory.create("ccCourtDivisionCode", "0000503"),
                fieldFactory.create("ccCourtDivisionName", "I Wydział Cywilny"));
        
        
        return new Object[][] {
                { basicJudgment, basicFields },
                { keywordsJudgment, keywordsFields },
                { commonCourtJudgment, commonCourtFields },
        };
    }
    
    @Before
    public void setUp() {
        ccJudgmentIndexingProcessor.setFieldAdder(fieldAdder);
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    @UseDataProvider("ccJudgmentsFieldsData")
    public void fillFields(CommonCourtJudgment givenJudgment, List<SolrInputField> expectedFields) {
        SolrInputDocument doc = new SolrInputDocument();
        ccJudgmentIndexingProcessor.fillFields(doc, givenJudgment);
        
        expectedFields.forEach(expectedField -> assertFieldValues(doc, expectedField)); 
    }

}
