package pl.edu.icm.saos.search.search.service;

import java.text.ParseException;

import org.apache.solr.client.solrj.SolrQuery;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.Sorting;

/**
 * @author madryk
 */
public class JudgmentSearchQueryFactoryTest {

    private final static LocalDate FIRST_DATE = new LocalDate(2014, 4, 1);
    private final static LocalDate SECOND_DATE = new LocalDate(2014, 6, 1);
    
    private JudgmentSearchQueryFactory queryFactory = new JudgmentSearchQueryFactory();
    
    private SolrCriterionTransformer<JudgmentIndexField> criterionTransformer = new SolrCriterionTransformer<JudgmentIndexField>();
    
    @Before
    public void setUp() {
        queryFactory.setCriterionTransformer(criterionTransformer);
    }
    
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

        Assert.assertEquals("+courtName:word1 +judgeName:word2", solrQuery.getQuery());
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
    public void shouldCreateQueryWithJudgeName() {
        JudgmentCriteria criteria = new JudgmentCriteria(null)
            .setJudgeName("Adam Nowak");

        SolrQuery solrQuery = queryFactory.createQuery(criteria, null);

        Assert.assertEquals("+judgeName:Adam\\ Nowak", solrQuery.getQuery());
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
