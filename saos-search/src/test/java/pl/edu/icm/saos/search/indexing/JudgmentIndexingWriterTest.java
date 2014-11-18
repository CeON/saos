package pl.edu.icm.saos.search.indexing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class JudgmentIndexingWriterTest {

    private JudgmentIndexingWriter judgmentIndexingWriter = new JudgmentIndexingWriter();
    
    @Mock
    private SolrServer judgmentsSolrServer;
    
    @Captor
    private ArgumentCaptor<Collection<SolrInputDocument>> addDocumentsCaptor;
    
    @Before
    public void setUp() {
        judgmentIndexingWriter.setSolrServer(judgmentsSolrServer);
    }
    
    @Test
    public void write() throws Exception {
        SolrInputDocument firstDocument = new SolrInputDocument();
        SolrInputDocument secondDocument = new SolrInputDocument();
        
        judgmentIndexingWriter.write(Lists.newArrayList(firstDocument, secondDocument));
        
        verify(judgmentsSolrServer, times(1)).add(addDocumentsCaptor.capture());
        assertThat(addDocumentsCaptor.getValue(), containsInAnyOrder(firstDocument, secondDocument));
        
    }
}
