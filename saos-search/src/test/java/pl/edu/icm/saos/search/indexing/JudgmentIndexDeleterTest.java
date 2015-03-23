package pl.edu.icm.saos.search.indexing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class JudgmentIndexDeleterTest {

    private JudgmentIndexDeleter judgmentIndexDeleter = new JudgmentIndexDeleter();
    
    @Mock
    private JudgmentRepository judgmentRepository;

    @Mock
    private SolrServer solrJudgmentsServer;
    
    
    @Captor
    private ArgumentCaptor<SolrParams> solrParamsCaptor;
    
    @Captor
    private ArgumentCaptor<List<Long>> filterExistingIdsCaptor;
    
    @Captor
    private ArgumentCaptor<List<String>> deleteJudgmentIdsCaptor; 
    
    
    @Before
    public void setUp() {
        judgmentIndexDeleter.setJudgmentRepository(judgmentRepository);
        judgmentIndexDeleter.setSolrJudgmentsServer(solrJudgmentsServer);
        judgmentIndexDeleter.setIndexIteratingPageSize(2);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void deleteFromIndexWithoutCorrespondingJudgmentInDb_EMPTY_INDEX() throws SolrServerException, IOException {
        
        // given
        when(solrJudgmentsServer.query(any())).thenReturn(createSolrResponse(0, Lists.newArrayList()));
        
        // execute
        judgmentIndexDeleter.deleteFromIndexWithoutCorrespondingJudgmentInDb();
        
        // assert
        verify(solrJudgmentsServer).query(solrParamsCaptor.capture());
        verifyZeroInteractions(judgmentRepository);
        
        assertSolrParams(solrParamsCaptor.getValue(), 0, 2);
    }
    
    @Test
    public void deleteFromIndexWithoutCorrespondingJudgmentInDb_NO_DIFFERENCE() throws SolrServerException, IOException {
        
        // given
        List<Long> indexIds = Lists.newArrayList(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        
        when(solrJudgmentsServer.query(any())).thenAnswer(buildSolrResponseAnswer(indexIds));
        when(judgmentRepository.filterIdsToExisting(any())).then(returnsFirstArg());
        
        
        // execute
        judgmentIndexDeleter.deleteFromIndexWithoutCorrespondingJudgmentInDb();
        
        
        // assert
        verify(solrJudgmentsServer, times(5)).query(solrParamsCaptor.capture());
        verify(judgmentRepository, times(4)).filterIdsToExisting(filterExistingIdsCaptor.capture());
        
        assertSolrParams(solrParamsCaptor.getAllValues().get(0), 0, 2);
        assertSolrParams(solrParamsCaptor.getAllValues().get(1), 2, 2);
        assertSolrParams(solrParamsCaptor.getAllValues().get(2), 4, 2);
        assertSolrParams(solrParamsCaptor.getAllValues().get(3), 6, 2);
        assertSolrParams(solrParamsCaptor.getAllValues().get(4), 8, 2);
        
        assertThat(filterExistingIdsCaptor.getAllValues().get(0), contains(1L, 2L));
        assertThat(filterExistingIdsCaptor.getAllValues().get(1), contains(3L, 4L));
        assertThat(filterExistingIdsCaptor.getAllValues().get(2), contains(5L, 6L));
        assertThat(filterExistingIdsCaptor.getAllValues().get(3), contains(7L));
        
        verifyNoMoreInteractions(solrJudgmentsServer, judgmentRepository);
    }
    
    @Test
    public void deleteFromIndexWithoutCorrespondingJudgmentInDb_WITH_DIFFERENCE() throws SolrServerException, IOException {
        
        // given
        List<Long> indexIds = Lists.newArrayList(1L, 2L, 3L, 4L);
        List<Long> databaseIds = Lists.newArrayList(1L, 7L);
        
        when(solrJudgmentsServer.query(any())).then(buildSolrResponseAnswer(indexIds));
        
        when(judgmentRepository.filterIdsToExisting(any())).then(new Answer<List<Long>>() {

            @SuppressWarnings("unchecked")
            @Override
            public List<Long> answer(InvocationOnMock invocation) throws Throwable {
                List<Long> arg = (List<Long>)invocation.getArguments()[0];
                return arg.stream().filter(id -> databaseIds.contains(id)).collect(Collectors.toList());
            }
            
        });
        
        
        // execute
        judgmentIndexDeleter.deleteFromIndexWithoutCorrespondingJudgmentInDb();
        
        
        // assert        
        verify(solrJudgmentsServer, times(2)).deleteById(deleteJudgmentIdsCaptor.capture());
        verify(solrJudgmentsServer, times(3)).query(solrParamsCaptor.capture());
        verify(judgmentRepository, times(2)).filterIdsToExisting(filterExistingIdsCaptor.capture());
        
        assertThat(deleteJudgmentIdsCaptor.getAllValues().get(0), contains("2"));
        assertThat(deleteJudgmentIdsCaptor.getAllValues().get(1), contains("3", "4"));
        
        assertSolrParams(solrParamsCaptor.getAllValues().get(0), 0, 2);
        assertSolrParams(solrParamsCaptor.getAllValues().get(1), 2, 2);
        assertSolrParams(solrParamsCaptor.getAllValues().get(2), 4, 2);
        
        assertThat(filterExistingIdsCaptor.getAllValues().get(0), contains(1L, 2L));
        assertThat(filterExistingIdsCaptor.getAllValues().get(1), contains(3L, 4L));
        
        verifyNoMoreInteractions(solrJudgmentsServer, judgmentRepository);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertSolrParams(SolrParams solrParams, int start, int rows) {
        
        assertThat(solrParams.getParams("fl"), arrayContaining("databaseId"));
        assertThat(solrParams.getParams("q"), arrayContaining("*:*"));
        assertThat(solrParams.getParams("start"), arrayContaining(String.valueOf(start)));
        assertThat(solrParams.getParams("rows"), arrayContaining(String.valueOf(rows)));
        assertThat(solrParams.getParams("sort"), arrayContaining("databaseId asc"));
    }
    
    private QueryResponse createSolrResponse(long totalResults, List<Long> judgmentIds) {
        QueryResponse response = new QueryResponse();
        
        SolrDocumentList documentList = new SolrDocumentList();
        documentList.setNumFound(totalResults);
        
        for (Long id : judgmentIds) {
            SolrDocument doc = new SolrDocument();
            
            doc.addField(JudgmentIndexField.DATABASE_ID.getFieldName(), id);
            
            documentList.add(doc);
        }
        
        NamedList<Object> namedList = new NamedList<Object>();
        namedList.add("response", documentList);
        response.setResponse(namedList);
        
        return response;
    }
    
    private Answer<QueryResponse> buildSolrResponseAnswer(List<Long> indexJudgmentsIds) {
        return new Answer<QueryResponse>() {

            @Override
            public QueryResponse answer(InvocationOnMock invocation) throws Throwable {
                SolrParams params = (SolrParams)invocation.getArguments()[0];
                int start = params.getInt("start");
                int rows = params.getInt("rows");
                
                if (start >= indexJudgmentsIds.size()) {
                    return createSolrResponse(indexJudgmentsIds.size(), Lists.newArrayList());
                }
                
                int end = Math.min(indexJudgmentsIds.size(), start + rows);
                
                return createSolrResponse(indexJudgmentsIds.size(), indexJudgmentsIds.subList(start, end));
            }
        };
    }
    
    
}
