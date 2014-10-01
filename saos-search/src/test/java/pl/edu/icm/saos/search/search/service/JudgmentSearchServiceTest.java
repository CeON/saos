package pl.edu.icm.saos.search.search.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.search.SearchTestConfiguration;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;

import com.google.common.collect.Lists;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * @author madryk
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(DataProviderRunner.class)
@ContextConfiguration(classes={ SearchTestConfiguration.class })
@Category(SlowTest.class)
public class JudgmentSearchServiceTest {

    private TestContextManager testContextManager;
    
    @Autowired
    private JudgmentSearchService judgmentSearchService;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer judgmentsServer;
    
    @DataProvider
    public static Object[][] searchResultsCountData() {
        
        return new Object[][] {
            { Lists.newArrayList(1961, 41808), new JudgmentCriteriaBuilder("content").build() },
            { Lists.newArrayList(), new JudgmentCriteriaBuilder("other").build() },
            
            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withDateRange(new LocalDate(2012, 5, 15), new LocalDate(2012, 5, 15)).build() },
            { Lists.newArrayList(41808), new JudgmentCriteriaBuilder().withDateFrom(new LocalDate(2014, 9, 3)).build() },
            { Lists.newArrayList(1961, 41808), new JudgmentCriteriaBuilder().withDateTo(new LocalDate(2014, 9, 3)).build() },
            
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withKeyword("przestępstwo").build() },
            { Lists.newArrayList(41808), new JudgmentCriteriaBuilder().withKeyword("przestępstwo przeciwko wolności").build() },
            { Lists.newArrayList(41808), new JudgmentCriteriaBuilder().withKeyword("Przestępstwo Przeciwko Wolności").build() },
            
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
            
            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withCourtId("15500000").build() },
            { Lists.newArrayList(), new JudgmentCriteriaBuilder().withCourtId("15505000").build() },
            
            { Lists.newArrayList(1961), new JudgmentCriteriaBuilder().withCourtName("Sąd Apelacyjny we Wrocławiu").build() },
            
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
    
    @Test
    @UseDataProvider("searchResultsCountData")
    public void search_CHECK_RESULTS_COUNT(List<Integer> expectedResultsIds, JudgmentCriteria criteria) {
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        int expectedResultsCount = expectedResultsIds.size();
        assertEquals(expectedResultsCount, results.getTotalResults());
        expectedResultsIds.forEach(id -> assertContainsResultWithId(results, String.valueOf(id)));
    }
    
    @Test
    public void search_CHECK_RESULT() {
        JudgmentCriteria criteria = new JudgmentCriteriaBuilder().withCaseNumber("III AUa 271/12").build();
        
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        assertEquals(1, results.getTotalResults());
        assertEquals(1, results.getResults().size());
        
        JudgmentSearchResult result = results.getResults().get(0);
        assertEquals("1961", result.getId());
        
        assertEquals(1, result.getCaseNumbers().size());
        assertEquals("III AUa 271/12", result.getCaseNumbers().get(0));
        assertEquals("SENTENCE", result.getJudgmentType());
        
        LocalDate expectedDate = new LocalDate(2012, 5, 15);
        assertEquals(expectedDate, result.getJudgmentDate());
        
        assertEquals(3, result.getJudges().size());
        assertTrue(result.getJudges().contains("Jacek Witkowski"));
        assertTrue(result.getJudges().contains("Elżbieta Kunecka"));
        assertTrue(result.getJudges().contains("Irena Różańska-Dorosz"));
        
        assertEquals("Sąd Apelacyjny we Wrocławiu", result.getCourtName());
        assertEquals("III Wydział Pracy i Ubezpieczeń Społecznych", result.getCourtDivisionName());
        
        assertEquals(1, result.getKeywords().size());
        assertEquals("zwrot nienależnie pobranych świadczeń z ubezpieczenia", result.getKeywords().get(0));
        
        assertEquals("this is content", result.getContent());
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertContainsResultWithId(SearchResults<JudgmentSearchResult> results, String id) {
        
        for (JudgmentSearchResult result : results.getResults()) {
            if (StringUtils.equals(id, result.getId())) {
                return;
            }
        }
        fail("results doesn't contain judgment with id " + id);
    }
    
    private void indexJudgments() throws SolrServerException, IOException {
        judgmentsServer.add(fetchFirstDoc());
        judgmentsServer.add(fetchSecondDoc());
        
        judgmentsServer.commit();
    }
    
    private SolrInputDocument fetchFirstDoc() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", UUID.randomUUID());
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
        
        doc.addField("courtType", "DISTRICT");
        doc.addField("courtId", "15050505"); // id: 36
        doc.addField("courtName", "Sąd Rejonowy w Białymstoku");
        doc.addField("courtDivisionId", "0007506"); // id: 213
        doc.addField("courtDivisionName", "XV Wydział Karny");
        
        doc.addField("judgeWithRole_#_PRESIDING_JUDGE", "Marcin Kęska");
        
        doc.addField("keyword", "przestępstwo przeciwko wolności");
        doc.addField("keyword", "przestępstwo przeciwko porządkowi publicznemu");
        doc.addField("content", "some content");
        
        return doc;
    }
    
    private SolrInputDocument fetchSecondDoc() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", UUID.randomUUID());
        doc.addField("databaseId", "1961");
        doc.addField("caseNumber", "III AUa 271/12");
        
        doc.addField("judgmentDate", "2012-05-15T00:00:00Z");
        doc.addField("judgmentType", "SENTENCE");

        doc.addField("judgeWithRole_#_PRESIDING_JUDGE", "Jacek Witkowski");
        doc.addField("judge", "Elżbieta Kunecka");
        doc.addField("judge", "Irena Różańska-Dorosz");
        
        String firstRR = "Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296 - art. 385)"; // id: 6810
        String secondRR = "Dekret z dnia 8 października 1946 r. - Prawo spadkowe (Dz. U. z 1946 r. Nr 60, poz. 328 - )"; // id: 6811
        String thirdRR = "Ustawa z dnia 29 sierpnia 1997 r. - Prawo bankowe (Dz. U. z 1997 r. Nr 140, poz. 939 - art. 55; art. 55 ust. 1; art. 55 ust. 1 pkt. 2; art. 55 ust. 3; art. 56)"; // id: 6812
        String fourthRR = "Ustawa z dnia 23 kwietnia 1964 r. - Kodeks cywilny (Dz. U. z 1964 r. Nr 16, poz. 93 - art. 101; art. 101 § 2)"; // id: 6813
        doc.addField("referencedRegulations", firstRR);
        doc.addField("referencedRegulations", secondRR);
        doc.addField("referencedRegulations", thirdRR);
        doc.addField("referencedRegulations", fourthRR);
        
        doc.addField("courtType", "APPEAL");
        doc.addField("courtId", "15500000"); // id: 1
        doc.addField("courtName", "Sąd Apelacyjny we Wrocławiu");
        doc.addField("courtDivisionId", "0001521"); // id: 3
        doc.addField("courtDivisionName", "III Wydział Pracy i Ubezpieczeń Społecznych");
        
        doc.addField("keyword", "zwrot nienależnie pobranych świadczeń z ubezpieczenia");
        doc.addField("content", "this is content");
        
        return doc;
    }
    
}