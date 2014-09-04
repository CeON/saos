package pl.edu.icm.saos.batch.admin;

import org.springframework.batch.admin.service.JobService;

/**
 * @author Łukasz Dumiszewski
 */

public interface SaosJobService extends JobService {
    
    /** this method is part of SimpleJobService */
    public void removeInactiveExecutions();
}
