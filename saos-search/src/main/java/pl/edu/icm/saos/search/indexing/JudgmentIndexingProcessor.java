package pl.edu.icm.saos.search.indexing;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * Batch processor for indexing all judgments
 * 
 * @author madryk
 */
@Service
public class JudgmentIndexingProcessor implements ItemProcessor<Judgment, SolrInputDocument> {
    
    private JudgmentRepository judgmentRepository;
    
    private List<JudgmentIndexFieldsFiller> judgmentIndexFieldsFillers = new ArrayList<>();
    
    
    @Override
    public SolrInputDocument process(Judgment item) throws Exception {
        SolrInputDocument doc = new SolrInputDocument();
        
        fillJudgmentFields(doc, item);
        
        item.markAsIndexed();
        judgmentRepository.save(item);
        
        return doc;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void fillJudgmentFields(SolrInputDocument doc, Judgment item) {
        for (JudgmentIndexFieldsFiller indexFieldsFiller : judgmentIndexFieldsFillers) {
            if (indexFieldsFiller.isApplicable(item.getClass())) {
                indexFieldsFiller.fillFields(doc, item);
                return;
            }
        }
        throw new RuntimeException("Unable to process judgment type: " + item.getClass());
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    @Autowired
    public void setJudgmentIndexFieldsFillers(
            List<JudgmentIndexFieldsFiller> judgmentIndexFieldsFillers) {
        this.judgmentIndexFieldsFillers = judgmentIndexFieldsFillers;
    }

}
