package pl.edu.icm.saos.importer.common;

import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceJudgmentRepository;

/**
 * Judgment import - download - writer
 * 
 * 
 * @author Łukasz Dumiszewski
 */
@Service
public class JudgmentImportDownloadWriter implements ItemWriter<RawSourceJudgment> {

    private Logger log = LoggerFactory.getLogger(JudgmentImportDownloadWriter.class);
    
    private RawSourceJudgmentRepository rawSourceJudgmentRepository;
    
    private EntityManager entityManager;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void write(List<? extends RawSourceJudgment> judgments) throws Exception {
        log.debug("persisting {} judgments...", judgments.size());
        rawSourceJudgmentRepository.save(judgments);
        rawSourceJudgmentRepository.flush();
        entityManager.clear();
        
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setRawSourceJudgmentRepository(RawSourceJudgmentRepository rawSourceJudgmentRepository) {
        this.rawSourceJudgmentRepository = rawSourceJudgmentRepository;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
