package pl.edu.icm.saos.search.indexing;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JudgmentIndexingWriter implements ItemWriter<SolrInputDocument> {

    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer solrServer;
    
    @Override
    public void write(List<? extends SolrInputDocument> items) throws Exception {
        for (SolrInputDocument item : items) {
            solrServer.add(item);
        }
        solrServer.commit();
    }

}
