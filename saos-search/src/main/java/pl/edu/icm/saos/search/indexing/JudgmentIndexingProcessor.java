package pl.edu.icm.saos.search.indexing;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentService;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * Batch processor for indexing all judgments
 * 
 * @author madryk
 */
@Service
public class JudgmentIndexingProcessor implements ItemProcessor<JudgmentIndexingData, SolrInputDocument> {
    
    private JudgmentEnrichmentService judgmentEnrichmentService;
    
    private List<JudgmentIndexFieldsFiller> judgmentIndexFieldsFillers = new ArrayList<>();
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public SolrInputDocument process(JudgmentIndexingData item) throws Exception {
        SolrInputDocument doc = new SolrInputDocument();
        
        fillJudgmentFields(doc, item);
        
        Judgment judgment = item.getJudgment();
        judgment.markAsIndexed();
        judgmentEnrichmentService.unenrichAndSave(judgment);
        
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
    public void setJudgmentEnrichmentService(JudgmentEnrichmentService judgmentEnrichmentService) {
        this.judgmentEnrichmentService = judgmentEnrichmentService;
    }

    @Autowired
    public void setJudgmentIndexFieldsFillers(
            List<JudgmentIndexFieldsFiller> judgmentIndexFieldsFillers) {
        this.judgmentIndexFieldsFillers = judgmentIndexFieldsFillers;
    }

}
