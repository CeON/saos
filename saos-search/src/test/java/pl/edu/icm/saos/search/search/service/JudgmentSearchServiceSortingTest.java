package pl.edu.icm.saos.search.search.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.search.SearchTestConfiguration;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.search.search.model.Sorting;
import pl.edu.icm.saos.search.search.model.Sorting.Direction;

import com.google.common.collect.Lists;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * @author madryk
 */
@RunWith(DataProviderRunner.class)
@ContextConfiguration(classes={ SearchTestConfiguration.class })
@Category(SlowTest.class)
public class JudgmentSearchServiceSortingTest {

    private TestContextManager testContextManager;
    
    @Autowired
    private JudgmentSearchService judgmentSearchService;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer solrJudgmentsServer;
    
    
    @Before
    public void setUp() throws Exception {
        testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
        
        solrJudgmentsServer.deleteByQuery("*:*");
        solrJudgmentsServer.commit();
    }
    
    @DataProvider
    public static Object[][] searchResultsSortingData() {
        return new Object[][] {
                { Lists.newArrayList(2L, 7L, 8L, 3L, 4L, 1L, 6L, 5L), new Sorting(JudgmentIndexField.REFERENCING_JUDGMENTS_COUNT.getFieldName(), Direction.ASC), referencingCountDocsData() },
                { Lists.newArrayList(5L, 6L, 1L, 4L, 3L, 2L, 7L, 8L), new Sorting(JudgmentIndexField.REFERENCING_JUDGMENTS_COUNT.getFieldName(), Direction.DESC), referencingCountDocsData() },
                
                { Lists.newArrayList(1L, 6L, 3L, 5L, 2L, 7L, 4L), new Sorting(JudgmentIndexField.JUDGMENT_DATE.getFieldName(), Direction.ASC), judgmentDateDocsData() },
                { Lists.newArrayList(4L, 7L, 2L, 5L, 3L, 1L, 6L), new Sorting(JudgmentIndexField.JUDGMENT_DATE.getFieldName(), Direction.DESC), judgmentDateDocsData() },
                
        };
    }
    
    @DataProvider
    public static Object[][] searchResultsRelevanceSortingData() {
        return new Object[][] {
                { Lists.newArrayList(3L, 2L, 1L), JudgmentCriteriaBuilder.create().withAll("test").build(), relevanceDocsData() },
        };
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    @UseDataProvider("searchResultsSortingData")
    public void search_CHECK_SORTING(List<Long> expectedResults, Sorting givenSorting, List<SolrInputDocument> givenDocuments) throws SolrServerException, IOException {
        // given
        indexJudgments(givenDocuments);
        
        // execute
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(new JudgmentCriteria(), new Paging(0, 10, givenSorting));
        
        // assert
        List<Long> resultsIds = results.getResults().stream().map(r -> r.getId()).collect(Collectors.toList());
        assertThat(resultsIds, contains(expectedResults.toArray(new Long[] { })));
    }
    
    
    @Test
    @UseDataProvider("searchResultsRelevanceSortingData")
    public void search_CHECK_RELEVANCE_SORTING(List<Long> expectedResults, JudgmentCriteria givenCriteria , List<SolrInputDocument> givenDocuments) throws SolrServerException, IOException {
        // given
        indexJudgments(givenDocuments);
        
        // execute
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(givenCriteria, new Paging(0, 10, Sorting.relevanceSorting()));
        
        // assert
        List<Long> resultsIds = results.getResults().stream().map(r -> r.getId()).collect(Collectors.toList());
        assertThat(resultsIds, contains(expectedResults.toArray(new Long[] { })));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private static List<SolrInputDocument> referencingCountDocsData() {
        List<SolrInputDocument> docs = Lists.newArrayList();
        
        List<Long> refCounts = Lists.newArrayList(23L, 0L, 12L, 15L, 89L, 68L, 0L, 0L);
        
        int i = 1;
        for (Long refCount : refCounts) {
            SolrInputDocument doc = new SolrInputDocument();
            
            doc.addField("databaseId", i);
            doc.addField("referencingJudgmentsCount", refCount);
            docs.add(doc);
            
            ++i;
        }
        
        return docs;
    }
    
    private static List<SolrInputDocument> judgmentDateDocsData() {
        List<SolrInputDocument> docs = Lists.newArrayList();
        
        List<String> dateStrings = Lists.newArrayList(
                "1990-04-15T00:00:00Z",
                "1991-04-15T00:00:00Z",
                "1990-04-16T00:00:00Z",
                "1992-01-01T00:00:00Z",
                "1991-03-15T00:00:00Z",
                "1990-04-15T00:00:00Z",
                "1991-05-15T00:00:00Z");
        
        int i = 1;
        for (String refCount : dateStrings) {
            SolrInputDocument doc = new SolrInputDocument();
            
            doc.addField("databaseId", i);
            doc.addField("judgmentDate", refCount);
            docs.add(doc);
            
            ++i;
        }
        
        return docs;
    }
    
    private static List<SolrInputDocument> relevanceDocsData() {
        
        SolrInputDocument firstDoc = new SolrInputDocument();
        firstDoc.addField("databaseId", 1L);
        firstDoc.addField("content", "test other other test");
        
        SolrInputDocument secondDoc = new SolrInputDocument();
        secondDoc.addField("databaseId", 2L);
        secondDoc.addField("content", "test test other test");
        
        SolrInputDocument thirdDoc = new SolrInputDocument();
        thirdDoc.addField("databaseId", 3L);
        thirdDoc.addField("content", "test test");
        
        return Lists.newArrayList(firstDoc, secondDoc, thirdDoc);
    }
    
    private void indexJudgments(List<SolrInputDocument> documents) throws SolrServerException, IOException {
        solrJudgmentsServer.add(documents);
        solrJudgmentsServer.commit();
    }
}
