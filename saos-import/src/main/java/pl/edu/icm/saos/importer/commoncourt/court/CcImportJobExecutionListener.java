package pl.edu.icm.saos.importer.commoncourt.court;

import org.jboss.logging.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.service.CommonCourtHierarchyUpdater;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccImportJobExecutionListener")
public class CcImportJobExecutionListener implements JobExecutionListener {

    private static Logger log = Logger.getLogger(CcImportJobExecutionListener.class);
    
    @Autowired
    private CommonCourtHierarchyUpdater commonCourtHierarchyUpdater;
    
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("common court import has started...");
        
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("updating common court hierarchy...");
        commonCourtHierarchyUpdater.updateCommonCourtHierarchy();
        log.info("common court import has successfully finished");
        
        
    }

}
