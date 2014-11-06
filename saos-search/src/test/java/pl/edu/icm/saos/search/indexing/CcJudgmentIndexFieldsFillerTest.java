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
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
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
        CommonCourtJudgment basicJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .textContent("some content")
                .build();
        List<SolrInputField> basicFields = Lists.newArrayList(
                fieldFactory.create("databaseId", 1),
                fieldFactory.create("content", "some content"));
        
        
        // keywords
        CcJudgmentKeyword firstKeyword = new CcJudgmentKeyword("some keyword");
        CcJudgmentKeyword secondKeyword = new CcJudgmentKeyword("some other keyword");
        CommonCourtJudgment keywordsJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .keywords(Lists.newArrayList(firstKeyword, secondKeyword))
                .build();
        List<SolrInputField> keywordsFields = Collections.singletonList(
                fieldFactory.create("keyword", "some keyword", "some other keyword"));
        
        
        // common court
        CommonCourt commonCourt = BuildersFactory.commonCourt(123)
                .code("15200000")
                .name("Sąd Apelacyjny w Krakowie")
                .type(CommonCourtType.APPEAL)
                .build();
        CommonCourtDivision commonCourtDivision = BuildersFactory.commonCourtDivision(816)
                .code("0000503")
                .name("I Wydział Cywilny")
                .court(commonCourt)
                .build();
        CommonCourtJudgment commonCourtJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .division(commonCourtDivision)
                .build();
        List<SolrInputField> commonCourtFields = Lists.newArrayList(
                fieldFactory.create("courtType", "COMMON"),
                fieldFactory.create("commonCourtType", "APPEAL"),
                fieldFactory.create("courtId", 123),
                fieldFactory.create("courtCode", "15200000"),
                fieldFactory.create("courtName", "Sąd Apelacyjny w Krakowie"),
                fieldFactory.create("courtDivisionId", 816),
                fieldFactory.create("courtDivisionCode", "0000503"),
                fieldFactory.create("courtDivisionName", "I Wydział Cywilny"));
        
        
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
