package pl.edu.icm.saos.importer.commoncourt.download;

import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

/**
 * Common court judgment import - download - writer
 * 
 * 
 * @author Łukasz Dumiszewski
 */
@Service
public class CcjImportDownloadWriter implements ItemWriter<RawSourceCcJudgment> {

    private Logger log = LoggerFactory.getLogger(CcjImportDownloadWriter.class);
    
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository;
    
    private EntityManager entityManager;
    
    @Override
    public void write(List<? extends RawSourceCcJudgment> judgments) throws Exception {
        log.debug("persisting {} judgments...", judgments.size());
        rawSourceCcJudgmentRepository.save(judgments);
        rawSourceCcJudgmentRepository.flush();
        entityManager.clear();
        
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setRawSourceCcJudgmentRepository(RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository) {
        this.rawSourceCcJudgmentRepository = rawSourceCcJudgmentRepository;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
