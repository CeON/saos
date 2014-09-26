package pl.edu.icm.saos.search.indexing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class JudgmentIndexingWriterTest {

    private JudgmentIndexingWriter judgmentIndexingWriter = new JudgmentIndexingWriter();
    
    private SolrServer judgmentsSolrServer = mock(SolrServer.class);
    
    @Before
    public void setUp() {
        judgmentIndexingWriter.setSolrServer(judgmentsSolrServer);
    }
    
    @Test
    public void write() throws Exception {
        SolrInputDocument firstDocument = new SolrInputDocument();
        SolrInputDocument secondDocument = new SolrInputDocument();
        
        judgmentIndexingWriter.write(Lists.newArrayList(firstDocument, secondDocument));
        
        verify(judgmentsSolrServer).add(firstDocument);
        verify(judgmentsSolrServer).add(secondDocument);
    }
}
