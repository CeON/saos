package pl.edu.icm.saos.search.search.service;

import java.text.ParseException;
import java.util.Date;

import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Assert;
import org.junit.Test;

import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.Sorting;
import pl.edu.icm.saos.search.search.service.JudgmentSearchQueryFactory;

/**
 * @author madryk
 */
public class JudgmentSearchQueryFactoryTest {

    private final static Date FIRST_DATE = new Date(1396310400000L); // 2014-04-01
    private final static Date SECOND_DATE = new Date(1401580800000L); // 2014-06-01
    
    private JudgmentSearchQueryFactory queryFactory = new JudgmentSearchQueryFactory();
    
    @Test
    public void shouldCreateQueryWithAllField() {
        JudgmentCriteria criteria = new JudgmentCriteria("word");
        
        SolrQuery solrQuery = queryFactory.createQuery(criteria, null);

        Assert.assertEquals("+content:word", solrQuery.getQuery());
    }
    
    @Test
    public void shouldCreateQueryWithSingleField() {
        JudgmentCriteria criteria = new JudgmentCriteria()
            .setKeyword("word");

        SolrQuery solrQuery = queryFactory.createQuery(criteria, null);

        Assert.assertEquals("+keyword:word", solrQuery.getQuery());
    }
    
    @Test
    public void shouldCreateQuryWithTwoFields() {
        JudgmentCriteria criteria = new JudgmentCriteria()
            .setCourtName("word1")
            .setJudgeName("word2");

        SolrQuery solrQuery = queryFactory.createQuery(criteria, null);

        Assert.assertEquals("+courtName:word1 +judge:word2", solrQuery.getQuery());
    }
    
    @Test
    public void shouldCreateQueryWithFromDate() throws ParseException {
        JudgmentCriteria criteria = new JudgmentCriteria()
            .setDateFrom(FIRST_DATE);

        SolrQuery solrQuery = queryFactory.createQuery(criteria, null);

        Assert.assertEquals("+judgmentDate:[2014-04-01T00:00:00Z TO *]", solrQuery.getQuery());
    }
    
    @Test
    public void shouldCreateQueryWithToDate() throws ParseException {
        JudgmentCriteria criteria = new JudgmentCriteria(null)
            .setDateTo(FIRST_DATE);
        
        SolrQuery solrQuery = queryFactory.createQuery(criteria, null);

        Assert.assertEquals("+judgmentDate:[* TO 2014-04-01T23:59:59Z]", solrQuery.getQuery());
    }
    
    @Test
    public void shouldCreateQueryWithDateRange() throws ParseException {
        JudgmentCriteria criteria = new JudgmentCriteria()
            .setDateFrom(FIRST_DATE)
            .setDateTo(SECOND_DATE);
        
        SolrQuery solrQuery = queryFactory.createQuery(criteria, null);

        Assert.assertEquals("+judgmentDate:[2014-04-01T00:00:00Z TO 2014-06-01T23:59:59Z]", solrQuery.getQuery());
    }
    
    @Test
    public void shouldTakeSearchAllQueryWhenNoCriteria() {
        JudgmentCriteria criteria = new JudgmentCriteria(" ");
        
        SolrQuery solrQuery = queryFactory.createQuery(criteria, null);
        
        Assert.assertEquals("*:*", solrQuery.getQuery());
    }
    
    @Test
    public void shouldApplyPagingToQuery() {
        JudgmentCriteria criteria = new JudgmentCriteria();
        Paging paging = new Paging(3, 15);
        
        SolrQuery solrQuery = queryFactory.createQuery(criteria, paging);
        
        Assert.assertEquals(Integer.valueOf(45), solrQuery.getStart());
        Assert.assertEquals(Integer.valueOf(15), solrQuery.getRows());
    }
    
    @Test
    public void shouldApplySortingToQuery() {
        JudgmentCriteria criteria = new JudgmentCriteria();
        Paging paging = new Paging(0, 10, Sorting.relevanceSorting());
        
        SolrQuery solrQuery = queryFactory.createQuery(criteria, paging);

        Assert.assertEquals("score desc", solrQuery.getSortField());
    }
}
