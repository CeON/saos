package pl.edu.icm.saos.webapp.api.model;

import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.List;

/**
 * @author pavtel
 */
public class JudgmentsSearchResults {

    private Pagination pagination;

    private List<? extends Judgment> judgments;

    public JudgmentsSearchResults(Pagination pagination, List<? extends Judgment> judgments) {
        this.pagination = pagination;
        this.judgments = judgments;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public List<? extends Judgment> getJudgments() {
        return judgments;
    }
}
