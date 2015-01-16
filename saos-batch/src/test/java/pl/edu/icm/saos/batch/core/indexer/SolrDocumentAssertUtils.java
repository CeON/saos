package pl.edu.icm.saos.batch.core.indexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.apache.solr.common.SolrDocument;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * @author madryk
 */
class SolrDocumentAssertUtils {

    
    //------------------------ CONSTRUCTORS --------------------------
    
    private SolrDocumentAssertUtils() { }
    
    
    
    //------------------------ LOGIC --------------------------
    
    public static void assertSolrDocumentValues(SolrDocument doc, JudgmentIndexField field, String ... fieldValues) {
        String fieldName = field.getFieldName();
        assertSolrDocumentValues(doc, fieldName, fieldValues);
    }
    
    public static void assertSolrDocumentPostfixedFieldValues(SolrDocument doc, JudgmentIndexField field, String postfix, String ... fieldValues) {
        String fieldName = field.getFieldName() + "_#_" + postfix;
        assertSolrDocumentValues(doc, fieldName, fieldValues);
    }
    
    public static void assertSolrDocumentIntValues(SolrDocument doc, JudgmentIndexField field, int ... fieldValues) {
        String fieldName =  field.getFieldName();
        assertTrue(doc.getFieldNames().contains(fieldName));
        
        Collection<Object> vals = doc.getFieldValues(fieldName);
        assertNotNull(vals);
        assertEquals(fieldValues.length, vals.size());
        for (int expectedVal : fieldValues) {
            assertTrue("Field " + fieldName + " doesn't contain value " + expectedVal, vals.contains(expectedVal));
        }
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private static void assertSolrDocumentValues(SolrDocument doc, String fieldName, String ... fieldValues) {
        assertTrue(doc.getFieldNames().contains(fieldName));
        
        Collection<Object> vals = doc.getFieldValues(fieldName);
        assertNotNull(vals);
        assertEquals(fieldValues.length, vals.size());
        for (String expectedVal : fieldValues) {
            assertTrue("Field " + fieldName + " doesn't contain value " + expectedVal, vals.contains(expectedVal));
        }
    }
}
