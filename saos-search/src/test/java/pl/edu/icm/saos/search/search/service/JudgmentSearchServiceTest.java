package pl.edu.icm.saos.search.search.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.search.SearchTestConfiguration;
import pl.edu.icm.saos.search.search.model.JudgeResult;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.search.search.model.SupremeCourtChamberResult;

import com.google.common.collect.Lists;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * @author madryk
 */
@RunWith(DataProviderRunner.class)
@ContextConfiguration(classes={ SearchTestConfiguration.class })
@Category(SlowTest.class)
public class JudgmentSearchServiceTest {

    private final static String CONTENT_FIELD_FILE_1961 = "contentField1961.txt";
    private final static String CONTENT_FIELD_FILE_41808 = "contentField41808.txt";
    
    private TestContextManager testContextManager;
    
    @Autowired
    private JudgmentSearchService judgmentSearchService;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer judgmentsServer;
    
    @DataProvider
    public static Object[][] searchResultsListData() {
        
        return new Object[][] {
            { Lists.newArrayList(1961l, 41808l), JudgmentCriteriaBuilder.create().withAll("następuje").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("izba").build() },
            { Lists.newArrayList(22l), JudgmentCriteriaBuilder.create().withAll("constitutionalTribunalCaseNumber").build() },
            { Lists.newArrayList(23l), JudgmentCriteriaBuilder.create().withAll("meansOfAppeal").build() },
            { Lists.newArrayList(23l), JudgmentCriteriaBuilder.create().withAll("judgmentresult").build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withAll("other").build() },
            
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("przedawnienie").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("przedawnienia").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("przedawnieniu").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("przedawnieniem").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("przedawnień").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("przedawnieniom").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("przedawnieniami").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("przedawnieniach").build() },
            
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("zaniechać").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("zaniechałem").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("zaniechałeś").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("zaniechał").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("zaniechała").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("zaniechało").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("zaniechaliśmy").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("zaniechaliście").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("zaniechali").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withAll("zaniechały").build() },
            
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withAll("AND").build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withAll("OR").build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withAll("NOT").build() },
            
            { Lists.newArrayList(1961l), JudgmentCriteriaBuilder.create().withDateRange(new LocalDate(2012, 5, 15), new LocalDate(2012, 5, 15)).build() },
            { Lists.newArrayList(41808l), JudgmentCriteriaBuilder.create().withDateFrom(new LocalDate(2014, 9, 3)).build() },
            { Lists.newArrayList(1961l, 41808l), JudgmentCriteriaBuilder.create().withDateTo(new LocalDate(2014, 9, 3)).build() },
            
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withKeyword("przestępstwo").build() },
            { Lists.newArrayList(41808l), JudgmentCriteriaBuilder.create().withKeyword("przestępstwo przeciwko wolności").build() },
            { Lists.newArrayList(41808l), JudgmentCriteriaBuilder.create().withKeyword("Przestępstwo Przeciwko Wolności").build() },
            { Lists.newArrayList(1961l, 41808l), JudgmentCriteriaBuilder.create().withKeyword("słowo kluczowe").build() },
            { Lists.newArrayList(41808l), JudgmentCriteriaBuilder.create()
                    .withKeyword("Przestępstwo Przeciwko Wolności").withKeyword("słowo kluczowe").build() },
            
            { Lists.newArrayList(1961l), JudgmentCriteriaBuilder.create().withJudgeName("Jacek Witkowski").build() },
            { Lists.newArrayList(1961l), JudgmentCriteriaBuilder.create().withJudgeName("Witkowski").build() },
            { Lists.newArrayList(1961l), JudgmentCriteriaBuilder.create().withJudgeName("Witkowski Kunecka").build() },
            { Lists.newArrayList(1961l), JudgmentCriteriaBuilder.create().withJudgeName("Elżbieta Kunecka").build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withJudgeName("\"Irena Kunecka\"").build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withJudgeName("Ewelina Kunecka").build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withJudgeName("Adam Nowak").build() },
            
            { Lists.newArrayList(41808l), JudgmentCriteriaBuilder.create().withLegalBase("art. 227 kk").build() },
            
            { Lists.newArrayList(1961l, 41808l), JudgmentCriteriaBuilder.create().withReferencedRegulation("kodeks").build() },
            { Lists.newArrayList(1961l), JudgmentCriteriaBuilder.create().withReferencedRegulation("1964").build() },
            { Lists.newArrayList(1961l, 41808l), JudgmentCriteriaBuilder.create().withReferencedRegulation("ustawa").build() },
            
            { Lists.newArrayList(41808l), JudgmentCriteriaBuilder.create().withLawJournalEntryId(502).build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withLawJournalEntryId(510).build() },
            
            { Lists.newArrayList(22l, 23l), JudgmentCriteriaBuilder.create().withReferencedCourtCaseId(58).build() },
            { Lists.newArrayList(23l), JudgmentCriteriaBuilder.create().withReferencedCourtCaseId(59).build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withReferencedCourtCaseId(60).build() },
            
            { Lists.newArrayList(41808l), JudgmentCriteriaBuilder.create().withCaseNumber("XV K 792/13").build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withCaseNumber("XV").build() },
            
            { Lists.newArrayList(1961l, 41808l), JudgmentCriteriaBuilder.create().withJudgmentType(JudgmentType.SENTENCE).build() },
            { Lists.newArrayList(21l, 1961l, 41808l), JudgmentCriteriaBuilder.create()
                    .withJudgmentType(JudgmentType.SENTENCE).withJudgmentType(JudgmentType.RESOLUTION).build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withJudgmentType(JudgmentType.DECISION).build() },
            
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withCourtType(CourtType.SUPREME).build() },
            { Lists.newArrayList(1961l, 41808l), JudgmentCriteriaBuilder.create().withCourtType(CourtType.COMMON).build() },
            
            { Lists.newArrayList(41808l), JudgmentCriteriaBuilder.create().withCcCourtType(CommonCourtType.DISTRICT).build() },
            { Lists.newArrayList(1961l), JudgmentCriteriaBuilder.create().withCcCourtType(CommonCourtType.APPEAL).build() },
            
            { Lists.newArrayList(41808l), JudgmentCriteriaBuilder.create().withCcCourtId(36).build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withCcCourtId(37).build() },

            { Lists.newArrayList(1961l), JudgmentCriteriaBuilder.create().withCcCourtCode("15500000").build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withCcCourtCode("15505000").build() },
            
            { Lists.newArrayList(1961l), JudgmentCriteriaBuilder.create().withCcCourtName("Sąd Apelacyjny we Wrocławiu").build() },
            
            { Lists.newArrayList(1961l), JudgmentCriteriaBuilder.create().withCcDivisionId(3).build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withCcDivisionId(4).build() },
            
            { Lists.newArrayList(1961l), JudgmentCriteriaBuilder.create().withCcDivisionCode("0001521").build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withCcDivisionCode("0001522").build() },
            
            { Lists.newArrayList(1961l), JudgmentCriteriaBuilder.create().withCcDivisionName("III Wydział Pracy i Ubezpieczeń Społecznych").build() },
            
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withScJudgmentFormId(12l).build() },
            
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withScPersonnelType(PersonnelType.ONE_PERSON).build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withScPersonnelType(PersonnelType.JOINED_CHAMBERS).build() },
            
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withScChamberId(13).build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withScChamberId(12).build() },
            
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withScChamberName("chamber").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withScChamberName("Izba Cywilna").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withScChamberName("Izba Pracy").build() },
            
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withScChamberDivisionId(112).build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withScChamberDivisionId(111).build() },
            
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withScChamberDivisionName("division").build() },
            { Lists.newArrayList(21l), JudgmentCriteriaBuilder.create().withScChamberDivisionName("Wydział III").build() },
            
            { Lists.newArrayList(22l), JudgmentCriteriaBuilder.create().withCtDissentingOpinion("first").build() },
            { Lists.newArrayList(22l, 23l), JudgmentCriteriaBuilder.create().withCtDissentingOpinion("text OR first").build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withCtDissentingOpinion("text first").build() },
            { Lists.newArrayList(22l), JudgmentCriteriaBuilder.create().withCtDissentingOpinion("Kowalski").build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withCtDissentingOpinion("other").build() },
            { Lists.newArrayList(22l), JudgmentCriteriaBuilder.create().withCtDissentingOpinionAuthor("Kowalski").build() },
            { Lists.newArrayList(), JudgmentCriteriaBuilder.create().withCtDissentingOpinionAuthor("first").build() },
        };
    }
    
    @Before
    public void setUp() throws Exception {
        testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
        
        judgmentsServer.deleteByQuery("*:*");
        judgmentsServer.commit();
        
        indexJudgments();
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    @UseDataProvider("searchResultsListData")
    public void search_CHECK_RESULTS_LIST(List<Long> expectedResultsIds, JudgmentCriteria criteria) {
        // execute
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        // assert
        long expectedResultsCount = expectedResultsIds.size();
        assertEquals(expectedResultsCount, results.getTotalResults());
        expectedResultsIds.forEach(id -> assertContainsResultWithId(results, id));
    }
    
    @Test
    public void search_CHECK_RESULT() {
        // given
        JudgmentCriteria criteria = JudgmentCriteriaBuilder.create().withCaseNumber("III AUa 271/12").build();
        
        // execute
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        // assert
        JudgmentSearchResult result = fetchAndAssertSingleSearchResult(results);
        assertEquals(1961, result.getId());
        
        assertEquals(1, result.getCaseNumbers().size());
        assertEquals("III AUa 271/12", result.getCaseNumbers().get(0));
        assertEquals(JudgmentType.SENTENCE, result.getJudgmentType());
        
        assertEquals(new BigDecimal("12300.23"), result.getMaxMoneyAmount());
        
        LocalDate expectedDate = new LocalDate(2012, 5, 15);
        assertEquals(expectedDate, result.getJudgmentDate());
        
        assertThat(result.getJudges(), containsInAnyOrder(
                new JudgeResult("Jacek Witkowski", JudgeRole.PRESIDING_JUDGE),
                new JudgeResult("Elżbieta Kunecka"),
                new JudgeResult("Irena Różańska-Dorosz")));
        
        assertThat(result.getKeywords(), containsInAnyOrder("zwrot nienależnie pobranych świadczeń z ubezpieczenia", "słowo kluczowe"));
        
        
        assertTrue(result.getContent().contains("W IMIENIU RZECZYPOSPOLITEJ POLSKIEJ"));
        assertTrue(result.getContent().contains("SSA Irena Różańska-Dorosz (spr.)"));
        assertTrue(result.getContent().contains("o zwrot nienależnie pobranego świadczenia"));
        assertFalse(result.getContent().contains("<p>"));
        assertFalse(result.getContent().contains("</p>"));
        assertFalse(result.getContent().contains("anon-block"));
        assertTrue(result.getContent().length() <= 800);
    }
    
    @Test
    public void search_CHECK_COMMON_COURT_RESULT() {
        // given
        JudgmentCriteria criteria = JudgmentCriteriaBuilder.create().withCaseNumber("III AUa 271/12").build();
        
        // execute
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        // assert
        JudgmentSearchResult result = fetchAndAssertSingleSearchResult(results);
        assertEquals(1961, result.getId());
        
        assertEquals(1l, result.getCcCourtId().longValue());
        assertEquals("15500000", result.getCcCourtCode());
        assertEquals("Sąd Apelacyjny we Wrocławiu", result.getCcCourtName());
        
        assertEquals(3l, result.getCcCourtDivisionId().longValue());
        assertEquals("0001521", result.getCcCourtDivisionCode());
        assertEquals("III Wydział Pracy i Ubezpieczeń Społecznych", result.getCcCourtDivisionName());
    }
    
    @Test
    public void search_CHECK_SUPREME_COURT_RESULT() {
        // given
        JudgmentCriteria criteria = JudgmentCriteriaBuilder.create().withCaseNumber("supremeCaseNumber").build();
        
        // execute
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        // assert
        JudgmentSearchResult result = fetchAndAssertSingleSearchResult(results);
        assertEquals(21, result.getId());
        
        assertEquals(12l, result.getScJudgmentFormId().longValue());
        assertEquals("Postanowienie SN", result.getScJudgmentFormName());
        assertEquals(PersonnelType.JOINED_CHAMBERS,result.getScPersonnelType());
        assertTrue(result.getScCourtChambers().contains(new SupremeCourtChamberResult(11, "Izba Cywilna")));
        assertTrue(result.getScCourtChambers().contains(new SupremeCourtChamberResult(12, "Izba Pracy")));
        assertEquals(111l, result.getScCourtDivisionId().longValue());
        assertEquals("Wydział III", result.getScCourtDivisionName());
        assertEquals(11l, result.getScCourtDivisionsChamberId().longValue());
        assertEquals("Izba Cywilna", result.getScCourtDivisionsChamberName());
    }
    
    @Test
    public void search_CHECK_HIGHLIGHTING() {
        // given
        JudgmentCriteria criteria = new JudgmentCriteria("świadków");
        
        // execute
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        // assert
        JudgmentSearchResult result = fetchAndAssertSingleSearchResult(results);
        assertEquals(41808, result.getId());
        
        Pattern highlightPattern = Pattern.compile("<em>\\S+</em>");
        Matcher highlightMatcher = highlightPattern.matcher(result.getContent());
        List<String> highlights = Lists.newArrayList();
        while (highlightMatcher.find()) {
            highlights.add(highlightMatcher.group());
        }
        assertThat(highlights, contains("<em>świadków</em>", "<em>Świadkowie</em>", "<em>świadkowie</em>", "<em>świadek</em>"));
        assertEquals(3, StringUtils.countMatches(result.getContent(), " ... "));
    }
    
    @Test
    public void search_CHECK_HIGHLIGHTING_REQUIRED_FIELD_MATCH() {
        // given
        JudgmentCriteria criteria = new JudgmentCriteria("content");
        criteria.setScCourtChamberId(11l);
        
        // execute
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        // assert
        JudgmentSearchResult result = fetchAndAssertSingleSearchResult(results);
        assertEquals(21, result.getId());
        
        assertTrue(result.getContent().contains("11"));
        assertFalse(result.getContent().contains("<em>11</em>"));
    }
    
    @Test
    public void search_CHECK_HIGHLIGHTING_NO_LENGTH_LIMIT() {
        // given
        JudgmentCriteria criteria = new JudgmentCriteria("lastword");
        
        // execute
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        // assert
        JudgmentSearchResult result = fetchAndAssertSingleSearchResult(results);
        assertEquals(22, result.getId());
        
        assertTrue(result.getContent().contains("<em>lastword</em>"));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertContainsResultWithId(SearchResults<JudgmentSearchResult> results, long id) {
        
        for (JudgmentSearchResult result : results.getResults()) {
            if (result.getId() == id) {
                return;
            }
        }
        fail("results doesn't contain judgment with id " + id);
    }
    
    private JudgmentSearchResult fetchAndAssertSingleSearchResult(SearchResults<JudgmentSearchResult> results) {
        assertEquals(1, results.getTotalResults());
        assertEquals(1, results.getResults().size());
        
        return results.getResults().get(0);
    }
    
    private void indexJudgments() throws SolrServerException, IOException {
        judgmentsServer.add(fetchFirstCcJudgmentDoc());
        judgmentsServer.add(fetchSecondCcJudgmentDoc());
        judgmentsServer.add(fetchScJudgmentDoc());
        judgmentsServer.add(fetchfirstCtJudgmentDoc());
        judgmentsServer.add(fetchSecondCtJudgmentDoc());
        
        judgmentsServer.commit();
    }
    
    private SolrInputDocument fetchFirstCcJudgmentDoc() throws IOException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("databaseId", "41808");
        doc.addField("caseNumber", "XV K 792/13");
        
        doc.addField("judgmentDate", "2014-09-03T00:00:00Z");
        doc.addField("judgmentType", "SENTENCE");
        
        doc.addField("legalBases", "art. 193 kk");
        doc.addField("legalBases", "art. 227 kk");
        
        String firstRR = "Ustawa z dnia 23 czerwca 1973 r. o opłatach w sprawach karnych (Dz. U. z 1973 r. Nr 27, poz. 152 - art. 3; art. 3 ust. 1)";
        String secondRR = "Ustawa z dnia 6 czerwca 1997 r. - Kodeks karny (Dz. U. z 1997 r. Nr 88, poz. 553 - art. 11; art. 11 § 2; art. 11 § 3; art. 193; art. 227; art. 31; art. 31 § 1; art. 31 § 2; art. 44; art. 44 § 2; art. 53)";
        String thirdRR = "Ustawa z dnia 6 czerwca 1997 r. - Kodeks postępowania karnego (Dz. U. z 1997 r. Nr 89, poz. 555 - art. 627)";
        
        doc.addField("referencedRegulations", firstRR);
        doc.addField("referencedRegulations", secondRR);
        doc.addField("referencedRegulations", thirdRR);
        
        doc.addField("lawJournalEntryId", "501");
        doc.addField("lawJournalEntryId", "502");
        doc.addField("lawJournalEntryId", "503");
        
        doc.addField("courtType", "COMMON");
        doc.addField("ccCourtType", "DISTRICT");
        doc.addField("ccCourtId", "36");
        doc.addField("ccCourtCode", "15050505");
        doc.addField("ccCourtName", "Sąd Rejonowy w Białymstoku");
        doc.addField("ccCourtDivisionId", "213");
        doc.addField("ccCourtDivisionCode", "0007506");
        doc.addField("ccCourtDivisionName", "XV Wydział Karny");
        
        doc.addField("judge", "Marcin Kęska|PRESIDING_JUDGE");
        doc.addField("judgeName", "Marcin Kęska");
        doc.addField("judgeWithRole_#_PRESIDING_JUDGE", "Marcin Kęska");
        
        doc.addField("keyword", "przestępstwo przeciwko wolności");
        doc.addField("keyword", "przestępstwo przeciwko porządkowi publicznemu");
        doc.addField("keyword", "słowo kluczowe");
        
        try (InputStream inputStream = new ClassPathResource(CONTENT_FIELD_FILE_41808).getInputStream()) {
            doc.addField("content", IOUtils.toString(inputStream, "UTF-8"));
        }
        
        return doc;
    }
    
    private SolrInputDocument fetchSecondCcJudgmentDoc() throws IOException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("databaseId", "1961");
        doc.addField("caseNumber", "III AUa 271/12");
        
        doc.addField("judgmentDate", "2012-05-15T00:00:00Z");
        doc.addField("judgmentType", "SENTENCE");
        
        doc.addField("maximumMoneyAmount", new BigDecimal("12300.23"));

        doc.addField("judge", "Jacek Witkowski|PRESIDING_JUDGE");
        doc.addField("judge", "Elżbieta Kunecka");
        doc.addField("judge", "Irena Różańska-Dorosz");
        doc.addField("judgeName", "Jacek Witkowski");
        doc.addField("judgeName", "Elżbieta Kunecka");
        doc.addField("judgeName", "Irena Różańska-Dorosz");
        doc.addField("judgeWithRole_#_PRESIDING_JUDGE", "Jacek Witkowski");
        doc.addField("judgeWithRole_#_NO_ROLE", "Elżbieta Kunecka");
        doc.addField("judgeWithRole_#_NO_ROLE", "Irena Różańska-Dorosz");
        
        String firstRR = "Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296 - art. 385)";
        String secondRR = "Dekret z dnia 8 października 1946 r. - Prawo spadkowe (Dz. U. z 1946 r. Nr 60, poz. 328 - )";
        String thirdRR = "Ustawa z dnia 29 sierpnia 1997 r. - Prawo bankowe (Dz. U. z 1997 r. Nr 140, poz. 939 - art. 55; art. 55 ust. 1; art. 55 ust. 1 pkt. 2; art. 55 ust. 3; art. 56)";
        String fourthRR = "Ustawa z dnia 23 kwietnia 1964 r. - Kodeks cywilny (Dz. U. z 1964 r. Nr 16, poz. 93 - art. 101; art. 101 § 2)";
        doc.addField("referencedRegulations", firstRR);
        doc.addField("referencedRegulations", secondRR);
        doc.addField("referencedRegulations", thirdRR);
        doc.addField("referencedRegulations", fourthRR);
        
        doc.addField("lawJournalEntryId", "505");
        doc.addField("lawJournalEntryId", "506");
        doc.addField("lawJournalEntryId", "507");
        doc.addField("lawJournalEntryId", "508");
        
        doc.addField("courtType", "COMMON");
        doc.addField("ccCourtType", "APPEAL");
        doc.addField("ccCourtId", "1");
        doc.addField("ccCourtCode", "15500000");
        doc.addField("ccCourtName", "Sąd Apelacyjny we Wrocławiu");
        doc.addField("ccCourtDivisionId", "3");
        doc.addField("ccCourtDivisionCode", "0001521");
        doc.addField("ccCourtDivisionName", "III Wydział Pracy i Ubezpieczeń Społecznych");
        
        doc.addField("keyword", "zwrot nienależnie pobranych świadczeń z ubezpieczenia");
        doc.addField("keyword", "słowo kluczowe");
        
        
        try (InputStream inputStream = new ClassPathResource(CONTENT_FIELD_FILE_1961).getInputStream()) {
            doc.addField("content", IOUtils.toString(inputStream, "UTF-8"));
        }
        
        return doc;
    }
    
    private SolrInputDocument fetchScJudgmentDoc() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("databaseId", "21");
        doc.addField("caseNumber", "supremeCaseNumber");
        
        doc.addField("judgmentType", "RESOLUTION");
        
        doc.addField("scJudgmentFormId", 12l);
        doc.addField("scJudgmentFormName", "Postanowienie SN");
        doc.addField("scPersonnelType", "JOINED_CHAMBERS");
        doc.addField("courtType", "SUPREME");
        doc.addField("scCourtChamber", "11|Izba Cywilna");
        doc.addField("scCourtChamber", "12|Izba Pracy");
        doc.addField("scCourtChamberId", "11");
        doc.addField("scCourtChamberName", "Izba Cywilna");
        doc.addField("scCourtChamberId", "12");
        doc.addField("scCourtChamberName", "Izba Pracy");
        doc.addField("scCourtChamberDivisionId", "111");
        doc.addField("scCourtChamberDivisionName", "Wydział III");
        doc.addField("scCourtDivisionsChamberId", "11");
        doc.addField("scCourtDivisionsChamberName", "Izba Cywilna");
        
        doc.addField("content", "someContent 11 content 12 111 przedawnienia zaniechał");
        
        return doc;
    }
    
    private SolrInputDocument fetchfirstCtJudgmentDoc() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("databaseId", "22");
        doc.addField("caseNumber", "constitutionalTribunalCaseNumber");
        
        doc.addField("referencedCourtCasesIds", "57");
        doc.addField("referencedCourtCasesIds", "58");
        
        doc.addField("ctDissentingOpinion", "first dissenting opinion");
        doc.addField("ctDissentingOpinion", "Aleksander Kowalski");
        doc.addField("ctDissentingOpinion", "Jan Nowak");
        doc.addField("ctDissentingOpinion", "second dissenting opinion");
        doc.addField("ctDissentingOpinion", "Maciej Kamiński");
        doc.addField("ctDissentingOpinionAuthor", "Aleksander Kowalski");
        doc.addField("ctDissentingOpinionAuthor", "Jan Nowak");
        doc.addField("ctDissentingOpinionAuthor", "Maciej Kamiński");
        
        doc.addField("content", StringUtils.repeat('a', 100000) + " " + "lastword");
        
        return doc;
    }
    
    private SolrInputDocument fetchSecondCtJudgmentDoc() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("databaseId", "23");
        doc.addField("all", "meansOfAppeal field");
        doc.addField("all", "judgmentResult");
        doc.addField("caseNumber", "secondConstitutionalTribunalCaseNumber");
        
        doc.addField("referencedCourtCasesIds", "58");
        doc.addField("referencedCourtCasesIds", "59");
        
        doc.addField("ctDissentingOpinion", "text of dissenting opinion");
        doc.addField("ctDissentingOpinion", "Kazimierz Kozłowski");
        doc.addField("ctDissentingOpinionAuthor", "Kazimierz Kozłowski");
        
        return doc;
    }
    
}