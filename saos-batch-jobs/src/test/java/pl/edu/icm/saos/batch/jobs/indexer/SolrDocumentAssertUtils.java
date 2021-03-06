package pl.edu.icm.saos.batch.jobs.indexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.apache.solr.common.SolrDocument;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * @author madryk
 */
class SolrDocumentAssertUtils {

    
    //------------------------ CONSTRUCTORS --------------------------
    
    private SolrDocumentAssertUtils() { }
    
    
    
    //------------------------ LOGIC --------------------------
    
    public static void assertSolrDocumentValues(SolrDocument doc, JudgmentIndexField field, List<String> fieldValues) {
        assertSolrDocumentValues(doc, field, fieldValues.toArray(new String[] { }));
    }
    
    public static void assertSolrDocumentValues(SolrDocument doc, JudgmentIndexField field, String ... fieldValues) {
        String fieldName = field.getFieldName();
        assertSolrDocumentValues(doc, fieldName, fieldValues);
    }
    
    public static void assertSolrDocumentPostfixedFieldValues(SolrDocument doc, JudgmentIndexField field, String postfix, List<String> fieldValues) {
        assertSolrDocumentPostfixedFieldValues(doc, field, postfix, fieldValues.toArray(new String[] { }));
    }
    
    public static void assertSolrDocumentPostfixedFieldValues(SolrDocument doc, JudgmentIndexField field, String postfix, String ... fieldValues) {
        String fieldName = field.getFieldName() + "_#_" + postfix;
        assertSolrDocumentValues(doc, fieldName, fieldValues);
    }
    
    public static void assertSolrDocumentLongValues(SolrDocument doc, JudgmentIndexField field, List<Long> fieldValues) {
        long[] fieldValuesArray = new long[fieldValues.size()];
        for (int i=0; i<fieldValues.size(); ++i) {
            fieldValuesArray[i] = fieldValues.get(i);
        }
        assertSolrDocumentLongValues(doc, field, fieldValuesArray);
    }
    
    public static void assertSolrDocumentLongValues(SolrDocument doc, JudgmentIndexField field, long ... fieldValues) {
        String fieldName =  field.getFieldName();
        if (fieldValues.length == 0) {
            assertFalse(doc.getFieldNames().contains(fieldName));
            return;
        }
        assertTrue(doc.getFieldNames().contains(fieldName));
        
        Collection<Object> vals = doc.getFieldValues(fieldName);
        assertNotNull(vals);
        assertEquals(fieldValues.length, vals.size());
        for (long expectedVal : fieldValues) {
            assertTrue("Field " + fieldName + " doesn't contain value " + expectedVal, vals.contains(expectedVal));
        }
    }
    
    public static void assertFieldNotExists(SolrDocument doc, JudgmentIndexField field) {
        assertTrue(!doc.getFieldNames().contains(field.getFieldName()));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private static void assertSolrDocumentValues(SolrDocument doc, String fieldName, String ... fieldValues) {
        if (fieldValues.length == 0) {
            assertFalse(doc.getFieldNames().contains(fieldName));
            return;
        }
        assertTrue(doc.getFieldNames().contains(fieldName));
        
        Collection<Object> vals = doc.getFieldValues(fieldName);
        assertNotNull(vals);
        assertEquals(fieldValues.length, vals.size());
        for (String expectedVal : fieldValues) {
            assertTrue("Field " + fieldName + " doesn't contain value " + expectedVal, vals.contains(expectedVal));
        }
    }
}
