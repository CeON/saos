package pl.edu.icm.saos.search.search.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.HighlightParams;

import pl.edu.icm.saos.search.search.model.HighlightingFieldParams;
import pl.edu.icm.saos.search.search.model.HighlightingParams;

public class TestCommonSearchQueryFactory {

    public HighlightingParams generateHighlighting() {
        HighlightingParams highlightParams = new HighlightingParams();
        highlightParams.addParam(HighlightParams.SIMPLE_PRE, "<highlight>");
        highlightParams.addParam(HighlightParams.SIMPLE_POST, "</highlight>");
        
        HighlightingFieldParams firstFieldHighlightParams = new HighlightingFieldParams("firstContent");
        firstFieldHighlightParams.addParam(HighlightParams.FRAGSIZE, "50");
        firstFieldHighlightParams.addParam(HighlightParams.SNIPPETS, "2");
        highlightParams.addFieldParams(firstFieldHighlightParams);
        
        HighlightingFieldParams secondFieldHighlightParams = new HighlightingFieldParams("secondContent");
        secondFieldHighlightParams.addParam(HighlightParams.FRAGSIZE, "200");
        secondFieldHighlightParams.addParam(HighlightParams.SNIPPETS, "5");
        highlightParams.addFieldParams(secondFieldHighlightParams);
        
        return highlightParams;
    }
    
    public void assertHighlighting(SolrQuery query) {
        assertTrue(query.getHighlight());
        assertEquals("<highlight>", query.getHighlightSimplePre());
        assertEquals("</highlight>", query.getHighlightSimplePost());
        
        assertEquals(2, query.getHighlightFields().length);
        assertEquals("firstContent", query.getHighlightFields()[0]);
        assertEquals("secondContent", query.getHighlightFields()[1]);
        
        assertEquals("50", query.getFieldParam("firstContent", HighlightParams.FRAGSIZE));
        assertEquals("2", query.getFieldParam("firstContent", HighlightParams.SNIPPETS));
        
        assertEquals("200", query.getFieldParam("secondContent", HighlightParams.FRAGSIZE));
        assertEquals("5", query.getFieldParam("secondContent", HighlightParams.SNIPPETS));
    }
}
