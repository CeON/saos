package pl.edu.icm.saos.search.indexing;

import org.apache.solr.common.SolrInputField;

/**
 * @author madryk
 */
public class SolrInputFieldFactory {

    
    //------------------------ LOGIC --------------------------
    
    public SolrInputField create(String fieldName, Object ... fieldValues) {
        SolrInputField inputField = new SolrInputField(fieldName);
        for (Object fieldValue : fieldValues) {
            inputField.addValue(fieldValue, 1);
        }
        return inputField;
    }

}
