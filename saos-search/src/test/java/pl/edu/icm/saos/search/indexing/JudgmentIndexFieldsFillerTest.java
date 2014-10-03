package pl.edu.icm.saos.search.indexing;

import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;
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
public class JudgmentIndexFieldsFillerTest {

    private JudgmentIndexFieldsFiller<Judgment> judgmentIndexFieldsFiller = new JudgmentIndexFieldsFiller<Judgment>();
    
    private SolrFieldAdder<JudgmentIndexField> fieldAdder = new SolrFieldAdder<JudgmentIndexField>();
    

    @DataProvider
    public static Object[][] judgmentsFieldsData() {

        // simple
        Judgment simpleJudgment = BuildersFactory.commonCourtJudgmentWrapper(1).build();
        Map<String, List<String>> simpleFields = StringListMap.of(new String[][] {
                { "databaseId", "1" }
        });
        
        
        // court case
        CourtCase firstCourtCase = new CourtCase("ABC1A");
        CourtCase secondCourtCase = new CourtCase("CAA2BA");
        Judgment signatureJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .courtCases(Lists.newArrayList(firstCourtCase, secondCourtCase))
                .build();
        Map<String, List<String>> signatureFields = StringListMap.of(new String[][] {
                { "caseNumber", "ABC1A", "CAA2BA" }
        });
        
        
        // judges
        Judge firstJudge = new Judge("Jan Kowalski", JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE);
        Judge secondJudge = new Judge("Jacek Zieliński", JudgeRole.REPORTING_JUDGE);
        Judge thirdJudge =  new Judge("Adam Nowak");
        Judgment judgesJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .judges(Lists.newArrayList(firstJudge, secondJudge, thirdJudge))
                .build();
        Map<String, List<String>> judgesFields = StringListMap.of(new String[][] {
                { "judge", "Jan Kowalski|PRESIDING_JUDGE|REPORTING_JUDGE", "Jacek Zieliński|REPORTING_JUDGE", "Adam Nowak" },
                { "judgeName", "Jan Kowalski", "Jacek Zieliński", "Adam Nowak" },
                { "judgeWithRole_#_PRESIDING_JUDGE", "Jan Kowalski" },
                { "judgeWithRole_#_REPORTING_JUDGE", "Jan Kowalski", "Jacek Zieliński" },
                { "judgeWithRole_#_NO_ROLE", "Adam Nowak" }
        });
        
        
        // legal bases
        Judgment legalBasesJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .legalBases(Lists.newArrayList("art 1203 kc", "art 1204 kc"))
                .build();
        Map<String, List<String>> legalBasesFields = StringListMap.of(new String[][] {
                { "legalBases", "art 1203 kc", "art 1204 kc" }
        });
        
        
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
        
        Map<String, List<String>> referencedRegulationsFields = StringListMap.of(new String[][] {
                { "referencedRegulations", "Ustawa 1", "Ustawa 2" }
        });
        
        
        // judgment type
        Judgment typeJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .judgmentType(JudgmentType.SENTENCE)
                .build();
        Map<String, List<String>> typeFields = StringListMap.of(new String[][] {
                { "judgmentType", "SENTENCE" } 
        });
        
        
        // judgment date
        Judgment dateJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .judgmentDate(new LocalDate(2014, 9, 4))
                .build();
        Map<String, List<String>> dateFields = StringListMap.of(new String[][] {
                { "judgmentDate", "2014-09-04T00:00:00Z" }
        });
        
        
        // text content
        Judgment contentJudgment = BuildersFactory.commonCourtJudgmentWrapper(1)
                .textContent("some content")
                .build();
        Map<String, List<String>> contentFields = StringListMap.of(new String[][] {
                { "content", "some content" }
        });
        
        
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

                StringListMap.of(new String[][] {
                    {"databaseId", "1"},
                    {"caseNumber", "ABC1A"},
                    {"judge", "Adam Nowak"},
                    {"judgeName", "Adam Nowak"},
                    {"judgeWithRole_#_NO_ROLE", "Adam Nowak"},
                    {"legalBases", "art 1023 kc"},
                    {"referencedRegulations", "Ustawa 1"},
                    {"judgmentDate", "2014-09-04T00:00:00Z"},
                    {"judgmentType", "SENTENCE"},
                    {"content", "some content"},
                    
                })
            }
        };
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

}
