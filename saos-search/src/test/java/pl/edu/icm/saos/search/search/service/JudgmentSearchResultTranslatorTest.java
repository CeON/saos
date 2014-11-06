package pl.edu.icm.saos.search.search.service;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.solr.common.SolrDocument;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
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
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void translateSingle() {
        SolrDocument doc = new SolrDocument();
        
        doc.addField("id", "ID");
        doc.addField("databaseId", 1);
        doc.addField("content", "some content");
        doc.addField("caseNumber", "AAAB1A");
        
        GregorianCalendar calendar = new GregorianCalendar(2014, 9, 7);
        doc.addField("judgmentDate", calendar.getTime());
        doc.addField("judgmentType", "SENTENCE");
        doc.addField("legalBases", "art. 1234 kc");
        doc.addField("referencedRegulations", "Ustawa 1");
        doc.addField("referencedRegulations", "Ustawa 2");
        
        doc.addField("courtType", "APPEAL");
        
        doc.addField("keyword", "some keyword");
        doc.addField("keyword", "some other keyword");
        
        doc.addField("judge", "Jan Kowalski|PRESIDING_JUDGE|REPORTING_JUDGE");
        doc.addField("judge", "Jacek Zieliński|REPORTING_JUDGE");
        doc.addField("judge", "Adam Nowak");
        
        
        JudgmentSearchResult result = resultsTranslator.translateSingle(doc);
        
        
        Assert.assertEquals(1, result.getId());
        Assert.assertEquals(1, result.getCaseNumbers().size());
        Assert.assertTrue(result.getCaseNumbers().contains("AAAB1A"));
        
        Assert.assertEquals(new LocalDate(2014, 10, 7), result.getJudgmentDate());
        Assert.assertEquals("SENTENCE", result.getJudgmentType());
        
        Assert.assertEquals(2, result.getKeywords().size());
        Assert.assertTrue(result.getKeywords().contains("some keyword"));
        Assert.assertTrue(result.getKeywords().contains("some other keyword"));
        
        Assert.assertEquals(3, result.getJudges().size());
        Assert.assertTrue(result.getJudges().contains(new JudgeResult("Jan Kowalski", JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE)));
        Assert.assertTrue(result.getJudges().contains(new JudgeResult("Jacek Zieliński", JudgeRole.REPORTING_JUDGE)));
        Assert.assertTrue(result.getJudges().contains(new JudgeResult("Adam Nowak")));
    }
    
    @Test
    public void translateSingle_COMMON_COURT() {
        SolrDocument doc = new SolrDocument();
        doc.addField("databaseId", 1);
        
        doc.addField("ccCourtId", 123);
        doc.addField("ccCourtCode", "15200000");
        doc.addField("ccCourtName", "Sąd Apelacyjny w Krakowie");
        doc.addField("ccCourtDivisionId", 816);
        doc.addField("ccCourtDivisionCode", "0000503");
        doc.addField("ccCourtDivisionName", "I Wydział Cywilny");
        
        JudgmentSearchResult result = resultsTranslator.translateSingle(doc);
        
        Assert.assertEquals(Integer.valueOf(123), result.getCourtId());
        Assert.assertEquals("15200000", result.getCourtCode());
        Assert.assertEquals("Sąd Apelacyjny w Krakowie", result.getCourtName());

        Assert.assertEquals(Integer.valueOf(816), result.getCourtDivisionId());
        Assert.assertEquals("0000503", result.getCourtDivisionCode());
        Assert.assertEquals("I Wydział Cywilny", result.getCourtDivisionName());
    }
    
    @Test
    public void translateSingle_SUPREME_COURT() {
        SolrDocument doc = new SolrDocument();
        doc.addField("databaseId", 1);
        
        doc.addField("scPersonnelType", PersonnelType.JOINED_CHAMBERS.name());
        doc.addField("scCourtChamber", "11|Izba Cywilna");
        doc.addField("scCourtChamber", "12|Izba Pracy");
        doc.addField("scCourtChamberDivisionId", 111);
        doc.addField("scCourtChamberDivisionName", "Izba Cywilna Wydział III");
        
        JudgmentSearchResult result = resultsTranslator.translateSingle(doc);
        
        Assert.assertEquals("JOINED_CHAMBERS", result.getPersonnelType());
        Assert.assertTrue(result.getCourtChambers().contains(new SupremeCourtChamberResult(11, "Izba Cywilna")));
        Assert.assertTrue(result.getCourtChambers().contains(new SupremeCourtChamberResult(12, "Izba Pracy")));
        Assert.assertEquals(2, result.getCourtChambers().size());
        Assert.assertEquals(Integer.valueOf(111), result.getCourtChamberDivisionId());
        Assert.assertEquals("Izba Cywilna Wydział III", result.getCourtChamberDivisionName());
    }
    
    @Test
    public void translateSingle_SUPREME_COURT_defective_court_chamber() {
    	SolrDocument doc = new SolrDocument();
    	
    	doc.addField("databaseId", 1);
        doc.addField("scCourtChamber", "11");
    	
        JudgmentSearchResult result = resultsTranslator.translateSingle(doc);

        Assert.assertEquals(0, result.getCourtChambers().size());
    }
    
    @Test
    public void applyHighlighting() {
        Map<String, List<String>> docHighlighting = StringListMap.of(new String[][] {
                { "content", "first fragment", "second fragment" }
        });
        
        JudgmentSearchResult result = new JudgmentSearchResult();
        resultsTranslator.applyHighlighting(docHighlighting, result);
        
        Assert.assertEquals("first fragment ... second fragment", result.getContent());
    }
    
}
