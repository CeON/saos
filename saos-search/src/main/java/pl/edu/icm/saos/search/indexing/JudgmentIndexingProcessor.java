package pl.edu.icm.saos.search.indexing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

@Service
public class JudgmentIndexingProcessor extends JudgmentIndexingProcessorBase implements ItemProcessor<Judgment, SolrInputDocument> {
    
    @Autowired
    private JudgmentRepository ccJudgmentRepository;
    
    @Autowired
    private CcJudgmentIndexingProcessor ccJudgmentIndexingProcessor;
    
    private Map<Class<? extends Judgment>, SpecificJudgmentIndexingProcessor<? extends Judgment>> judgmentSpecificProcessors = new HashMap<>();
    
    @PostConstruct
    public void init() {
        judgmentSpecificProcessors = new HashMap<>();
        judgmentSpecificProcessors.put(CommonCourtJudgment.class, ccJudgmentIndexingProcessor);
    }
    
    @Override
    public SolrInputDocument process(Judgment item) throws Exception {
        SolrInputDocument doc = new SolrInputDocument();
        
        processIds(doc, item);
        processJudges(doc, item);
        processJudgmentSpecificFields(doc, item);
        
        for (String legalBase : item.getLegalBases()) {
            addField(doc, JudgmentIndexField.LEGAL_BASE, legalBase);
        }
        for (JudgmentReferencedRegulation referencedRegulation : item.getReferencedRegulations()) {
            addField(doc, JudgmentIndexField.REFERENCED_REGULATION, referencedRegulation.getRawText());
        }
        
        
        addDateField(doc, JudgmentIndexField.JUDGMENT_DATE, item.getJudgmentDate());
        if (item.getJudgmentType() != null) {
            addField(doc, JudgmentIndexField.JUDGMENT_TYPE, item.getJudgmentType().name());
        }
        addField(doc, JudgmentIndexField.CONTENT, item.getTextContent());
        
        item.markAsIndexed();
        ccJudgmentRepository.save(item);
        
        return doc;
    }
    
    protected void processIds(SolrInputDocument doc, Judgment item) {
        addField(doc, JudgmentIndexField.ID, UUID.randomUUID().toString());
        addField(doc, JudgmentIndexField.DATABASE_ID, String.valueOf(item.getId()));
        addField(doc, JudgmentIndexField.SIGNATURE, item.getCaseNumber());
    }
    
    protected void processJudges(SolrInputDocument doc, Judgment item) {
        for (Judge judge : item.getJudges()) {
            List<JudgeRole> roles = judge.getSpecialRoles();
            
            if (roles.isEmpty()) {
                addField(doc, JudgmentIndexField.JUDGE, judge.getName());
            } else {
                for (JudgeRole role : roles) {
                    addField(doc, JudgmentIndexField.JUDGE_WITH_ROLE, role.name(), judge.getName());
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    protected void processJudgmentSpecificFields(SolrInputDocument doc, Judgment item) {
        if (judgmentSpecificProcessors.containsKey(item.getClass())) {
            SpecificJudgmentIndexingProcessor<Judgment> judgmentSpecificProcessor = 
                    (SpecificJudgmentIndexingProcessor<Judgment>)judgmentSpecificProcessors.get(item.getClass());
            
            judgmentSpecificProcessor.process(doc, item);
        }
    }

}
