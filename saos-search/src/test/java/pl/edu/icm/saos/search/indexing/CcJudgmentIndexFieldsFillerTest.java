package pl.edu.icm.saos.search.indexing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.util.CcCourtAreaFieldValueCreator;

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
    
    private CcCourtAreaFieldValueCreator ccCourtAreaFieldValueCreator = mock(CcCourtAreaFieldValueCreator.class); 
            
            
    
    @DataProvider
    public static Object[][] ccJudgmentsFieldsData() {
        SolrInputFieldFactory fieldFactory = new SolrInputFieldFactory();
        
        // basic
        String textContent = "some content";
        long idValue = 1;

        CommonCourtJudgment basicJudgment = new CommonCourtJudgment();
        Whitebox.setInternalState(basicJudgment, "id", idValue);
        basicJudgment.getTextContent().setRawTextContent(textContent);

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
        
        CommonCourt appealCourt = new CommonCourt();
        Whitebox.setInternalState(appealCourt, "id", 123);
        appealCourt.setCode("15200000");
        appealCourt.setName("Sąd Apelacyjny w Krakowie");
        appealCourt.setType(CommonCourtType.APPEAL);

        CommonCourtDivision appealCourtDivision = new CommonCourtDivision();
        Whitebox.setInternalState(appealCourtDivision, "id", 816);
        appealCourtDivision.setCode("0000503");
        appealCourtDivision.setName("I Wydział Cywilny");
        appealCourtDivision.setCourt(appealCourt);

        
        CommonCourt regionalCourt = new CommonCourt();
        Whitebox.setInternalState(regionalCourt, "id", 124);
        regionalCourt.setCode("15200001");
        regionalCourt.setName("Sąd Okręgowy w Krakowie");
        regionalCourt.setType(CommonCourtType.REGIONAL);
        regionalCourt.setParentCourt(appealCourt);
        
        CommonCourtDivision regionalCourtDivision = new CommonCourtDivision();
        Whitebox.setInternalState(regionalCourtDivision, "id", 817);
        regionalCourtDivision.setCode("0000504");
        regionalCourtDivision.setName("I Wydział Cywilny");
        regionalCourtDivision.setCourt(regionalCourt);


        CommonCourt districtCourt = new CommonCourt();
        Whitebox.setInternalState(districtCourt, "id", 125);
        districtCourt.setCode("15200002");
        districtCourt.setName("Sąd Rejonowy w Częstochowie");
        districtCourt.setType(CommonCourtType.DISTRICT);
        districtCourt.setParentCourt(regionalCourt);

        CommonCourtDivision districtCourtDivision = new CommonCourtDivision();
        Whitebox.setInternalState(districtCourtDivision, "id", 818);
        districtCourtDivision.setCode("0000505");
        districtCourtDivision.setName("I Wydział Cywilny");
        districtCourtDivision.setCourt(districtCourt);

        CommonCourtJudgment appealCourtJudgment = new CommonCourtJudgment();
        Whitebox.setInternalState(appealCourtJudgment, "id", idValue);
        appealCourtJudgment.setCourtDivision(appealCourtDivision);

        CommonCourtJudgment regionalCourtJudgment = new CommonCourtJudgment();
        Whitebox.setInternalState(regionalCourtJudgment, "id", idValue+1);
        regionalCourtJudgment.setCourtDivision(regionalCourtDivision);

        CommonCourtJudgment districtCourtJudgment = new CommonCourtJudgment();
        Whitebox.setInternalState(districtCourtJudgment, "id", idValue+2);
        districtCourtJudgment.setCourtDivision(districtCourtDivision);

        
        List<SolrInputField> appealCourtFields = Lists.newArrayList(
                fieldFactory.create("courtType", "COMMON"),
                fieldFactory.create("ccCourtType", "APPEAL"),
                fieldFactory.create("ccCourtId", 123l),
                fieldFactory.create("ccCourtCode", "15200000"),
                fieldFactory.create("ccCourtName", "Sąd Apelacyjny w Krakowie"),
                fieldFactory.create("ccCourtDivisionId", 816l),
                fieldFactory.create("ccCourtDivisionCode", "0000503"),
                fieldFactory.create("ccCourtDivisionName", "I Wydział Cywilny"),
                fieldFactory.create("ccAppealCourtId", 123l),
                fieldFactory.create("ccAppealArea", "NA_Sąd Apelacyjny w Krakowie#123"),
                fieldFactory.create("ccRegionArea", "123_Sąd Apelacyjny w Krakowie#123"));
        
                        
         List<SolrInputField> regionalCourtFields = Lists.newArrayList(
                fieldFactory.create("courtType", "COMMON"),
                fieldFactory.create("ccCourtType", "REGIONAL"),
                fieldFactory.create("ccCourtId", 124l),
                fieldFactory.create("ccCourtCode", "15200001"),
                fieldFactory.create("ccCourtName", "Sąd Okręgowy w Krakowie"),
                fieldFactory.create("ccCourtDivisionId", 817l),
                fieldFactory.create("ccCourtDivisionCode", "0000504"),
                fieldFactory.create("ccCourtDivisionName", "I Wydział Cywilny"),
                fieldFactory.create("ccAppealCourtId", 123l),
                fieldFactory.create("ccAppealArea", "NA_Sąd Apelacyjny w Krakowie#123"),
                fieldFactory.create("ccRegionalCourtId", 124l),
                fieldFactory.create("ccRegionArea", "123_Sąd Okręgowy w Krakowie#124"),
                fieldFactory.create("ccDistrictArea", "124_Sąd Okręgowy w Krakowie#124"));

                
          List<SolrInputField> districtCourtFields = Lists.newArrayList(
                fieldFactory.create("courtType", "COMMON"),
                fieldFactory.create("ccCourtType", "DISTRICT"),
                fieldFactory.create("ccCourtId", 125l),
                fieldFactory.create("ccCourtCode", "15200002"),
                fieldFactory.create("ccCourtName", "Sąd Rejonowy w Częstochowie"),
                fieldFactory.create("ccCourtDivisionId", 818l),
                fieldFactory.create("ccCourtDivisionCode", "0000505"),
                fieldFactory.create("ccCourtDivisionName", "I Wydział Cywilny"),
                fieldFactory.create("ccAppealCourtId", 123l),
                fieldFactory.create("ccAppealArea", "NA_Sąd Apelacyjny w Krakowie#123"),
                fieldFactory.create("ccRegionalCourtId", 124l),
                fieldFactory.create("ccRegionArea", "123_Sąd Okręgowy w Krakowie#124"),
                fieldFactory.create("ccDistrictCourtId", 125l),
                fieldFactory.create("ccDistrictArea", "124_Sąd Rejonowy w Częstochowie#125"));
        
              
        return new Object[][] {
                { basicJudgment, basicFields },
                { keywordsJudgment, keywordsFields },
                { appealCourtJudgment, appealCourtFields },
                { regionalCourtJudgment, regionalCourtFields },
                { districtCourtJudgment, districtCourtFields },
        };
    }
    
    @Before
    public void setUp() {
        ccJudgmentIndexingProcessor.setFieldAdder(fieldAdder);
        ccJudgmentIndexingProcessor.setCcCourtAreaFieldValueCreator(ccCourtAreaFieldValueCreator);
        when(ccCourtAreaFieldValueCreator.createCcCourtAreaFieldValue(Mockito.anyLong(), Mockito.any(CommonCourt.class))).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                CommonCourt court = (CommonCourt)invocation.getArguments()[1];
                String parentAreaCourtId = "NA";
                if (invocation.getArguments()[0] != null) {
                    parentAreaCourtId = invocation.getArguments()[0].toString();
                }
                return parentAreaCourtId + "_" + court.getName() + "#" + court.getId();
            }
        });
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    @UseDataProvider("ccJudgmentsFieldsData")
    public void fillFields(CommonCourtJudgment givenJudgment, List<SolrInputField> expectedFields) {
        // given
        SolrInputDocument doc = new SolrInputDocument();
        JudgmentIndexingData indexingData = new JudgmentIndexingData();
        indexingData.setJudgment(givenJudgment);
        
        // execute
        ccJudgmentIndexingProcessor.fillFields(doc, indexingData);
        
        // assert
        expectedFields.forEach(expectedField -> assertFieldValues(doc, expectedField)); 
    }

}
