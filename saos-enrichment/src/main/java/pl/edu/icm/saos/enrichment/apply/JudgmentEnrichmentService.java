package pl.edu.icm.saos.enrichment.apply;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.common.Generatable;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.base.Preconditions;

/**
 * Service enriching {@link Judgment} with data generated from {@link EnrichmentTag}s that are assigned to it. 
 * 
 * @author Łukasz Dumiszewski
 */
@Service("judgmentEnrichmentService")
public class JudgmentEnrichmentService {

    
    private JudgmentRepository judgmentRepository;

    private EnrichmentTagRepository enrichmentTagRepository;
    
    private EnrichmentTagApplierManager enrichmentTagApplierManager;
    
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Finds a judgment with the given id by using {@link JudgmentRepository#findOneAndInitialize(long) and
     * then invokes #enrich(Judgment). <br/>
     * Usually you will want to use this method instead of {@link JudgmentRepository#findOneAndInitialize(long)}.
     * 
     */
    public <T extends Judgment> T findOneAndEnrich(long judgmentId) {
        
        T judgment = judgmentRepository.findOneAndInitialize(judgmentId);
        
        if (judgment == null) {
            return null;
        }
        
        return enrich(judgment);
    
    }


    /**
     * Finds {@link EnrichmentTag}s related to the given judgment, generates data from them and then adds the generated data
     * to the {@link Judgment} object tree. Generated objects are {@link Generatable} and have {@link Generatable#isGenerated()} equal to true.
     */
    public <T extends Judgment> T enrich(T judgment) {
        
        Preconditions.checkNotNull(judgment);
        
        List<EnrichmentTag> enrichmentTags = enrichmentTagRepository.findAllByJudgmentId(judgment.getId());
        
        for (EnrichmentTag enrichmentTag : enrichmentTags) {
           
            EnrichmentTagApplier enrichmentTagApplier = enrichmentTagApplierManager.getEnrichmentTagApplier(enrichmentTag.getTagType());
            
            if (enrichmentTagApplier != null) {
                
                enrichmentTagApplier.applyEnrichmentTag(judgment, enrichmentTag);
                
            }
           
        }
        
        return judgment;
    }


    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    @Autowired
    public void setEnrichmentTagRepository(EnrichmentTagRepository enrichmentTagRepository) {
        this.enrichmentTagRepository = enrichmentTagRepository;
    }

    @Autowired
    public void setEnrichmentTagApplierManager(EnrichmentTagApplierManager enrichmentTagApplierManager) {
        this.enrichmentTagApplierManager = enrichmentTagApplierManager;
    }
    
    
}
