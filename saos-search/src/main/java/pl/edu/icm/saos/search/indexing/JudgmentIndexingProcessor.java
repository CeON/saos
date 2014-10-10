package pl.edu.icm.saos.search.indexing;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * Batch processor for indexing all judgments
 * 
 * @author madryk
 */
@Service
public class JudgmentIndexingProcessor implements ItemProcessor<Judgment, SolrInputDocument> {
    
    private JudgmentRepository ccJudgmentRepository;
    
    private CcJudgmentIndexFieldsFiller ccJudgmentIndexFieldsFiller;
    
    private Map<Class<? extends Judgment>, JudgmentIndexFieldsFiller<? extends Judgment>> judgmentIndexFieldsFillers = new HashMap<>();
    
    
    @PostConstruct
    public void init() {
        judgmentIndexFieldsFillers = new HashMap<>();
        judgmentIndexFieldsFillers.put(CommonCourtJudgment.class, ccJudgmentIndexFieldsFiller);
    }
    
    @Override
    public SolrInputDocument process(Judgment item) throws Exception {
        SolrInputDocument doc = new SolrInputDocument();
        
        fillJudgmentFields(doc, item);
        
        item.markAsIndexed();
        ccJudgmentRepository.save(item);
        
        return doc;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    @SuppressWarnings("unchecked")
    private void fillJudgmentFields(SolrInputDocument doc, Judgment item) {
        if (judgmentIndexFieldsFillers.containsKey(item.getClass())) {
            JudgmentIndexFieldsFiller<Judgment> judgmentSpecificProcessor = 
                    (JudgmentIndexFieldsFiller<Judgment>)judgmentIndexFieldsFillers.get(item.getClass());
            
            judgmentSpecificProcessor.fillFields(doc, item);
        } else {
            throw new RuntimeException("Unable to process judgment type: " + item.getClass());
        }
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setCcJudgmentRepository(JudgmentRepository ccJudgmentRepository) {
        this.ccJudgmentRepository = ccJudgmentRepository;
    }

    @Autowired
    public void setCcJudgmentIndexFieldsFiller(
            CcJudgmentIndexFieldsFiller ccJudgmentIndexFieldsFiller) {
        this.ccJudgmentIndexFieldsFiller = ccJudgmentIndexFieldsFiller;
    }

}
