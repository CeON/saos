package pl.edu.icm.saos.search.search.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.HighlightParams;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.search.search.model.HighlightingFieldParams;
import pl.edu.icm.saos.search.search.model.HighlightingParams;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.Sorting;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchQueryFactoryImplTest {

    private SearchQueryFactoryImpl<JudgmentCriteria> queryFactory = new SearchQueryFactoryImpl<JudgmentCriteria>();
    
    @Mock
    private CriteriaTransformer<JudgmentCriteria> criteriaTransformer;
    
    @Before
    public void setUp() {
        queryFactory.setCriteriaTransformer(criteriaTransformer);
        queryFactory.setHighlightParams(generateHighlighting());
    }
    
    @Test
    public void createQuery_TRANSFORM_CRITERIA() {
        // given
        when(criteriaTransformer.transformCriteria(any())).thenReturn("+keyword:word");
        
        // execute
        SolrQuery solrQuery = queryFactory.createQuery(new JudgmentCriteria(), null);
        
        // assert
        assertEquals("+keyword:word", solrQuery.getQuery());
    }
    
    @Test
    public void createQuery_PAGING() {
        // given
        JudgmentCriteria criteria = new JudgmentCriteria();
        Paging paging = new Paging(3, 15);
        
        when(criteriaTransformer.transformCriteria(any())).thenReturn("*:*");

        // execute
        SolrQuery solrQuery = queryFactory.createQuery(criteria, paging);

        // assert
        assertEquals(Integer.valueOf(45), solrQuery.getStart());
        assertEquals(Integer.valueOf(15), solrQuery.getRows());
    }

    @Test
    public void createQuery_SORTING() {
        // given
        JudgmentCriteria criteria = new JudgmentCriteria();
        Paging paging = new Paging(0, 10, Sorting.relevanceSorting());
        
        when(criteriaTransformer.transformCriteria(any())).thenReturn("*:*");

        // execute
        SolrQuery solrQuery = queryFactory.createQuery(criteria, paging);

        // assert
        assertEquals("score desc", solrQuery.getSortField());
    }

    @Test
    public void createQuery_HIGHLIGHTING() {
        // given
        when(criteriaTransformer.transformCriteria(any())).thenReturn("*:*");
        
        // execute
        SolrQuery solrQuery = queryFactory.createQuery(new JudgmentCriteria(), null);

        // assert
        assertTrue(solrQuery.getHighlight());
        assertEquals("<highlight>", solrQuery.getHighlightSimplePre());
        assertEquals("</highlight>", solrQuery.getHighlightSimplePost());
        
        assertEquals(2, solrQuery.getHighlightFields().length);
        assertEquals("firstContent", solrQuery.getHighlightFields()[0]);
        assertEquals("secondContent", solrQuery.getHighlightFields()[1]);
        
        assertEquals("50", solrQuery.getFieldParam("firstContent", HighlightParams.FRAGSIZE));
        assertEquals("2", solrQuery.getFieldParam("firstContent", HighlightParams.SNIPPETS));
        
        assertEquals("200", solrQuery.getFieldParam("secondContent", HighlightParams.FRAGSIZE));
        assertEquals("5", solrQuery.getFieldParam("secondContent", HighlightParams.SNIPPETS));
    }
    
    @Test
    public void createQuery_HIGHLIGHTING_FROM_FIELD() {
        // given
        when(criteriaTransformer.transformCriteria(any())).thenReturn("+firstHighlightFrom:word +secondHighlightFrom:word +keyword:word");
        
        // execute
        SolrQuery solrQuery = queryFactory.createQuery(new JudgmentCriteria(), null);
        
        // assert
        assertEquals("+firstContent:word +firstContent:word +keyword:word", solrQuery.get(HighlightParams.Q));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private HighlightingParams generateHighlighting() {
        HighlightingParams highlightParams = new HighlightingParams();
        highlightParams.addParam(HighlightParams.SIMPLE_PRE, "<highlight>");
        highlightParams.addParam(HighlightParams.SIMPLE_POST, "</highlight>");
        
        HighlightingFieldParams firstFieldHighlightParams = new HighlightingFieldParams("firstContent");
        firstFieldHighlightParams.addHighlightFromField("firstHighlightFrom");
        firstFieldHighlightParams.addHighlightFromField("secondHighlightFrom");
        firstFieldHighlightParams.addParam(HighlightParams.FRAGSIZE, "50");
        firstFieldHighlightParams.addParam(HighlightParams.SNIPPETS, "2");
        highlightParams.addFieldParams(firstFieldHighlightParams);
        
        HighlightingFieldParams secondFieldHighlightParams = new HighlightingFieldParams("secondContent");
        secondFieldHighlightParams.addParam(HighlightParams.FRAGSIZE, "200");
        secondFieldHighlightParams.addParam(HighlightParams.SNIPPETS, "5");
        highlightParams.addFieldParams(secondFieldHighlightParams);
        
        return highlightParams;
    }
}
