package pl.edu.icm.saos.enrichment.hash;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.JudgmentEnrichmentHashRepository;
import pl.edu.icm.saos.persistence.enrichment.model.JudgmentEnrichmentHash;

/**
 * @author madryk
 */
@Service
public class UpdateEnrichmentHashWriter implements ItemWriter<JudgmentEnrichmentHash> {

    private JudgmentEnrichmentHashRepository judgmentEnrichmentHashRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void write(List<? extends JudgmentEnrichmentHash> enrichmentHashes) throws Exception {
        
        judgmentEnrichmentHashRepository.save(enrichmentHashes);
        
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentEnrichmentHashRepository(JudgmentEnrichmentHashRepository judgmentEnrichmentHashRepository) {
        this.judgmentEnrichmentHashRepository = judgmentEnrichmentHashRepository;
    }


}
