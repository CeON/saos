package pl.edu.icm.saos.search.search.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.solr.common.SolrDocument;
import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * @author madryk
 */
public class SolrFieldFetcherTest {

    private SolrFieldFetcher<JudgmentIndexField> fieldFetcher = new SolrFieldFetcher<JudgmentIndexField>();
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void fetchValue() {
        SolrDocument doc = new SolrDocument();
        doc.addField("judge", "Jan Kowalski");
        
        String actual = fieldFetcher.fetchValue(doc, JudgmentIndexField.JUDGE);
        
        assertEquals("Jan Kowalski", actual);
    }
    
    @Test
    public void fetchLongValue() {
        SolrDocument doc = new SolrDocument();
        doc.addField("ccCourtId", 123l);
        
        Long actual = fieldFetcher.fetchLongValue(doc, JudgmentIndexField.CC_COURT_ID);
        
        assertEquals(123l, actual.longValue());
    }
    
    @Test
    public void fetchDateValue() {
        SolrDocument doc = new SolrDocument();
        Date date = new Date(1396310400000L); // 2014-04-01
        doc.addField("judgmentDate", date);
        
        LocalDate actualDate = fieldFetcher.fetchDateValue(doc, JudgmentIndexField.JUDGMENT_DATE);
        
        LocalDate expectedDate = new LocalDate(2014, 4, 1);
        assertEquals(expectedDate, actualDate);
    }
    
    @Test
    public void fetchValues() {
        SolrDocument doc = new SolrDocument();
        doc.addField("judge", "Jan Kowalski");
        doc.addField("judge", "Adam Nowak");
        
        List<String> actual = fieldFetcher.fetchValues(doc, JudgmentIndexField.JUDGE);
        
        assertEquals(2, actual.size());
        assertTrue(actual.contains("Jan Kowalski"));
        assertTrue(actual.contains("Adam Nowak"));
    }
    
    @Test
    public void fetchValues_FIELD_WITH_POSTFIX() {
        SolrDocument doc = new SolrDocument();
        doc.addField("judgeWithRole_#_PRESIDING_JUDGE", "Jan Kowalski");
        doc.addField("judgeWithRole_#_PRESIDING_JUDGE", "Jan Nowak");
        doc.addField("judgeWithRole_#_REPORTING_JUDGE", "Adam Nowak");
        
        List<String> actual = fieldFetcher.fetchValues(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "PRESIDING_JUDGE");
        
        assertEquals(2, actual.size());
        assertTrue(actual.contains("Jan Kowalski"));
        assertTrue(actual.contains("Jan Nowak"));
    }
    
    @Test
    public void fetchValuesWithAttributes() {
        SolrDocument doc = new SolrDocument();
        doc.addField("judge", "Jan Kowalski|PRESIDING_JUDGE|REPORTING_JUDGE");
        doc.addField("judge", "Adam Nowak|PRESIDING_JUDGE|UNKNOWN_ROLE");
        
        List<Pair<String, List<String>>> actual = fieldFetcher.fetchValuesWithAttributes(doc, JudgmentIndexField.JUDGE);
        
        assertEquals(2, actual.size());
        Pair<String, List<String>> actualFirstJudge = actual.get(0);
        Pair<String, List<String>> actualSecondJudge = actual.get(1);
        
        assertEquals("Jan Kowalski", actualFirstJudge.getLeft());
        assertEquals(2, actualFirstJudge.getRight().size());
        assertTrue(actualFirstJudge.getRight().contains("PRESIDING_JUDGE"));
        assertTrue(actualFirstJudge.getRight().contains("REPORTING_JUDGE"));
        
        assertEquals("Adam Nowak", actualSecondJudge.getLeft());
        assertEquals(2, actualSecondJudge.getRight().size());
        assertTrue(actualSecondJudge.getRight().contains("PRESIDING_JUDGE"));
        assertTrue(actualSecondJudge.getRight().contains("UNKNOWN_ROLE"));
    }
    
    @Test
    public void fetchValuesWithEnumedAttributes() {
        SolrDocument doc = new SolrDocument();
        doc.addField("judge", "Jan Kowalski|PRESIDING_JUDGE|REPORTING_JUDGE");
        doc.addField("judge", "Adam Nowak|PRESIDING_JUDGE|UNKNOWN_ROLE");
        
        List<Pair<String, List<JudgeRole>>> actual = fieldFetcher.fetchValuesWithEnumedAttributes(doc, JudgmentIndexField.JUDGE, JudgeRole.class);
        
        assertEquals(2, actual.size());
        Pair<String, List<JudgeRole>> actualFirstJudge = actual.get(0);
        Pair<String, List<JudgeRole>> actualSecondJudge = actual.get(1);
        
        assertEquals("Jan Kowalski", actualFirstJudge.getLeft());
        assertEquals(2, actualFirstJudge.getRight().size());
        assertTrue(actualFirstJudge.getRight().contains(JudgeRole.PRESIDING_JUDGE));
        assertTrue(actualFirstJudge.getRight().contains(JudgeRole.REPORTING_JUDGE));
        
        assertEquals("Adam Nowak", actualSecondJudge.getLeft());
        assertEquals(1, actualSecondJudge.getRight().size());
        assertTrue(actualSecondJudge.getRight().contains(JudgeRole.PRESIDING_JUDGE));
    }
    
}
