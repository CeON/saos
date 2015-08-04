package pl.edu.icm.saos.search.indexing;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.common.TestInMemoryObjectFactory;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.JudgmentResult;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;
import pl.edu.icm.saos.persistence.model.MoneyAmount;
import pl.edu.icm.saos.persistence.model.ReferencedCourtCase;
import pl.edu.icm.saos.persistence.model.SourceCode;
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
        Judgment simpleJudgment = TestInMemoryObjectFactory.createSimpleCcJudgment();
        Whitebox.setInternalState(simpleJudgment, "id", 1);
        
        List<SolrInputField> simpleFields = Lists.newArrayList(
                fieldFactory.create("databaseId", 1l),
                fieldFactory.create("sourceCode", SourceCode.COMMON_COURT.name()),
                fieldFactory.create("courtType", CourtType.COMMON.name()));
        
        
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
        firstLawJournalEntry.setYear(1964);
        firstLawJournalEntry.setJournalNo(43);
        firstLawJournalEntry.setEntry(296);
        firstReferencedRegulation.setRawText("Ustawa 1");
        firstReferencedRegulation.setLawJournalEntry(firstLawJournalEntry);
        
        
        JudgmentReferencedRegulation secondReferencedRegulation = new JudgmentReferencedRegulation();
        LawJournalEntry secondLawJournalEntry = new LawJournalEntry();
        
        Whitebox.setInternalState(secondLawJournalEntry, "id", 56);
        secondLawJournalEntry.setYear(1999);
        secondLawJournalEntry.setJournalNo(110);
        secondLawJournalEntry.setEntry(1257);
        secondReferencedRegulation.setRawText("Ustawa 2");
        secondReferencedRegulation.setLawJournalEntry(secondLawJournalEntry);

        referencedRegulationsJudgment.addReferencedRegulation(firstReferencedRegulation);
        referencedRegulationsJudgment.addReferencedRegulation(secondReferencedRegulation);
        
        
        List<SolrInputField> referencedRegulationsFields = Lists.newArrayList(
                fieldFactory.create("referencedRegulations", "Ustawa 1", "Ustawa 2"),
                fieldFactory.create("lawJournalEntryId", 55l, 56l),
                fieldFactory.create("lawJournalEntryCode", "1964/296", "1999/1257"));
        
        
        // referenced court cases
        Judgment referencedCourtCasesJudgment = new CommonCourtJudgment();
        
        ReferencedCourtCase firstReferencedCourtCase = new ReferencedCourtCase();
        firstReferencedCourtCase.setCaseNumber("AAAB");
        firstReferencedCourtCase.setJudgmentIds(Lists.newArrayList(12L, 15L));
        
        ReferencedCourtCase secondReferencedCourtCase = new ReferencedCourtCase();
        secondReferencedCourtCase.setCaseNumber("AAAC");
        secondReferencedCourtCase.setJudgmentIds(Lists.newArrayList());
        
        referencedCourtCasesJudgment.addReferencedCourtCase(firstReferencedCourtCase);
        referencedCourtCasesJudgment.addReferencedCourtCase(secondReferencedCourtCase);
        
        
        List<SolrInputField> referencedCourtCasesFields = Collections.singletonList(
                fieldFactory.create("referencedCourtCasesIds", 12L, 15L));
        
        
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
        
        
        // means of appeal
        Judgment meansOfAppealJudgment = new CommonCourtJudgment();
        meansOfAppealJudgment.setMeansOfAppeal(new MeansOfAppeal(CourtType.COMMON, "kasacja z urzędu"));
        
        List<SolrInputField> meansOfAppealFields = Collections.singletonList(
                fieldFactory.create("all", "kasacja z urzędu"));
        
        
        // judgment result
        Judgment judgmentResultJudgment = new CommonCourtJudgment();
        judgmentResultJudgment.setJudgmentResult(new JudgmentResult(CourtType.COMMON, "pozostawiono bez rozpoznania"));
        
        List<SolrInputField> judgmentResultFields = Collections.singletonList(
                fieldFactory.create("all", "pozostawiono bez rozpoznania"));
        
        // text content
        Judgment contentJudgment = new CommonCourtJudgment();
        contentJudgment.getTextContent().setRawTextContent("some content");

        List<SolrInputField> contentFields = Collections.singletonList(
                fieldFactory.create("content", "some content"));
        
        
        // max money amount
        Judgment maxMoneyJudgment = new CommonCourtJudgment();
        MoneyAmount moneyAmount = new MoneyAmount();
        moneyAmount.setAmount(new BigDecimal("12300.23"));
        maxMoneyJudgment.setMaxMoneyAmount(moneyAmount);
        
        List<SolrInputField> maxMoneyFields = Collections.singletonList(
                fieldFactory.create("maximumMoneyAmount", new BigDecimal("12300.23")));

        //general
        Judgment generalJudgment = TestInMemoryObjectFactory.createSimpleScJudgment();
        generalJudgment.addJudge(thirdJudge);
        generalJudgment.addLegalBase("art 1023 kc");
        generalJudgment.addReferencedRegulation(firstReferencedRegulation);
        generalJudgment.setJudgmentDate(new LocalDate(2014, 9, 4));
        generalJudgment.setJudgmentType(JudgmentType.SENTENCE);
        generalJudgment.setMeansOfAppeal(new MeansOfAppeal(CourtType.SUPREME, "kasacja z urzędu"));
        generalJudgment.setJudgmentResult(new JudgmentResult(CourtType.SUPREME, "pozostawiono bez rozpoznania"));
        generalJudgment.getTextContent().setRawTextContent("some content");
        generalJudgment.setMaxMoneyAmount(moneyAmount);

        List<SolrInputField> generalFields =
            Lists.newArrayList(
                    fieldFactory.create("sourceCode", SourceCode.SUPREME_COURT.name()),
                    fieldFactory.create("courtType", CourtType.SUPREME.name()),
                    fieldFactory.create("caseNumber", generalJudgment.getCaseNumbers().get(0)),
                    fieldFactory.create("judge", "Adam Nowak"),
                    fieldFactory.create("judgeName", "Adam Nowak"),
                    fieldFactory.create("judgeWithRole_#_NO_ROLE", "Adam Nowak"),
                    fieldFactory.create("legalBases", "art 1023 kc"),
                    fieldFactory.create("referencedRegulations", "Ustawa 1"),
                    fieldFactory.create("lawJournalEntryId", 55L),
                    fieldFactory.create("lawJournalEntryCode", "1964/296"),
                    fieldFactory.create("judgmentDate", "2014-09-04T00:00:00Z"),
                    fieldFactory.create("judgmentType", "SENTENCE"),
                    fieldFactory.create("content", "some content"),
                    fieldFactory.create("maximumMoneyAmount", new BigDecimal("12300.23")),
                    fieldFactory.create("all", "kasacja z urzędu", "pozostawiono bez rozpoznania")
            );

        
        
        return new Object[][] {
            { simpleJudgment, simpleFields },
            { signatureJudgment, signatureFields },
            { judgesJudgment, judgesFields },
            { legalBasesJudgment, legalBasesFields },
            { referencedRegulationsJudgment, referencedRegulationsFields },
            { referencedCourtCasesJudgment, referencedCourtCasesFields },
            { typeJudgment, typeFields },
            { dateJudgment, dateFields },
            { contentJudgment, contentFields },
            { meansOfAppealJudgment, meansOfAppealFields },
            { judgmentResultJudgment, judgmentResultFields },
            { maxMoneyJudgment, maxMoneyFields },
            { generalJudgment, generalFields}
        };
    }
    
    @Before
    public void setUp() {
        doCallRealMethod().when(judgmentIndexFieldsFiller).fillFields(any(), any());
        doCallRealMethod().when(judgmentIndexFieldsFiller).setFieldAdder(any());
        judgmentIndexFieldsFiller.setFieldAdder(fieldAdder);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    @UseDataProvider("judgmentsFieldsData")
    public void fillFields(Judgment givenJudgment, List<SolrInputField> expectedFields) {
        // given
        SolrInputDocument doc = new SolrInputDocument();
        JudgmentIndexingData indexingData = new JudgmentIndexingData();
        indexingData.setJudgment(givenJudgment);
        
        // execute
        judgmentIndexFieldsFiller.fillFields(doc, indexingData);
        
        // assert
        expectedFields.forEach(expectedField -> assertFieldValues(doc, expectedField));
    }
    
    @Test
    public void fillFields_REFERENCING_COUNT() {
        // given
        SolrInputFieldFactory fieldFactory = new SolrInputFieldFactory();
        SolrInputDocument doc = new SolrInputDocument();
        JudgmentIndexingData indexingData = new JudgmentIndexingData();
        indexingData.setJudgment(new CommonCourtJudgment());
        indexingData.setReferencingCount(4);
        
        
        // execute
        judgmentIndexFieldsFiller.fillFields(doc, indexingData);
        
        // assert
        assertFieldValues(doc, fieldFactory.create("referencingJudgmentsCount", 4L));
    }

}
