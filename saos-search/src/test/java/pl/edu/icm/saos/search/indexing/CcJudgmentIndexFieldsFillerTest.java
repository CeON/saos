package pl.edu.icm.saos.search.indexing;

import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.edu.icm.saos.persistence.builder.BuildersFactory;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.search.StringListMap;
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
        
        // basic
        CommonCourtJudgment basicJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .textContent("some content")
                .build();
        Map<String, List<String>> basicFields = StringListMap.of(new String[][] {
                { "databaseId", "1" },
                { "content", "some content" }
        });
        
        
        // keywords
        CcJudgmentKeyword firstKeyword = new CcJudgmentKeyword("some keyword");
        CcJudgmentKeyword secondKeyword = new CcJudgmentKeyword("some other keyword");
        CommonCourtJudgment keywordsJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .keywords(Lists.newArrayList(firstKeyword, secondKeyword))
                .build();
        Map<String, List<String>> keywordsFields = StringListMap.of(new String[][] {
                { "keyword", "some keyword", "some other keyword" },
        });
        
        
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
        Map<String, List<String>> commonCourtFields = StringListMap.of(new String[][] {
                { "courtType", "APPEAL" },
                { "courtId", "123" },
                { "courtCode", "15200000" },
                { "courtName", "Sąd Apelacyjny w Krakowie" },
                { "courtDivisionId", "816" },
                { "courtDivisionCode", "0000503" },
                { "courtDivisionName", "I Wydział Cywilny" },
        });
        
        
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
    
    @Test
    @UseDataProvider("ccJudgmentsFieldsData")
    public void fillFields(CommonCourtJudgment givenJudgment, Map<String, List<String>> expectedFields) {
        SolrInputDocument doc = new SolrInputDocument();
        ccJudgmentIndexingProcessor.fillFields(doc, givenJudgment);
        
        expectedFields.forEach((fieldName, fieldValues) -> assertFieldValues(doc, fieldName, fieldValues)); 
    }

}
