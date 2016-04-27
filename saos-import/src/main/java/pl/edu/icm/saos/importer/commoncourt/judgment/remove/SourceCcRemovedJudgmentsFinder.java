package pl.edu.icm.saos.importer.commoncourt.judgment.remove;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.commoncourt.judgment.download.SourceCcjExternalRepository;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * Finder of common court judgments that was removed
 * from source external repository but still are present in the
 * saos repository.
 * 
 * @author madryk
 */
@Service
public class SourceCcRemovedJudgmentsFinder {

    private final static Logger log = LoggerFactory.getLogger(SourceCcRemovedJudgmentsFinder.class);


    private SourceCcjExternalRepository sourceCcjExternalRepository;
    
    private JudgmentRepository judgmentRepository;
    
    
    private int pageSize = 1000;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns list of source judgment ids from saos repository
     * that was removed from source external repository 
     */
    public List<String> findRemovedJudgments() {
        
        log.info("Searching for ccJudgments removed from external repository");
        
        List<String> removedJudgmentsSourceIds = judgmentRepository.findAllSourceIdsBySourceCode(SourceCode.COMMON_COURT);
        
        int pageNumber = 0;
        while(true) {
            
            List<String> judgmentExternalIdsPage = sourceCcjExternalRepository.findJudgmentIds(pageNumber, pageSize, null);
            
            log.info("Downloaded {} judgment ids from external repository", (pageNumber*pageSize) + judgmentExternalIdsPage.size());
            
            
            if (judgmentExternalIdsPage.isEmpty()) {
                break;
            }
            
            removedJudgmentsSourceIds.removeAll(judgmentExternalIdsPage);
            
            ++pageNumber;
        }
        
        return removedJudgmentsSourceIds;
    }


    //------------------------ SETTERS --------------------------

    @Autowired
    public void setSourceCcjExternalRepository(SourceCcjExternalRepository sourceCcjExternalRepository) {
        this.sourceCcjExternalRepository = sourceCcjExternalRepository;
    }

    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
