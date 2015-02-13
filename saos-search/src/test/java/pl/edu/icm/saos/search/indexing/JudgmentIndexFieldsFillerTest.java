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
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
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
        Judgment simpleJudgment = new CommonCourtJudgment();
        Whitebox.setInternalState(simpleJudgment, "id", 1);
        List<SolrInputField> simpleFields = Collections.singletonList(
                fieldFactory.create("databaseId", 1l));
        
        
        // court case
        CourtCase firstCourtCase = new CourtCase("ABC1A");
        CourtCase secondCourtCase = new CourtCase("CAA2BA");
        Judgment signatureJudgment = new CommonCourtJudgment();
        signatureJudgment.addCourtCase(firstCourtCase);
        signatureJudgment.addCourtCase(secondCourtCase);

        List<SolrInputField> signatureFields = Collections.singletonList(
                fieldFactory.create("caseNumber", "ABC1A", "CAA2BA"));
        
        
        // judges
        Judge firstJudge = new Judge("Jan Kowalski", JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE);
        Judge secondJudge = new Judge("Jacek Zieliński", JudgeRole.REPORTING_JUDGE);
        Judge thirdJudge =  new Judge("Adam Nowak");

        Judgment judgesJudgment = new CommonCourtJudgment();
        judgesJudgment.addJudge(firstJudge);
        judgesJudgment.addJudge(secondJudge);
        judgesJudgment.addJudge(thirdJudge);

        List<SolrInputField> judgesFields = Lists.newArrayList(
                fieldFactory.create("judge", "Jan Kowalski|PRESIDING_JUDGE|REPORTING_JUDGE", "Jacek Zieliński|REPORTING_JUDGE", "Adam Nowak"),
                fieldFactory.create("judgeName", "Jan Kowalski", "Jacek Zieliński", "Adam Nowak"),
                fieldFactory.create("judgeWithRole_#_PRESIDING_JUDGE", "Jan Kowalski"),
                fieldFactory.create("judgeWithRole_#_REPORTING_JUDGE", "Jan Kowalski", "Jacek Zieliński"),
                fieldFactory.create("judgeWithRole_#_NO_ROLE", "Adam Nowak"));
        
        
        // legal bases
        Judgment legalBasesJudgment = new CommonCourtJudgment();
        legalBasesJudgment.addLegalBase("art 1203 kc");
        legalBasesJudgment.addLegalBase("art 1204 kc");

        List<SolrInputField> legalBasesFields = Collections.singletonList(
                fieldFactory.create("legalBases", "art 1203 kc", "art 1204 kc"));
        
        
        // referenced regulations
        Judgment referencedRegulationsJudgment = new CommonCourtJudgment();

        JudgmentReferencedRegulation firstReferencedRegulation = new JudgmentReferencedRegulation();
        LawJournalEntry firstLawJournalEntry = new LawJournalEntry();
        
        Whitebox.setInternalState(firstLawJournalEntry, "id", 55);
        firstReferencedRegulation.setRawText("Ustawa 1");
        firstReferencedRegulation.setLawJournalEntry(firstLawJournalEntry);
        
        
        JudgmentReferencedRegulation secondReferencedRegulation = new JudgmentReferencedRegulation();
        LawJournalEntry secondLawJournalEntry = new LawJournalEntry();
        
        Whitebox.setInternalState(secondLawJournalEntry, "id", 56);
        secondReferencedRegulation.setRawText("Ustawa 2");
        secondReferencedRegulation.setLawJournalEntry(secondLawJournalEntry);

        referencedRegulationsJudgment.addReferencedRegulation(firstReferencedRegulation);
        referencedRegulationsJudgment.addReferencedRegulation(secondReferencedRegulation);
        
        
        List<SolrInputField> referencedRegulationsFields = Lists.newArrayList(
                fieldFactory.create("referencedRegulations", "Ustawa 1", "Ustawa 2"),
                fieldFactory.create("lawJournalEntryId", 55l, 56l));
        
        
        // judgment type
        Judgment typeJudgment = new CommonCourtJudgment();
        typeJudgment.setJudgmentType(JudgmentType.SENTENCE);

        List<SolrInputField> typeFields = Collections.singletonList(
                fieldFactory.create("judgmentType", "SENTENCE"));
        
        
        // judgment date
        Judgment dateJudgment = new CommonCourtJudgment();
        dateJudgment.setJudgmentDate(new LocalDate(2014, 9, 4));

        List<SolrInputField> dateFields = Collections.singletonList(
                fieldFactory.create("judgmentDate", "2014-09-04T00:00:00Z"));
        
        
        // text content
        Judgment contentJudgment = new CommonCourtJudgment();
        contentJudgment.setTextContent("some content");

        List<SolrInputField> contentFields = Collections.singletonList(
                fieldFactory.create("content", "some content"));

        //general
        Judgment generalJudgment = new CommonCourtJudgment();
        generalJudgment.addCourtCase(new CourtCase("ABC1A"));
        generalJudgment.addJudge(thirdJudge);
        generalJudgment.addLegalBase("art 1023 kc");
        generalJudgment.addReferencedRegulation(firstReferencedRegulation);
        generalJudgment.setJudgmentDate(new LocalDate(2014, 9, 4));
        generalJudgment.setJudgmentType(JudgmentType.SENTENCE);
        generalJudgment.setTextContent("some content");

        List<SolrInputField> generalFields =
            Lists.newArrayList(
                    fieldFactory.create("caseNumber", "ABC1A"),
                    fieldFactory.create("judge", "Adam Nowak"),
                    fieldFactory.create("judgeName", "Adam Nowak"),
                    fieldFactory.create("judgeWithRole_#_NO_ROLE", "Adam Nowak"),
                    fieldFactory.create("legalBases", "art 1023 kc"),
                    fieldFactory.create("referencedRegulations", "Ustawa 1"),
                    fieldFactory.create("judgmentDate", "2014-09-04T00:00:00Z"),
                    fieldFactory.create("judgmentType", "SENTENCE"),
                    fieldFactory.create("content", "some content")

            );

        
        
        return new Object[][] {
            { simpleJudgment, simpleFields },
            { signatureJudgment, signatureFields },
            { judgesJudgment, judgesFields },
            { legalBasesJudgment, legalBasesFields },
            { referencedRegulationsJudgment, referencedRegulationsFields },
            { typeJudgment, typeFields },
            { dateJudgment, dateFields },
            { contentJudgment, contentFields },
            { generalJudgment, generalFields}
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
