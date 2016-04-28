package pl.edu.icm.saos.importer.commoncourt.judgment.remove;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.enrichment.delete.JudgmentWithEnrichmentDeleter;
import pl.edu.icm.saos.persistence.model.DeletedJudgment;
import pl.edu.icm.saos.persistence.repository.DeletedJudgmentRepository;

/**
 * Spring batch writer for removing judgments
 * 
 * @author madryk
 */
@Service
public class CcjDeleteRemovedWriter implements ItemWriter<DeletedJudgment> {

    @Autowired
    private JudgmentWithEnrichmentDeleter judgmentWithEnrichmentDeleter;
    
    @Autowired
    private DeletedJudgmentRepository deletedJudgmentRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void write(List<? extends DeletedJudgment> judgmentsToDelete) throws Exception {
        
        List<Long> judgmentIdsToDelete = judgmentsToDelete.stream()
                .map(j -> j.getRemovedJudgmentId())
                .collect(Collectors.toList());
        
        judgmentWithEnrichmentDeleter.delete(judgmentIdsToDelete);
        
        deletedJudgmentRepository.save(judgmentsToDelete);
        
        
    }

}
