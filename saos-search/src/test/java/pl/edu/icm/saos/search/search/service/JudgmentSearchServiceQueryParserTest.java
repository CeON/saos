package pl.edu.icm.saos.search.search.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

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
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;

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
public class JudgmentSearchServiceQueryParserTest {
    
    private TestContextManager testContextManager;
    
    @Autowired
    private JudgmentSearchService judgmentSearchService;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer solrJudgmentsServer;
    
    @DataProvider
    public static Object[][] queryResultsData() {
        
        return new Object[][] {
            { Lists.newArrayList(1l, 12l, 13l, 123l), "word1" },
            { Lists.newArrayList(12l, 123l), "word1 word2" },
            { Lists.newArrayList(123l), "word1 word2 word3" },
            
            
            // with OR
            { Lists.newArrayList(1l, 2l, 12l, 13l, 23l, 123l), "word1 OR word2" },
            { Lists.newArrayList(1l, 2l, 3l, 12l, 13l, 23l, 123l), "word1 OR word2 OR word3" },
            
            { Lists.newArrayList(13l, 23l, 123l), "word1 OR word2 word3" },
            { Lists.newArrayList(12l, 23l, 123l), "word2 word3 OR word1" },
            
            
            // with exclusion
            { Lists.newArrayList(2l, 3l, 23l), "-word1" },
            { Lists.newArrayList(1l, 13l), "word1 -word2" },
            { Lists.newArrayList(1l), "word1 -word2 -word3" },
            { Lists.newArrayList(12l), "word1 word2 -word3" },
            
            
            // with quote
            { Lists.newArrayList(1l, 12l, 13l, 123l), "\"word1\"" },
            { Lists.newArrayList(12l, 123l), "\"word1 word2\"" },
            { Lists.newArrayList(123l), "\"word1 word2 word3\"" },
            { Lists.newArrayList(), "\"word2 word1\"" },
            { Lists.newArrayList(123l), "\"word1 word2\" \"word2 word3\"" },
            { Lists.newArrayList(123l), "\"word1 word2\" word3" },
            
            
            // with OR and exclusion
            { Lists.newArrayList(1l, 3l, 12l, 13l, 123l), "word1 OR -word2" },
            { Lists.newArrayList(1l, 2l, 3l, 13l, 23l), "-word1 OR -word2" },
            
            { Lists.newArrayList(1l, 12l), "word1 OR -word2 -word3" },
            { Lists.newArrayList(3l, 13l, 123l), "word1 OR -word2 word3" },
            
            { Lists.newArrayList(1l, 2l, 12l, 13l, 23l, 123l), "word1 OR word2 OR -word3" },
            { Lists.newArrayList(1l, 2l, 3l, 12l, 13l, 123l), "word1 OR -word2 OR -word3" },
            
            
            // with OR and quote
            { Lists.newArrayList(1l, 12l, 13l, 123l), "\"word1 word2\" OR word1" },
            { Lists.newArrayList(3l, 12l, 13l, 23l, 123l), "\"word1 word2\" OR word3" },
            { Lists.newArrayList(12l, 23l, 123l), "\"word1 word2\" OR \"word2 word3\"" },
            
            
            // with exclusion and quote
            { Lists.newArrayList(1l, 2l, 3l, 13l, 23l), "-\"word1 word2\"" },
            { Lists.newArrayList(12l), "\"word1 word2\" -word3" },
            { Lists.newArrayList(12l), "\"word1 word2\" -\"word2 word3\"" },
            
            
            // with OR, exclusion and quote
            { Lists.newArrayList(1l, 3l, 12l, 13l, 123l), "\"word1 word2\" OR -word2" },
            { Lists.newArrayList(1l, 2l, 3l, 13l, 23l, 123l), "-\"word1 word2\" OR word3" },
            { Lists.newArrayList(1l, 2l, 3l, 12l, 13l, 123l), "\"word1 word2\" OR -\"word2 word3\"" },
            { Lists.newArrayList(1l, 2l, 3l, 12l, 13l, 23l), "-\"word1 word2\" OR -\"word2 word3\"" },
            
            
        };
    }
    
    @Before
    public void setUp() throws Exception {
        testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
        
        solrJudgmentsServer.deleteByQuery("*:*");
        solrJudgmentsServer.commit();
        
        indexJudgments();
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    @UseDataProvider("queryResultsData")
    public void search_CHECK_RESULTS(List<Long> expectedResultsIds, String criteriaString) {
        // given
        JudgmentCriteria criteria = new JudgmentCriteria(criteriaString);
        
        // execute
        SearchResults<JudgmentSearchResult> results = judgmentSearchService.search(criteria, null);
        
        // assert
        List<Long> resultsIds = results.getResults().stream().map(r -> r.getId()).collect(Collectors.toList());
        assertThat(resultsIds, containsInAnyOrder(expectedResultsIds.toArray()));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void indexJudgments() throws SolrServerException, IOException {
        
        solrJudgmentsServer.add(fetchJudgmentDoc(1, "word1"));
        solrJudgmentsServer.add(fetchJudgmentDoc(2, "word2"));
        solrJudgmentsServer.add(fetchJudgmentDoc(3, "word3"));
        
        solrJudgmentsServer.add(fetchJudgmentDoc(12, "word1 word2"));
        solrJudgmentsServer.add(fetchJudgmentDoc(13, "word1 word3"));
        solrJudgmentsServer.add(fetchJudgmentDoc(23, "word2 word3"));
        
        solrJudgmentsServer.add(fetchJudgmentDoc(123, "word1 word2 word3"));
        
        solrJudgmentsServer.commit();
    }
    
    private SolrInputDocument fetchJudgmentDoc(long id, String content) {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("databaseId", id);
        
        doc.addField("content", content);
        
        return doc;
    }
    
}
