package pl.edu.icm.saos.api.dump.deletedjudgment.views;

import java.io.Serializable;

import pl.edu.icm.saos.api.dump.court.views.DumpCourtsView.Info;
import pl.edu.icm.saos.api.dump.court.views.DumpCourtsView.QueryTemplate;
import pl.edu.icm.saos.api.services.representations.success.CollectionRepresentation;

/**
 * A view representation of deleted judgment dump
 * @author Łukasz Dumiszewski
 */
public class DumpDeletedJudgmentsView extends CollectionRepresentation<Long, QueryTemplate, Info>{

    private static final long serialVersionUID = 1L;

    
   
    public static class QueryTemplate implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    public static class Info implements Serializable {
        private static final long serialVersionUID = 1L;
    }
}
