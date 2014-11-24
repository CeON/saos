package pl.edu.icm.saos.search.search.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.search.SearchTestConfiguration;
import pl.edu.icm.saos.search.search.model.CourtType;
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
    public static Object[][] searchResultsCountData() {
        
        return new Object[][] {
            { Lists.newArrayList(1961, 41808), new JudgmentCriteriaBuilder("następuje").build() },
            { Lists.newArrayList(), new JudgmentCriteriaBuilder("other").build() },
            
            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withDateRange(new LocalDate(2012, 5, 15), new LocalDate(2012, 5, 15)).build() },
            { Lists.newArrayList(41808), new JudgmentCriteriaBuilder().withDateFrom(new LocalDate(2014, 9, 3)).build() },
            { Lists.newArrayList(1961, 41808), new JudgmentCriteriaBuilder().withDateTo(new LocalDate(2014, 9, 3)).build() },
            
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withKeyword("przestępstwo").build() },
            { Lists.newArrayList(41808), new JudgmentCriteriaBuilder().withKeyword("przestępstwo przeciwko wolności").build() },
            { Lists.newArrayList(41808), new JudgmentCriteriaBuilder().withKeyword("Przestępstwo Przeciwko Wolności").build() },
            { Lists.newArrayList(1961, 41808), new JudgmentCriteriaBuilder().withKeyword("słowo kluczowe").build() },
            { Lists.newArrayList(41808), new JudgmentCriteriaBuilder()
                    .withKeyword("Przestępstwo Przeciwko Wolności").withKeyword("słowo kluczowe").build() },
            
            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withJudgeName("Jacek Witkowski").build() },
            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withJudgeName("Witkowski").build() },
            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withJudgeName("Elżbieta Kunecka").build() },
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withJudgeName("Adam Nowak").build() },
            
            { Lists.newArrayList(41808), new JudgmentCriteriaBuilder().withLegalBase("art. 227 kk").build() },
            
            { Lists.newArrayList(1961, 41808), new JudgmentCriteriaBuilder().withReferencedRegulation("kodeks").build() },
            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withReferencedRegulation("1964").build() },
            { Lists.newArrayList(1961, 41808), new JudgmentCriteriaBuilder().withReferencedRegulation("ustawa").build() },
            
            { Lists.newArrayList(41808), new JudgmentCriteriaBuilder().withCaseNumber("XV K 792/13").build() },
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withCaseNumber("XV").build() },
            
            { Lists.newArrayList(1961, 41808), new JudgmentCriteriaBuilder().withJudgmentType(JudgmentType.SENTENCE).build() },
            { Lists.newArrayList(21, 1961, 41808), new JudgmentCriteriaBuilder()
                    .withJudgmentType(JudgmentType.SENTENCE).withJudgmentType(JudgmentType.RESOLUTION).build() },
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withJudgmentType(JudgmentType.DECISION).build() },
            
            { Lists.newArrayList(21), new JudgmentCriteriaBuilder().withCourtType(CourtType.SUPREME).build() },
            { Lists.newArrayList(1961, 41808), new JudgmentCriteriaBuilder().withCourtType(CourtType.COMMON).build() },
            
            { Lists.newArrayList(41808), new JudgmentCriteriaBuilder().withCcCourtType(CommonCourtType.DISTRICT).build() },
            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withCcCourtType(CommonCourtType.APPEAL).build() },
            
            { Lists.newArrayList(41808), new JudgmentCriteriaBuilder().withCcCourtId(36).build() },
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withCcCourtId(37).build() },

            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withCcCourtCode("15500000").build() },
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withCcCourtCode("15505000").build() },
            
            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withCcCourtName("Sąd Apelacyjny we Wrocławiu").build() },
            
            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withCcDivisionId(3).build() },
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withCcDivisionId(4).build() },
            
            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withCcDivisionCode("0001521").build() },
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withCcDivisionCode("0001522").build() },
            
            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withCcDivisionName("III Wydział Pracy i Ubezpieczeń Społecznych").build() },
            
            
            
            
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withScPersonnelType(PersonnelType.ONE_PERSON).build() },
            { Lists.newArrayList(21), new JudgmentCriteriaBuilder().withScPersonnelType(PersonnelType.JOINED_CHAMBERS).build() },
            
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withScChamberId(13).build() },
            { Lists.newArrayList(21), new JudgmentCriteriaBuilder().withScChamberId(12).build() },
            
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withScChamberName("chamber").build() },
            { Lists.newArrayList(21), new JudgmentCriteriaBuilder().withScChamberName("Izba Cywilna").build() },
            { Lists.newArrayList(21), new JudgmentCriteriaBuilder().withScChamberName("Izba Pracy").build() },
            
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withScChamberDivisionId(112).build() },
            { Lists.newArrayList(21), new JudgmentCriteriaBuilder().withScChamberDivisionId(111).build() },
            
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withScChamberDivisionName("division").build() },
            { Lists.newArrayList(21), new JudgmentCriteriaBuilder().withScChamberDivisionName("Izba Cywilna Wydział III").build() },
            
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
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    @UseDataProvider("searchResultsCountData")
    public void search_CHECK_RESULTS_COUNT(List<Integer> expectedResultsIds, JudgmentCriteria criteria) {
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        int expectedResultsCount = expectedResultsIds.size();
        assertEquals(expectedResultsCount, results.getTotalResults());
        expectedResultsIds.forEach(id -> assertContainsResultWithId(results, id));
    }
    
    @Test
    public void search_CHECK_RESULT() {
        JudgmentCriteria criteria = new JudgmentCriteriaBuilder().withCaseNumber("III AUa 271/12").build();
        
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        assertEquals(1, results.getTotalResults());
        assertEquals(1, results.getResults().size());
        
        JudgmentSearchResult result = results.getResults().get(0);
        assertEquals(1961, result.getId());
        
        assertEquals(1, result.getCaseNumbers().size());
        assertEquals("III AUa 271/12", result.getCaseNumbers().get(0));
        assertEquals("SENTENCE", result.getJudgmentType());
        
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
        JudgmentCriteria criteria = new JudgmentCriteriaBuilder().withCaseNumber("III AUa 271/12").build();
        
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        assertEquals(1, results.getTotalResults());
        assertEquals(1, results.getResults().size());
        
        JudgmentSearchResult result = results.getResults().get(0);
        assertEquals(1961, result.getId());
        
        assertEquals(Integer.valueOf(1), result.getCourtId());
        assertEquals("15500000", result.getCourtCode());
        assertEquals("Sąd Apelacyjny we Wrocławiu", result.getCourtName());
        
        assertEquals(Integer.valueOf(3), result.getCourtDivisionId());
        assertEquals("0001521", result.getCourtDivisionCode());
        assertEquals("III Wydział Pracy i Ubezpieczeń Społecznych", result.getCourtDivisionName());
    }
    
    @Test
    public void search_CHECK_SUPREME_COURT_RESULT() {
        JudgmentCriteria criteria = new JudgmentCriteriaBuilder().withCaseNumber("supremeCaseNumber").build();
        
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        assertEquals(1, results.getTotalResults());
        assertEquals(1, results.getResults().size());
        
        JudgmentSearchResult result = results.getResults().get(0);
        assertEquals(21, result.getId());
        
        assertEquals("wyrok SN", result.getScJudgmentForm() );
        assertEquals("JOINED_CHAMBERS", result.getPersonnelType());
        assertTrue(result.getCourtChambers().contains(new SupremeCourtChamberResult(11, "Izba Cywilna")));
        assertTrue(result.getCourtChambers().contains(new SupremeCourtChamberResult(12, "Izba Pracy")));
        assertEquals(Integer.valueOf(111), result.getCourtChamberDivisionId());
        assertEquals("Izba Cywilna Wydział III", result.getCourtChamberDivisionName());
    }
    
    @Test
    public void search_CHECK_HIGHLIGHTING() {
        JudgmentCriteria criteria = new JudgmentCriteria("świadków");
        
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        assertEquals(1, results.getTotalResults());
        assertEquals(1, results.getResults().size());
        
        JudgmentSearchResult result = results.getResults().get(0);
        assertEquals(41808, result.getId());
        
        assertEquals(4, StringUtils.countMatches(result.getContent(), "<em>świadków</em>"));
        assertEquals(3, StringUtils.countMatches(result.getContent(), " ... "));
    }
    
    @Test
    public void search_CHECK_HIGHLIGHTING_REQUIRED_FIELD_MATCH() {
        JudgmentCriteria criteria = new JudgmentCriteria("content");
        criteria.setScCourtChamberId(11);
        
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        assertEquals(1, results.getTotalResults());
        assertEquals(1, results.getResults().size());
        
        JudgmentSearchResult result = results.getResults().get(0);
        assertEquals(21, result.getId());
        
        assertTrue(result.getContent().contains("11"));
        assertFalse(result.getContent().contains("<em>11</em>"));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertContainsResultWithId(SearchResults<JudgmentSearchResult> results, int id) {
        
        for (JudgmentSearchResult result : results.getResults()) {
            if (result.getId() == id) {
                return;
            }
        }
        fail("results doesn't contain judgment with id " + id);
    }
    
    private void indexJudgments() throws SolrServerException, IOException {
        judgmentsServer.add(fetchFirstCcJudgmentDoc());
        judgmentsServer.add(fetchSecondCcJudgmentDoc());
        judgmentsServer.add(fetchScJudgmentDoc());
        
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
        
        String firstRR = "Ustawa z dnia 23 czerwca 1973 r. o opłatach w sprawach karnych (Dz. U. z 1973 r. Nr 27, poz. 152 - art. 3; art. 3 ust. 1)"; // id: 140018
        String secondRR = "Ustawa z dnia 6 czerwca 1997 r. - Kodeks karny (Dz. U. z 1997 r. Nr 88, poz. 553 - art. 11; art. 11 § 2; art. 11 § 3; art. 193; art. 227; art. 31; art. 31 § 1; art. 31 § 2; art. 44; art. 44 § 2; art. 53)"; // id: 140019
        String thirdRR = "Ustawa z dnia 6 czerwca 1997 r. - Kodeks postępowania karnego (Dz. U. z 1997 r. Nr 89, poz. 555 - art. 627)"; // id: 140020
        
        doc.addField("referencedRegulations", firstRR);
        doc.addField("referencedRegulations", secondRR);
        doc.addField("referencedRegulations", thirdRR);
        
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

        doc.addField("judge", "Jacek Witkowski|PRESIDING_JUDGE");
        doc.addField("judge", "Elżbieta Kunecka");
        doc.addField("judge", "Irena Różańska-Dorosz");
        doc.addField("judgeName", "Jacek Witkowski");
        doc.addField("judgeName", "Elżbieta Kunecka");
        doc.addField("judgeName", "Irena Różańska-Dorosz");
        doc.addField("judgeWithRole_#_PRESIDING_JUDGE", "Jacek Witkowski");
        doc.addField("judgeWithRole_#_NO_ROLE", "Elżbieta Kunecka");
        doc.addField("judgeWithRole_#_NO_ROLE", "Irena Różańska-Dorosz");
        
        String firstRR = "Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296 - art. 385)"; // id: 6810
        String secondRR = "Dekret z dnia 8 października 1946 r. - Prawo spadkowe (Dz. U. z 1946 r. Nr 60, poz. 328 - )"; // id: 6811
        String thirdRR = "Ustawa z dnia 29 sierpnia 1997 r. - Prawo bankowe (Dz. U. z 1997 r. Nr 140, poz. 939 - art. 55; art. 55 ust. 1; art. 55 ust. 1 pkt. 2; art. 55 ust. 3; art. 56)"; // id: 6812
        String fourthRR = "Ustawa z dnia 23 kwietnia 1964 r. - Kodeks cywilny (Dz. U. z 1964 r. Nr 16, poz. 93 - art. 101; art. 101 § 2)"; // id: 6813
        doc.addField("referencedRegulations", firstRR);
        doc.addField("referencedRegulations", secondRR);
        doc.addField("referencedRegulations", thirdRR);
        doc.addField("referencedRegulations", fourthRR);
        
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
        
        doc.addField("scJudgmentForm", "wyrok SN");
        doc.addField("scPersonnelType", "JOINED_CHAMBERS");
        doc.addField("courtType", "SUPREME");
        doc.addField("scCourtChamber", "11|Izba Cywilna");
        doc.addField("scCourtChamber", "12|Izba Pracy");
        doc.addField("scCourtChamberId", "11");
        doc.addField("scCourtChamberName", "Izba Cywilna");
        doc.addField("scCourtChamberId", "12");
        doc.addField("scCourtChamberName", "Izba Pracy");
        doc.addField("scCourtChamberDivisionId", "111");
        doc.addField("scCourtChamberDivisionName", "Izba Cywilna Wydział III");
        
        doc.addField("content", "someContent 11 content 12 111");
        
        return doc;
    }
    
}