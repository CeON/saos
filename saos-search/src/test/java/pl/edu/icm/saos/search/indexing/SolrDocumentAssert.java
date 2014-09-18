package pl.edu.icm.saos.search.indexing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;

public class SolrDocumentAssert {

    private SolrDocumentAssert() { }
    
    public static void assertNoFields(SolrInputDocument doc) {
        Collection<String> fieldNames = doc.getFieldNames();
        
        assertTrue(fieldNames.isEmpty());
    }
    
    public static void assertSingleField(SolrInputDocument doc, String fieldName, String fieldValue) {
        List<String> fieldNames = new ArrayList<String>(doc.getFieldNames());
        
        assertEquals(1, fieldNames.size());
        assertEquals(fieldName, fieldNames.get(0));
        assertEquals(fieldValue, doc.getFieldValue(fieldName));
    }
    
    public static void assertFieldValue(SolrInputDocument doc, String fieldName, String fieldValue) {
        Collection<String> fieldNames = doc.getFieldNames();
        
        assertTrue(fieldNames.contains(fieldName));
        
        SolrInputField field = doc.getField(fieldName);
        assertEquals(1, field.getValueCount());
        assertEquals(fieldValue, field.getValue());
    }
    
    public static void assertFieldValues(SolrInputDocument doc, String fieldName, String ... fieldValues) {
        Collection<String> fieldNames = doc.getFieldNames();
        
        assertTrue(fieldNames.contains(fieldName));
        
        SolrInputField field = doc.getField(fieldName);
        assertEquals(fieldValues.length, field.getValueCount());
        Collection<Object> values = field.getValues();
        for (String expectedVal : fieldValues) {
            assertTrue(values.contains(expectedVal));
        }
    }

}
