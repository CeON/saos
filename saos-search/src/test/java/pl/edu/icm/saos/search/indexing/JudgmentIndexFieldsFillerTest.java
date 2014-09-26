package pl.edu.icm.saos.search.indexing;

import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * @author madryk
 */
@RunWith(DataProviderRunner.class)
public class JudgmentIndexFieldsFillerTest {

    private JudgmentIndexFieldsFiller<Judgment> judgmentIndexFieldsFiller = new JudgmentIndexFieldsFiller<Judgment>();
    
    private SolrFieldAdder<JudgmentIndexField> fieldAdder = new SolrFieldAdder<JudgmentIndexField>();
    

    @DataProvider
    public static Object[][] judgmentsFieldsData() {
        
        CommonCourtJudgmentBuilder judgmentBuilder = new CommonCourtJudgmentBuilder();
        
        @SuppressWarnings("serial")
        Object[][] data = {
                {
                        judgmentBuilder.withId(6).buildAndReset(),
                        ImmutableMap.of("databaseId", Lists.newArrayList("6"))
                },
                {
                        judgmentBuilder.withCourtCases(new CourtCase("ABC1A")).buildAndReset(),
                        ImmutableMap.of("caseNumber", Lists.newArrayList("ABC1A"))
                },
                {
                        judgmentBuilder.withJudges(
                                new Judge("Jan Kowalski", JudgeRole.PRESIDING_JUDGE),
                                new Judge("Adam Nowak"))
                                .buildAndReset(),
                        ImmutableMap.of(
                                "judge", Lists.newArrayList("Adam Nowak"),
                                "judgeWithRole_#_PRESIDING_JUDGE", Lists.newArrayList("Jan Kowalski"))
                },
                {
                        judgmentBuilder.withLegalBases("art 1203 kc", "art 1204 kc").buildAndReset(),
                        ImmutableMap.of("legalBases", Lists.newArrayList("art 1203 kc", "art 1204 kc"))
                },
                {
                        judgmentBuilder.withReferencedRegulations("Ustawa 1", "Ustawa 2").buildAndReset(),
                        ImmutableMap.of("referencedRegulations", Lists.newArrayList("Ustawa 1", "Ustawa 2"))
                },
                {
                        judgmentBuilder.withJudgmentDate(new LocalDate(2014, 9, 4)).buildAndReset(),
                        ImmutableMap.of("judgmentDate", Lists.newArrayList("2014-09-04T00:00:00Z"))
                },
                {
                        judgmentBuilder.withTextContent("some content").buildAndReset(),
                        ImmutableMap.of("content", Lists.newArrayList("some content"))
                },
                {
                        judgmentBuilder
                                .withId(6)
                                .withCourtCases(new CourtCase("ABC1A"))
                                .withJudges(new Judge("Adam Nowak"))
                                .withLegalBases("art 1023 kc")
                                .withReferencedRegulations("Ustawa 1")
                                .withJudgmentDate(new LocalDate(2014, 9, 4))
                                .withJudgmentType(JudgmentType.SENTENCE)
                                .withTextContent("some content").buildAndReset(),
                        new HashMap<String, List<String>>() {{
                            put("databaseId", Lists.newArrayList("6"));
                            put("caseNumber", Lists.newArrayList("ABC1A"));
                            put("judge", Lists.newArrayList("Adam Nowak"));
                            put("legalBases", Lists.newArrayList("art 1023 kc"));
                            put("referencedRegulations", Lists.newArrayList("Ustawa 1"));
                            put("judgmentDate", Lists.newArrayList("2014-09-04T00:00:00Z"));
                            put("judgmentType", Lists.newArrayList("SENTENCE"));
                            put("content", Lists.newArrayList("some content"));
                        }}
                }
        };
        
                
        return data;
    }
    
    @Before
    public void setUp() {
        judgmentIndexFieldsFiller.setFieldAdder(fieldAdder);
    }
    
    @Test
    @UseDataProvider("judgmentsFieldsData")
    public void fillFields(Judgment givenJudgment, Map<String, List<String>> expectedFields) {
        SolrInputDocument doc = new SolrInputDocument();
        judgmentIndexFieldsFiller.fillFields(doc, givenJudgment);
        
        expectedFields.forEach((fieldName, fieldValues) -> assertFieldValues(doc, fieldName, fieldValues)); 
    }
    
//    
//    @Test
//    public void fillFields() {
//        CommonCourtJudgment judgment = new CommonCourtJudgment();
//        
//        JudgmentReferencedRegulation firstReferencedRegulation = new JudgmentReferencedRegulation();
//        firstReferencedRegulation.setRawText("Ustawa 1");
//        
//        ReflectionTestUtils.setField(judgment, "id", 6);
//        judgment.addCourtCase(new CourtCase("ABC1A"));
//        judgment.addJudge(new Judge("Adam Nowak"));
//        judgment.addLegalBase("art 1203 kc");
//        judgment.addReferencedRegulation(firstReferencedRegulation);
//        judgment.setJudgmentDate(new LocalDate(2014, 9, 4));
//        judgment.setJudgmentType(JudgmentType.SENTENCE);
//        judgment.setTextContent("some content");
//        
//        
//        SolrInputDocument doc = new SolrInputDocument();
//        judgmentIndexFieldsFiller.fillFields(doc, judgment);
//        
//        
//        assertFieldValue(doc, "databaseId", "6");
//        assertFieldValue(doc, "caseNumber", "ABC1A");
//        assertFieldValue(doc, "judge", "Adam Nowak");
//        assertFieldValue(doc, "legalBases", "art 1203 kc");
//        assertFieldValue(doc, "referencedRegulations", "Ustawa 1");
//        assertFieldValue(doc, "judgmentDate", "2014-09-04T00:00:00Z");
//        assertFieldValue(doc, "judgmentType", "SENTENCE");
//        assertFieldValue(doc, "content", "some content");
//    }

}
