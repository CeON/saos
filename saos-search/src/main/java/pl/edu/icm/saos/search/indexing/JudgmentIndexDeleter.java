package pl.edu.icm.saos.search.indexing;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * @author madryk
 */
@Service
public class JudgmentIndexDeleter {

    private final static Logger log = LoggerFactory.getLogger(JudgmentIndexDeleter.class);


    private JudgmentRepository judgmentRepository;

    private SolrServer solrJudgmentsServer;
    
    
    private int indexIteratingPageSize = 1000;
    
    
    //------------------------ LOGIC --------------------------
    
    public void deleteFromIndexWithoutCorrespondingJudgmentInDb() throws SolrServerException, IOException {
        
        int pageNumber = 0;
        
        while (true) {
            SolrQuery query = createIndexIteratingQuery(pageNumber);
            
            QueryResponse response = solrJudgmentsServer.query(query);
            
            if (response.getResults().size() == 0) {
                break;
            }
            
            List<Long> indexJudgmentIds = extractIdsFromResult(response);
            
            List<Long> existingJudgmentIds = judgmentRepository.filterIdsToExisting(indexJudgmentIds);
            
            List<Long> judgmentIdsToDelete = Lists.newArrayList(indexJudgmentIds);
            judgmentIdsToDelete.removeAll(existingJudgmentIds);
            
            if (judgmentIdsToDelete.size() > 0) {
                log.info("Removing judgments from index which don't have corresponding entry in database: {}", judgmentIdsToDelete);
                
                solrJudgmentsServer.deleteById(judgmentIdsToDelete.stream().map(x -> String.valueOf(x)).collect(Collectors.toList()));
            }
            
            ++pageNumber;
        }
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private SolrQuery createIndexIteratingQuery(int pageNumber) {
        SolrQuery query = new SolrQuery("*:*");
        
        query.setStart(pageNumber * indexIteratingPageSize);
        query.setRows(indexIteratingPageSize);
        query.setSort(JudgmentIndexField.DATABASE_ID.getFieldName(), ORDER.asc);
        query.setFields(JudgmentIndexField.DATABASE_ID.getFieldName());
        
        return query;
    }
    
    private List<Long> extractIdsFromResult(QueryResponse response) {
        return response.getResults().stream()
                .map(x -> (Long) x.getFirstValue(JudgmentIndexField.DATABASE_ID.getFieldName()))
                .collect(Collectors.toList());
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    @Autowired
    @Qualifier("solrJudgmentsServer")
    public void setSolrJudgmentsServer(SolrServer solrJudgmentsServer) {
        this.solrJudgmentsServer = solrJudgmentsServer;
    }

    public void setIndexIteratingPageSize(int indexIteratingPageSize) {
        this.indexIteratingPageSize = indexIteratingPageSize;
    }
}
