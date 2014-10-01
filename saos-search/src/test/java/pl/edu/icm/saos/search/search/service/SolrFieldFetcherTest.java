package pl.edu.icm.saos.search.search.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * @author madryk
 */
public class SolrFieldFetcherTest {

    private SolrFieldFetcher<JudgmentIndexField> fieldFetcher = new SolrFieldFetcher<JudgmentIndexField>();
    
    @Test
    public void fetchValue() {
        SolrDocument doc = new SolrDocument();
        doc.addField("judge", "Jan Kowalski");
        
        String actual = fieldFetcher.fetchValue(doc, JudgmentIndexField.JUDGE);
        
        assertEquals("Jan Kowalski", actual);
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
    
}
