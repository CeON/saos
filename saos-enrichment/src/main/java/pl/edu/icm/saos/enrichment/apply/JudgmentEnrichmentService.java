package pl.edu.icm.saos.enrichment.apply;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.common.Generatable;
import pl.edu.icm.saos.persistence.common.UnenrichingVisitor;
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
    
    private EntityManager entityManager;
    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Finds a judgment with the given id by using {@link JudgmentRepository#findOneAndInitialize(long)} and
     * then invokes {@link #enrich(Judgment)}. <br/>
     * Usually you will want to use this method instead of {@link JudgmentRepository#findOneAndInitialize(long)}.<br/>
     * Method will return judgment detached from session to prevent flushing changes made to object
     * during enrichment process. 
     * 
     */
    public <T extends Judgment> T findOneAndEnrich(long judgmentId) {
        
        T judgment = judgmentRepository.findOneAndInitialize(judgmentId);
        
        if (judgment == null) {
            return null;
        }
        
        entityManager.detach(judgment);
        
        enrich(judgment);
        
        return judgment;
    
    }
    
    /**
     * Removes enrichment from judgment and then saves it using {@link JudgmentRepository#save(Judgment)}.<br/>
     * If judgment was fetched by {@link #findOneAndEnrich(long)} then you should use this method for save operation
     * instead of {@link JudgmentRepository#save(Judgment)}.
     */
    public void unenrichAndSave(Judgment judgment) {
        Preconditions.checkNotNull(judgment);
        
        judgment.accept(new UnenrichingVisitor());
        
        judgmentRepository.save(judgment);
    }


    /**
     * Finds {@link EnrichmentTag}s related to the given judgment and then invokes {@link #enrich(Judgment, List)} 
     */
    public <T extends Judgment> void enrich(T judgment) {
        
        Preconditions.checkNotNull(judgment);
        
        List<EnrichmentTag> enrichmentTags = enrichmentTagRepository.findAllByJudgmentId(judgment.getId());
        
        enrich(judgment, enrichmentTags);
       
    }
    
    
    /**
     * Invokes {@link #enrich(Judgment, EnrichmentTag)} for the given judgment and each of the enrichmentTags 
     */
    public <T extends Judgment> void enrich(T judgment, List<EnrichmentTag> enrichmentTags) {
        
        Preconditions.checkNotNull(enrichmentTags);
        
        for (EnrichmentTag enrichmentTag : enrichmentTags) {
            
            enrich(judgment, enrichmentTag);
           
        }
        
    }

    
    /** 
     * Generates data based on the given enrichmentTag and then adds the generated data
     * to the passed {@link Judgment} object tree. 
     * Generated objects are {@link Generatable} and have {@link Generatable#isGenerated()} equal to true.
    */
    public <T extends Judgment> void enrich(T judgment, EnrichmentTag enrichmentTag) {
        
        Preconditions.checkNotNull(judgment);
        Preconditions.checkNotNull(enrichmentTag);
        Preconditions.checkArgument(enrichmentTag.getJudgmentId()==judgment.getId());
        
        EnrichmentTagApplier enrichmentTagApplier = enrichmentTagApplierManager.getEnrichmentTagApplier(enrichmentTag.getTagType());
        
        if (enrichmentTagApplier != null) {
            
            enrichmentTagApplier.applyEnrichmentTag(judgment, enrichmentTag);
            
        }
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
    
    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
}
