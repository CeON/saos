package pl.edu.icm.saos.importer.commoncourt.judgment.remove;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.enrichment.delete.JudgmentWithEnrichmentDeleter;
import pl.edu.icm.saos.persistence.model.RemovedJudgment;
import pl.edu.icm.saos.persistence.repository.RemovedJudgmentRepository;

/**
 * Spring batch writer for removing judgments
 * 
 * @author madryk
 */
@Service
public class CcjDeleteRemovedWriter implements ItemWriter<RemovedJudgment> {

    @Autowired
    private JudgmentWithEnrichmentDeleter judgmentWithEnrichmentDeleter;
    
    @Autowired
    private RemovedJudgmentRepository removedJudgmentRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void write(List<? extends RemovedJudgment> judgmentsToRemove) throws Exception {
        
        List<Long> judgmentIdsToRemove = judgmentsToRemove.stream()
                .map(j -> j.getRemovedJudgmentId())
                .collect(Collectors.toList());
        
        judgmentWithEnrichmentDeleter.delete(judgmentIdsToRemove);
        
        removedJudgmentRepository.save(judgmentsToRemove);
        
        
    }

}
