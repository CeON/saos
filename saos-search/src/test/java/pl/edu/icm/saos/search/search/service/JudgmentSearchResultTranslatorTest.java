package pl.edu.icm.saos.search.search.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.search.StringListMap;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.JudgeResult;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SupremeCourtChamberResult;


/**
 * @author madryk
 */
public class JudgmentSearchResultTranslatorTest {

    private JudgmentSearchResultTranslator resultsTranslator = new JudgmentSearchResultTranslator();
    
    private SolrFieldFetcher<JudgmentIndexField> fieldFetcher = new SolrFieldFetcher<JudgmentIndexField>();
    
    private SolrHighlightFragmentsMerger<JudgmentIndexField> highlightFragmentsMerger = new SolrHighlightFragmentsMerger<JudgmentIndexField>();
    
    
    @Before
    public void setUp() {
        resultsTranslator.setFieldFetcher(fieldFetcher);
        resultsTranslator.setHighlightFragmentsMerger(highlightFragmentsMerger);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void translateSingle() {
        // given
        SolrDocument doc = new SolrDocument();
        
        doc.addField("id", "ID");
        doc.addField("databaseId", 1l);
        doc.addField("content", "some content");
        doc.addField("caseNumber", "AAAB1A");
        
        GregorianCalendar calendar = new GregorianCalendar(2014, 9, 7);
        doc.addField("judgmentDate", calendar.getTime());
        doc.addField("judgmentType", "SENTENCE");
        doc.addField("legalBases", "art. 1234 kc");
        doc.addField("referencedRegulations", "Ustawa 1");
        doc.addField("referencedRegulations", "Ustawa 2");
        doc.addField("referencingJudgmentsCount", 23L);
        
        doc.addField("maximumMoneyAmount", "12300.45,PLN");
        
        doc.addField("courtType", "COMMON");
        
        doc.addField("keyword", "some keyword");
        doc.addField("keyword", "some other keyword");
        
        doc.addField("judge", "Jan Kowalski|PRESIDING_JUDGE|REPORTING_JUDGE");
        doc.addField("judge", "Jacek Zieliński|REPORTING_JUDGE");
        doc.addField("judge", "Adam Nowak");
        
        
        // execute
        JudgmentSearchResult result = resultsTranslator.translateSingle(doc);
        
        
        // assert
        assertEquals(1, result.getId());
        assertEquals(1, result.getCaseNumbers().size());
        assertTrue(result.getCaseNumbers().contains("AAAB1A"));
        
        assertEquals(new LocalDate(2014, 10, 7), result.getJudgmentDate());
        assertEquals(JudgmentType.SENTENCE, result.getJudgmentType());
        assertEquals(CourtType.COMMON, result.getCourtType());
        
        assertEquals(23L, result.getReferencingCount());
        
        assertEquals(new BigDecimal("12300.45"), result.getMaxMoneyAmount());
        
        assertEquals(2, result.getKeywords().size());
        assertTrue(result.getKeywords().contains("some keyword"));
        assertTrue(result.getKeywords().contains("some other keyword"));
        
        assertEquals(3, result.getJudges().size());
        assertTrue(result.getJudges().contains(new JudgeResult("Jan Kowalski", JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE)));
        assertTrue(result.getJudges().contains(new JudgeResult("Jacek Zieliński", JudgeRole.REPORTING_JUDGE)));
        assertTrue(result.getJudges().contains(new JudgeResult("Adam Nowak")));
    }
    
    @Test
    public void translateSingle_COMMON_COURT() {
        // given
        SolrDocument doc = new SolrDocument();
        doc.addField("databaseId", 1l);
        
        doc.addField("ccCourtId", 123l);
        doc.addField("ccCourtCode", "15200000");
        doc.addField("ccCourtName", "Sąd Apelacyjny w Krakowie");
        doc.addField("ccCourtDivisionId", 816l);
        doc.addField("ccCourtDivisionCode", "0000503");
        doc.addField("ccCourtDivisionName", "I Wydział Cywilny");
        
        
        // execute
        JudgmentSearchResult result = resultsTranslator.translateSingle(doc);
        
        
        // assert
        assertEquals(123l, result.getCcCourtId().longValue());
        assertEquals("15200000", result.getCcCourtCode());
        assertEquals("Sąd Apelacyjny w Krakowie", result.getCcCourtName());

        assertEquals(816l, result.getCcCourtDivisionId().longValue());
        assertEquals("0000503", result.getCcCourtDivisionCode());
        assertEquals("I Wydział Cywilny", result.getCcCourtDivisionName());
    }
    
    @Test
    public void translateSingle_SUPREME_COURT() {
        // given
        SolrDocument doc = new SolrDocument();
        doc.addField("databaseId", 1l);
        
        doc.addField("scJudgmentForm", "wyrok SN");
        doc.addField("scPersonnelType", PersonnelType.JOINED_CHAMBERS.name());
        doc.addField("scCourtChamber", "11|Izba Cywilna");
        doc.addField("scCourtChamber", "12|Izba Pracy");
        doc.addField("scCourtChamberDivisionId", 111l);
        doc.addField("scCourtChamberDivisionName", "Wydział III");
        doc.addField("scCourtDivisionsChamberId", 11l);
        doc.addField("scCourtDivisionsChamberName", "Izba Cywilna");
        
        
        // execute
        JudgmentSearchResult result = resultsTranslator.translateSingle(doc);
        
        
        // assert
        assertEquals("wyrok SN", result.getScJudgmentForm());
        assertEquals(PersonnelType.JOINED_CHAMBERS, result.getScPersonnelType());
        assertTrue(result.getScCourtChambers().contains(new SupremeCourtChamberResult(11, "Izba Cywilna")));
        assertTrue(result.getScCourtChambers().contains(new SupremeCourtChamberResult(12, "Izba Pracy")));
        assertEquals(2, result.getScCourtChambers().size());
        assertEquals(111l, result.getScCourtDivisionId().longValue());
        assertEquals("Wydział III", result.getScCourtDivisionName());
        assertEquals(11l, result.getScCourtDivisionsChamberId().longValue());
        assertEquals("Izba Cywilna", result.getScCourtDivisionsChamberName());
    }
    
    @Test
    public void translateSingle_SUPREME_COURT_defective_court_chamber() {
        // given
    	SolrDocument doc = new SolrDocument();
    	
    	doc.addField("databaseId", 1l);
        doc.addField("scCourtChamber", "11");
        
        // execute
        JudgmentSearchResult result = resultsTranslator.translateSingle(doc);

        // assert
        assertEquals(0, result.getScCourtChambers().size());
    }
    
    @Test
    public void applyHighlighting() {
        // given
        Map<String, List<String>> docHighlighting = StringListMap.of(new String[][] {
                { "content", "first fragment", "second fragment" }
        });
        JudgmentSearchResult result = new JudgmentSearchResult();

        // execute
        resultsTranslator.applyHighlighting(docHighlighting, result);
        
        // assert
        assertEquals("first fragment ... second fragment", result.getContent());
    }
    
}
