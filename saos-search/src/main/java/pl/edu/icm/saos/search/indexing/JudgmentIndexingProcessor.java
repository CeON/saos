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
public class JudgmentIndexingProcessor implements ItemProcessor<JudgmentIndexingData, SolrInputDocument> {
    
    private JudgmentRepository judgmentRepository;
    
    private List<JudgmentIndexFieldsFiller> judgmentIndexFieldsFillers = new ArrayList<>();
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public SolrInputDocument process(JudgmentIndexingData item) throws Exception {
        SolrInputDocument doc = new SolrInputDocument();
        
        fillJudgmentFields(doc, item);
        
        Judgment judgment = item.getJudgment();
        judgmentRepository.markAsIndexed(judgment.getId());
        
        return doc;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void fillJudgmentFields(SolrInputDocument doc, JudgmentIndexingData item) {
        Judgment judgment = item.getJudgment();
        
        for (JudgmentIndexFieldsFiller indexFieldsFiller : judgmentIndexFieldsFillers) {
            if (indexFieldsFiller.isApplicable(judgment.getCourtType())) {
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
