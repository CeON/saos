package pl.edu.icm.saos.search.search.service;

import java.text.ParseException;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;

/**
 * @author madryk
 */
public class JudgmentCriteriaTransformerTest {

    private final static LocalDate FIRST_DATE = new LocalDate(2014, 4, 1);
    private final static LocalDate SECOND_DATE = new LocalDate(2014, 6, 1);
    
    private JudgmentCriteriaTransformer queryFactory = new JudgmentCriteriaTransformer();
    
    private SolrCriterionTransformer<JudgmentIndexField> criterionTransformer = new SolrCriterionTransformer<JudgmentIndexField>();
    
    @Before
    public void setUp() {
        queryFactory.setCriterionTransformer(criterionTransformer);
    }
    
    @Test
    public void transformCriteria_ALL_CRITERION() {
        JudgmentCriteria criteria = new JudgmentCriteria("word");
        
        String solrQuery = queryFactory.transformCriteria(criteria);

        Assert.assertEquals("+content:word", solrQuery);
    }
    
    @Test
    public void transformCriteria_SINGLE_CRITERION() {
        JudgmentCriteria criteria = new JudgmentCriteria()
            .setKeyword("word");

        String solrQuery = queryFactory.transformCriteria(criteria);

        Assert.assertEquals("+keyword:word", solrQuery);
    }
    
    @Test
    public void transformCriteria_TWO_CRITERIA() {
        JudgmentCriteria criteria = new JudgmentCriteria()
            .setCourtName("word1")
            .setJudgeName("word2");

        String solrQuery = queryFactory.transformCriteria(criteria);

        Assert.assertEquals("+courtName:word1 +judgeName:word2", solrQuery);
    }
    
    @Test
    public void transformCriteria_DATE_FROM() throws ParseException {
        JudgmentCriteria criteria = new JudgmentCriteria()
            .setDateFrom(FIRST_DATE);

        String solrQuery = queryFactory.transformCriteria(criteria);

        Assert.assertEquals("+judgmentDate:[2014-04-01T00:00:00Z TO *]", solrQuery);
    }
    
    @Test
    public void transformCriteria_DATE_TO() throws ParseException {
        JudgmentCriteria criteria = new JudgmentCriteria(null)
            .setDateTo(FIRST_DATE);
        
        String solrQuery = queryFactory.transformCriteria(criteria);

        Assert.assertEquals("+judgmentDate:[* TO 2014-04-01T23:59:59Z]", solrQuery);
    }
    
    @Test
    public void transformCriteria_DATE_RANGE() throws ParseException {
        JudgmentCriteria criteria = new JudgmentCriteria()
            .setDateFrom(FIRST_DATE)
            .setDateTo(SECOND_DATE);
        
        String solrQuery = queryFactory.transformCriteria(criteria);

        Assert.assertEquals("+judgmentDate:[2014-04-01T00:00:00Z TO 2014-06-01T23:59:59Z]", solrQuery);
    }
    
    @Test
    public void transformCriteria_JUDGE_NAME() {
        JudgmentCriteria criteria = new JudgmentCriteria(null)
            .setJudgeName("Adam Nowak");

        String solrQuery = queryFactory.transformCriteria(criteria);

        Assert.assertEquals("+judgeName:Adam\\ Nowak", solrQuery);
    }
    
    @Test
    public void transformCriteria_NO_CRITERIA() {
        JudgmentCriteria criteria = new JudgmentCriteria(" ");
        
        String solrQuery = queryFactory.transformCriteria(criteria);
        
        Assert.assertEquals("*:*", solrQuery);
    }
    

}
