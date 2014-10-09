package pl.edu.icm.saos.search.search.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.HighlightParams;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.search.model.HighlightingFieldParams;
import pl.edu.icm.saos.search.search.model.HighlightingParams;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.Sorting;
import pl.edu.icm.saos.search.search.model.Sorting.Direction;

/**
 * @author madryk
 */
public class AbstractSearchQueryFactoryTest {

    private TestSearchQueryFactory searchQueryFactory = new TestSearchQueryFactory();
    
    @Before
    public void setUp() {
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
        
        searchQueryFactory.setHighlightParams(highlightParams);
    }
    
    @Test
    public void createQUERY_PAGING() {
        SolrQuery query = searchQueryFactory.createQuery(new JudgmentCriteria(), new Paging(2, 20));
        
        assertEquals(Integer.valueOf(40), query.getStart());
        assertEquals(Integer.valueOf(20), query.getRows());
    }
    
    @Test
    public void createQuery_SORTING() {
        Sorting sorting = new Sorting("judgmentDate", Direction.ASC);
        Paging paging = new Paging(0, 10, sorting);
        
        SolrQuery query = searchQueryFactory.createQuery(new JudgmentCriteria(), paging);
        
        assertEquals("judgmentDate asc", query.getSortField());
    }
    
    @Test
    public void createQuery_HIGHLIGHTING() {
        SolrQuery query = searchQueryFactory.createQuery(new JudgmentCriteria(), new Paging(0, 10));
        
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
    
    
    //------------------------ PRIVATE --------------------------
    
    private class TestSearchQueryFactory extends AbstractSearchQueryFactory<JudgmentCriteria> {

        @Override
        protected String transformCriteria(JudgmentCriteria criteria) {
            return null;
        }
        
    }
}
