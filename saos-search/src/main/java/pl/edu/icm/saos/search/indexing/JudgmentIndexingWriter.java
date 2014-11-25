package pl.edu.icm.saos.search.indexing;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

/**
 * Spring batch writer of judgments into Solr
 * @author madryk
 */
@Service
public class JudgmentIndexingWriter implements ItemWriter<SolrInputDocument> {

    private SolrServer solrServer;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void write(List<? extends SolrInputDocument> items) throws Exception {
        List<SolrInputDocument> documents = Lists.newArrayList();
        documents.addAll(items);
        
        solrServer.add(documents);
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    public void setSolrServer(SolrServer solrServer) {
        this.solrServer = solrServer;
    }

}
