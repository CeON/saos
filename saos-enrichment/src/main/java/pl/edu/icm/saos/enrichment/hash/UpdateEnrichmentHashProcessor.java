package pl.edu.icm.saos.enrichment.hash;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.JudgmentEnrichmentHashRepository;
import pl.edu.icm.saos.persistence.enrichment.model.JudgmentEnrichmentHash;

/**
 * @author madryk
 */
@Service
public class UpdateEnrichmentHashProcessor implements ItemProcessor<JudgmentEnrichmentTags, JudgmentEnrichmentHash> {

    private JudgmentEnrichmentTagsHashCalculator judgmentEnrichmentTagsHashCalculator;
    
    private JudgmentEnrichmentHashRepository judgmentEnrichmentHashRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public JudgmentEnrichmentHash process(JudgmentEnrichmentTags judgmentEnrichmentTags) throws Exception {
        
        JudgmentEnrichmentHash enrichmentHash = judgmentEnrichmentHashRepository.findByJudgmentId(judgmentEnrichmentTags.getJudgmentId());
        
        if (enrichmentHash == null) {
            enrichmentHash = new JudgmentEnrichmentHash();
            enrichmentHash.setJudgmentId(judgmentEnrichmentTags.getJudgmentId());
        }
        
        String hash = judgmentEnrichmentTagsHashCalculator.calculateHash(judgmentEnrichmentTags);
        enrichmentHash.updateHash(hash);
        
        return enrichmentHash;
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentEnrichmentTagsHashCalculator(
            JudgmentEnrichmentTagsHashCalculator judgmentEnrichmentTagsHashCalculator) {
        this.judgmentEnrichmentTagsHashCalculator = judgmentEnrichmentTagsHashCalculator;
    }


    @Autowired
    public void setJudgmentEnrichmentHashRepository(
            JudgmentEnrichmentHashRepository judgmentEnrichmentHashRepository) {
        this.judgmentEnrichmentHashRepository = judgmentEnrichmentHashRepository;
    }

}
