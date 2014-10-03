package pl.edu.icm.saos.search.indexing;

import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertNoFields;
import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertSingleField;

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
    
    @Test
    public void addField() {
        SolrInputDocument doc = new SolrInputDocument();
        
        fieldAdder.addField(doc, JudgmentIndexField.CASE_NUMBER, "ABC1A");
        
        assertSingleField(doc, "caseNumber", "ABC1A");
    }
    
    @Test
    public void addField_EMPTY() {
        SolrInputDocument doc = new SolrInputDocument();
        
        fieldAdder.addField(doc, JudgmentIndexField.CASE_NUMBER, null);
        fieldAdder.addField(doc, JudgmentIndexField.CASE_NUMBER, "");
        
        assertNoFields(doc);
    }
    
    @Test
    public void addField_DYNAMIC() {
        SolrInputDocument doc = new SolrInputDocument();
        
        fieldAdder.addField(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "PRESIDING_JUDGE", "Jan Kowalski");
        
        assertSingleField(doc, "judgeWithRole_#_PRESIDING_JUDGE", "Jan Kowalski");
    }
    
    public void addFieldWithAttributes() {
        SolrInputDocument doc = new SolrInputDocument();
        
        fieldAdder.addFieldWithAttributes(doc, JudgmentIndexField.JUDGE, "Jan Kowalski",
                Lists.newArrayList("PRESIDING_JUDGE", "REPORTING_JUDGE"));
        
        assertSingleField(doc, "judge", "Jan Kowalski|PRESIDING_JUDGE|REPORTING_JUDGE");
    }
    
    public void addFieldWithAttributes_NO_ATTRIBUTES() {
        SolrInputDocument doc = new SolrInputDocument();
        
        fieldAdder.addFieldWithAttributes(doc, JudgmentIndexField.JUDGE, "Jan Kowalski", Lists.newArrayList());
        
        assertSingleField(doc, "judge", "Jan Kowalski");
    }
    
    @Test
    public void addDateField() {
        SolrInputDocument doc = new SolrInputDocument();
        
        fieldAdder.addDateField(doc, JudgmentIndexField.JUDGMENT_DATE, new LocalDate(2014, 9, 3));
        
        assertSingleField(doc, "judgmentDate", "2014-09-03T00:00:00Z");
    }
    
}
