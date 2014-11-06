package pl.edu.icm.saos.search.indexing;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

import java.util.Collections;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.edu.icm.saos.persistence.builder.BuildersFactory;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Lists;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * @author madryk
 */
@RunWith(DataProviderRunner.class)
public class JudgmentIndexFieldsFillerTest {

    private JudgmentIndexFieldsFiller judgmentIndexFieldsFiller = mock(JudgmentIndexFieldsFiller.class);
    
    private SolrFieldAdder<JudgmentIndexField> fieldAdder = new SolrFieldAdder<JudgmentIndexField>();
    

    @DataProvider
    public static Object[][] judgmentsFieldsData() {
        SolrInputFieldFactory fieldFactory = new SolrInputFieldFactory();

        // simple
        Judgment simpleJudgment = BuildersFactory.commonCourtJudgmentWrapper(1).build();
        List<SolrInputField> simpleFields = Collections.singletonList(
                fieldFactory.create("databaseId", 1));
        
        
        // court case
        CourtCase firstCourtCase = new CourtCase("ABC1A");
        CourtCase secondCourtCase = new CourtCase("CAA2BA");
        Judgment signatureJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .courtCases(Lists.newArrayList(firstCourtCase, secondCourtCase))
                .build();
        List<SolrInputField> signatureFields = Collections.singletonList(
                fieldFactory.create("caseNumber", "ABC1A", "CAA2BA"));
        
        
        // judges
        Judge firstJudge = new Judge("Jan Kowalski", JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE);
        Judge secondJudge = new Judge("Jacek Zieliński", JudgeRole.REPORTING_JUDGE);
        Judge thirdJudge =  new Judge("Adam Nowak");
        Judgment judgesJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .judges(Lists.newArrayList(firstJudge, secondJudge, thirdJudge))
                .build();
        List<SolrInputField> judgesFields = Lists.newArrayList(
                fieldFactory.create("judge", "Jan Kowalski|PRESIDING_JUDGE|REPORTING_JUDGE", "Jacek Zieliński|REPORTING_JUDGE", "Adam Nowak"),
                fieldFactory.create("judgeName", "Jan Kowalski", "Jacek Zieliński", "Adam Nowak"),
                fieldFactory.create("judgeWithRole_#_PRESIDING_JUDGE", "Jan Kowalski"),
                fieldFactory.create("judgeWithRole_#_REPORTING_JUDGE", "Jan Kowalski", "Jacek Zieliński"),
                fieldFactory.create("judgeWithRole_#_NO_ROLE", "Adam Nowak"));
        
        
        // legal bases
        Judgment legalBasesJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .legalBases(Lists.newArrayList("art 1203 kc", "art 1204 kc"))
                .build();
        List<SolrInputField> legalBasesFields = Collections.singletonList(
                fieldFactory.create("legalBases", "art 1203 kc", "art 1204 kc"));
        
        
        // referenced regulations
        Judgment referencedRegulationsJudgment = BuildersFactory.commonCourtJudgmentWrapper(1).build();
        JudgmentReferencedRegulation firstReferencedRegulation = BuildersFactory.judgmentReferencedRegulation()
                .rawText("Ustawa 1")
                .build();
        JudgmentReferencedRegulation secondReferencedRegulation = BuildersFactory.judgmentReferencedRegulation()
                .rawText("Ustawa 2")
                .build();
        referencedRegulationsJudgment.addReferencedRegulation(firstReferencedRegulation);
        referencedRegulationsJudgment.addReferencedRegulation(secondReferencedRegulation);
        
        List<SolrInputField> referencedRegulationsFields = Collections.singletonList(
                fieldFactory.create("referencedRegulations", "Ustawa 1", "Ustawa 2"));
        
        
        // judgment type
        Judgment typeJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .judgmentType(JudgmentType.SENTENCE)
                .build();
        List<SolrInputField> typeFields = Collections.singletonList(
                fieldFactory.create("judgmentType", "SENTENCE"));
        
        
        // judgment date
        Judgment dateJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .judgmentDate(new LocalDate(2014, 9, 4))
                .build();
        List<SolrInputField> dateFields = Collections.singletonList(
                fieldFactory.create("judgmentDate", "2014-09-04T00:00:00Z"));
        
        
        // text content
        Judgment contentJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .textContent("some content")
                .build();
        List<SolrInputField> contentFields = Collections.singletonList(
                fieldFactory.create("content", "some content"));
        
        
        return new Object[][] {
            { simpleJudgment, simpleFields },
            { signatureJudgment, signatureFields },
            { judgesJudgment, judgesFields },
            { legalBasesJudgment, legalBasesFields },
            { referencedRegulationsJudgment, referencedRegulationsFields },
            { typeJudgment, typeFields },
            { dateJudgment, dateFields },
            { contentJudgment, contentFields },
            {
                BuildersFactory.commonCourtJudgmentWrapper(1)
                    .courtCases(Lists.newArrayList(new CourtCase("ABC1A")))
                    .judges(Lists.newArrayList(thirdJudge))
                    .legalBases(Lists.newArrayList("art 1023 kc"))
                    .referencedRegulations(Lists.newArrayList(firstReferencedRegulation))
                    .judgmentDate(new LocalDate(2014, 9, 4))
                    .judgmentType(JudgmentType.SENTENCE)
                    .textContent("some content")
                    .build(),

                Lists.newArrayList(
                    fieldFactory.create("databaseId", 1),
                    fieldFactory.create("caseNumber", "ABC1A"),
                    fieldFactory.create("judge", "Adam Nowak"),
                    fieldFactory.create("judgeName", "Adam Nowak"),
                    fieldFactory.create("judgeWithRole_#_NO_ROLE", "Adam Nowak"),
                    fieldFactory.create("legalBases", "art 1023 kc"),
                    fieldFactory.create("referencedRegulations", "Ustawa 1"),
                    fieldFactory.create("judgmentDate", "2014-09-04T00:00:00Z"),
                    fieldFactory.create("judgmentType", "SENTENCE"),
                    fieldFactory.create("content", "some content")
                    
                )
            }
        };
    }
    
    @Before
    public void setUp() {
        doCallRealMethod().when(judgmentIndexFieldsFiller).fillFields(any(), any());
        doCallRealMethod().when(judgmentIndexFieldsFiller).setFieldAdder(any());
        judgmentIndexFieldsFiller.setFieldAdder(fieldAdder);
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    @UseDataProvider("judgmentsFieldsData")
    public void fillFields(Judgment givenJudgment, List<SolrInputField> expectedFields) {
        SolrInputDocument doc = new SolrInputDocument();
        judgmentIndexFieldsFiller.fillFields(doc, givenJudgment);
        
        expectedFields.forEach(expectedField -> assertFieldValues(doc, expectedField));
    }

}
