package pl.edu.icm.saos.api.search;

import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.List;

/**
 * @author pavtel
 */
public class JudgmentsSearchResults {

    private RequestParameters requestParameters;

    private List<? extends Judgment> judgments;

    public JudgmentsSearchResults(RequestParameters requestParameters, List<? extends Judgment> judgments) {
        this.requestParameters = requestParameters;
        this.judgments = judgments;
    }

    public RequestParameters getRequestParameters() {
        return requestParameters;
    }

    public List<? extends Judgment> getJudgments() {
        return judgments;
    }
}
