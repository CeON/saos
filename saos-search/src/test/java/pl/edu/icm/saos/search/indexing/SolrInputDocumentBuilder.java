package pl.edu.icm.saos.search.indexing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;

public class SolrInputDocumentBuilder {

    private Map<String, List<String>> fields = new HashMap<String, List<String>>();
    
    public SolrInputDocument build() {
        SolrInputDocument doc = new SolrInputDocument();
        for (Map.Entry<String, List<String>> fieldsEntry : fields.entrySet()) {
            String fieldName = fieldsEntry.getKey();
            for (String fieldValue : fieldsEntry.getValue()) {
                doc.addField(fieldName, fieldValue);
            }
        }
        return doc;
    }
    
    public SolrInputDocument buildAndReset() {
        SolrInputDocument doc = build();
        fields = new HashMap<String, List<String>>();
        return doc;
    }
    
    public SolrInputDocumentBuilder withField(String fieldName, String ... fieldValues) {
        if (!fields.containsKey(fieldName)) {
            fields.put(fieldName, new LinkedList<String>());
        }
        fields.get(fieldName).addAll(Arrays.asList(fieldValues));
        return this;
    }
}
