package pl.edu.icm.saos.importer.commoncourt.judgment.remove;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.commoncourt.judgment.SourceCcjExternalRepository;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * Finder of common court judgments that have been removed
 * from the external repository but are still present in the
 * saos repository.
 * 
 * @author madryk
 */
@Service
public class CcRemovedJudgmentsFinder {

    private final static Logger log = LoggerFactory.getLogger(CcRemovedJudgmentsFinder.class);


    private SourceCcjExternalRepository sourceCcjExternalRepository;
    
    private JudgmentRepository judgmentRepository;
    
    
    private int pageSize = 1000;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns a list of judgment ids that have been
     * removed in the external source repository.
     */
    public List<Long> findCcRemovedJudgmentSourceIds() {
        
        log.info("Searching for ccJudgments removed from external repository");
        
        List<Long> removedJudgmentsIds = judgmentRepository.findAllIdsBySourceCode(SourceCode.COMMON_COURT);
        
        
        int pageNumber = 0;
        while(true) {
            
            List<String> judgmentExternalIdsPage = sourceCcjExternalRepository.findJudgmentIds(pageNumber, pageSize, null);
            
            log.info("Downloaded {} judgment ids from external repository", (pageNumber*pageSize) + judgmentExternalIdsPage.size());
            
            
            if (judgmentExternalIdsPage.isEmpty()) {
                break;
            }
            
            List<Long> judgmentIdsPage = judgmentRepository.findAllIdsBySourceCodeAndSourceJudgmentIds(SourceCode.COMMON_COURT, judgmentExternalIdsPage);
            
            removedJudgmentsIds.removeAll(judgmentIdsPage);
            
            ++pageNumber;
        }
        
        return removedJudgmentsIds;
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

    /**
     * Defines how many judgment source ids will be downloaded
     * from external repository at one request.
     * Default value is 1000
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
