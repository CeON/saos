package pl.edu.icm.saos.search.config.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import junit.framework.Assert;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;

/**
 * @author madryk
 */
public class SolrIndexReloaderTest {
    
    private static final String EXISTING_INDEX_NAME = "indexName";
    private static final String NOT_EXISTING_INDEX_NAME = "notExistingIndexName";

    private SolrIndexReloader indexReloader = new SolrIndexReloader();
    
    private SolrServer solrServer = mock(SolrServer.class);
    
    @Before
    public void setUp() {
        indexReloader.setSolrServer(solrServer);
    }
    
    @Test
    public void reloadIndex_EXISTING() throws SolrServerException, IOException {
        
        when(solrServer.request(any())).thenReturn(buildSuccessResponse());
        when(solrServer.request(isCheckStatus())).thenReturn(buildCheckStatusResponse());
        
        IndexConfiguration indexConfiguration = new IndexConfiguration();
        indexConfiguration.setName(EXISTING_INDEX_NAME);
        indexConfiguration.setInstanceDir("indexDirectory");
        
        indexReloader.reloadIndex(indexConfiguration);
        
        ArgumentCaptor<SolrRequest> solrRequestArg = ArgumentCaptor.forClass(SolrRequest.class);
        verify(solrServer, times(2)).request(solrRequestArg.capture());
        
        SolrRequest actualCheckRequest = solrRequestArg.getAllValues().get(0);
        SolrRequest actualReloadRequest = solrRequestArg.getAllValues().get(1);
        
        assertStatusRequest(actualCheckRequest);
        assertReloadRequest(actualReloadRequest, "indexName");
        
    }
    
    @Test
    public void reloadIndex_NOT_EXISTING() throws SolrServerException, IOException {
        
        when(solrServer.request(any())).thenReturn(buildSuccessResponse());
        when(solrServer.request(isCheckStatus())).thenReturn(buildCheckStatusResponse());
        
        IndexConfiguration indexConfiguration = new IndexConfiguration();
        indexConfiguration.setName(NOT_EXISTING_INDEX_NAME);
        indexConfiguration.setInstanceDir("indexDirectory");
        
        indexReloader.reloadIndex(indexConfiguration);
        

        ArgumentCaptor<SolrRequest> solrRequestArg = ArgumentCaptor.forClass(SolrRequest.class);
        verify(solrServer, times(2)).request(solrRequestArg.capture());
        
        SolrRequest actualCheckRequest = solrRequestArg.getAllValues().get(0);
        SolrRequest actualCreateRequest = solrRequestArg.getAllValues().get(1);
        
        assertStatusRequest(actualCheckRequest);
        assertCreateRequest(actualCreateRequest, NOT_EXISTING_INDEX_NAME, "indexDirectory");
    }


    private NamedList<Object> buildCheckStatusResponse() {
        XMLResponseParser parser = new XMLResponseParser();
        
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<response>");
        sb.append("  <lst name=\"responseHeader\">");
        sb.append("      <int name=\"status\">0</int>");
        sb.append("      <int name=\"QTime\">2</int>");
        sb.append("  </lst>");
        sb.append("<lst name=\"initFailures\"/>");
        sb.append("<lst name=\"status\">");
        sb.append("  <lst name=\"" + EXISTING_INDEX_NAME + "\">");
        sb.append("    <str name=\"name\">" + EXISTING_INDEX_NAME + "</str>");
        sb.append("  </lst>");
        sb.append("</lst>");
        sb.append("</response>");

        Reader r = new StringReader(sb.toString());
        NamedList<Object> response = parser.processResponse(r);
        
        return response;
    }
    
    private NamedList<Object> buildSuccessResponse() {
        XMLResponseParser parser = new XMLResponseParser();
        
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<response>");
        sb.append("<lst name=\"responseHeader\">");
        sb.append("  <int name=\"status\">0</int>");
        sb.append("  <int name=\"QTime\">209</int>");
        sb.append("</lst>");
        sb.append("</response>");
        
        Reader r = new StringReader(sb.toString());
        NamedList<Object> response = parser.processResponse(r);
        
        return response;
    }
    
    private void assertStatusRequest(SolrRequest request) {
        SolrParams solrParams = request.getParams();
        
        Assert.assertEquals("STATUS", solrParams.get("action"));
    }
    
    private void assertReloadRequest(SolrRequest request, String indexName) {
        SolrParams solrParams = request.getParams();
        
        Assert.assertEquals("RELOAD", solrParams.get("action"));
        Assert.assertEquals(indexName, solrParams.get("core"));
    }
    
    private void assertCreateRequest(SolrRequest request, String indexName, String indexDir) {
        SolrParams solrParams = request.getParams();
        
        Assert.assertEquals("CREATE", solrParams.get("action"));
        Assert.assertEquals(indexName, solrParams.get("name"));
        Assert.assertEquals(indexDir, solrParams.get("instanceDir"));
        
    }
    
    private static class IsCheckStatusMatcher extends ArgumentMatcher<SolrRequest> {
     
        @Override
        public boolean matches(Object actual) {
            SolrRequest request = (SolrRequest) actual;
            SolrParams solrParams = request.getParams();
            return StringUtils.equals("STATUS", solrParams.get("action"));
        }
    }
    
    private static SolrRequest isCheckStatus() {
        return argThat(new IsCheckStatusMatcher());
    }
}
