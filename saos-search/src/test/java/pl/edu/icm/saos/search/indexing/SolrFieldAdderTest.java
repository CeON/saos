package pl.edu.icm.saos.search.indexing;

import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertNoFields;
import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertSingleField;

import java.math.BigDecimal;

import org.apache.solr.common.SolrInputDocument;
import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class SolrFieldAdderTest {

    private SolrFieldAdder<JudgmentIndexField> fieldAdder = new SolrFieldAdder<JudgmentIndexField>();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void addField() {
        // given
        SolrInputDocument doc = new SolrInputDocument();
        
        // execute
        fieldAdder.addField(doc, JudgmentIndexField.CASE_NUMBER, "ABC1A");
        
        // assert
        assertSingleField(doc, "caseNumber", "ABC1A");
    }
    
    @Test
    public void addField_EMPTY() {
        // given
        SolrInputDocument doc = new SolrInputDocument();
        
        // execute
        fieldAdder.addField(doc, JudgmentIndexField.CASE_NUMBER, (String)null);
        fieldAdder.addField(doc, JudgmentIndexField.CASE_NUMBER, "");
        
        // assert
        assertNoFields(doc);
    }
    
    @Test
    public void addField_DYNAMIC() {
        // given
        SolrInputDocument doc = new SolrInputDocument();
        
        // execute
        fieldAdder.addField(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "PRESIDING_JUDGE", "Jan Kowalski");
        
        // assert
        assertSingleField(doc, "judgeWithRole_#_PRESIDING_JUDGE", "Jan Kowalski");
    }
    
    @Test
    public void addCompositeField() {
        // given
        SolrInputDocument doc = new SolrInputDocument();
        
        // execute
        fieldAdder.addCompositeField(doc, JudgmentIndexField.JUDGE,
                Lists.newArrayList("Jan Kowalski", "PRESIDING_JUDGE", "REPORTING_JUDGE"));
        
        // assert
        assertSingleField(doc, "judge", "Jan Kowalski|PRESIDING_JUDGE|REPORTING_JUDGE");
    }
    
    @Test
    public void addCompositeField_SINGLE() {
        // given
        SolrInputDocument doc = new SolrInputDocument();
        
        // execute
        fieldAdder.addCompositeField(doc, JudgmentIndexField.JUDGE, Lists.newArrayList("Jan Kowalski"));
        
        // assert
        assertSingleField(doc, "judge", "Jan Kowalski");
    }
    
    @Test
    public void addDateField() {
        // given
        SolrInputDocument doc = new SolrInputDocument();
        
        // execute
        fieldAdder.addDateField(doc, JudgmentIndexField.JUDGMENT_DATE, new LocalDate(2014, 9, 3));
        
        // assert
        assertSingleField(doc, "judgmentDate", "2014-09-03T00:00:00Z");
    }
    
    @Test
    public void addCurrencyField() {
        // given
        SolrInputDocument doc = new SolrInputDocument();
        
        // execute
        fieldAdder.addCurrencyField(doc, JudgmentIndexField.MAXIMUM_MONEY_AMOUNT, new BigDecimal("12300.45"));
        
        // assert
        assertSingleField(doc, "maximumMoneyAmount", new BigDecimal("12300.45"));
    }
    
}
