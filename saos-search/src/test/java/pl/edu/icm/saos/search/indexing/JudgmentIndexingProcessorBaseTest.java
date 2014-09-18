package pl.edu.icm.saos.search.indexing;

import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertNoFields;
import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertSingleField;

import org.apache.solr.common.SolrInputDocument;
import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * @author madryk
 */
public class JudgmentIndexingProcessorBaseTest {

    JudgmentIndexingProcessorBase processorBase = new JudgmentIndexingProcessorBase() { };
    
    @Test
    public void addField() {
        SolrInputDocument doc = new SolrInputDocument();
        
        processorBase.addField(doc, JudgmentIndexField.CASE_NUMBER, "ABC1A");
        
        assertSingleField(doc, "caseNumber", "ABC1A");
    }
    
    @Test
    public void addField_EMPTY() {
        SolrInputDocument doc = new SolrInputDocument();
        
        processorBase.addField(doc, JudgmentIndexField.CASE_NUMBER, null);
        processorBase.addField(doc, JudgmentIndexField.CASE_NUMBER, "");
        
        assertNoFields(doc);
    }
    
    @Test
    public void addField_DYNAMIC() {
        SolrInputDocument doc = new SolrInputDocument();
        
        processorBase.addField(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "PRESIDING_JUDGE", "Jan Kowalski");
        
        assertSingleField(doc, "judgeWithRole_#_PRESIDING_JUDGE", "Jan Kowalski");
    }
    
    @Test
    public void addField_DYNAMIC_NOT_ALLOWED() {
        SolrInputDocument doc = new SolrInputDocument();
        
        processorBase.addField(doc, JudgmentIndexField.ID, "postfix", "id");
        
        assertNoFields(doc);
    }
    
    @Test
    public void addDateField() {
        SolrInputDocument doc = new SolrInputDocument();
        
        processorBase.addDateField(doc, JudgmentIndexField.JUDGMENT_DATE, new LocalDate(2014, 9, 3));
        
        assertSingleField(doc, "judgmentDate", "2014-09-03T00:00:00Z");
    }
    
}
