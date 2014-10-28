package pl.edu.icm.saos.api.support;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

/**
 * @author pavtel
 */
public class TestPersistenceObjectsContext {

    private CommonCourt commonCourt;
    private CommonCourtJudgment commonCourtJudgment;

    private CommonCourtDivision firstDivision;
    private CommonCourtDivision secondDivision;

    public int getJudgmentId(){
        return commonCourtJudgment.getId();
    }

    public int getCommonCourtId(){
        return commonCourt.getId();
    }

    public int getParentCourtId(){
        return commonCourt.getParentCourt().getId();
    }

    public int getFirstDivisionId(){
        return firstDivision.getId();
    }

    public int getSecondDivisionId(){
        return secondDivision.getId();
    }

    public CommonCourtDivision getFirstDivision() {
        return firstDivision;
    }

    public void setCommonCourt(CommonCourt commonCourt) {
        this.commonCourt = commonCourt;
    }

    public void setFirstDivision(CommonCourtDivision firstDivision) {
        this.firstDivision = firstDivision;
    }

    public void setSecondDivision(CommonCourtDivision secondDivision) {
        this.secondDivision = secondDivision;
    }

    public void setCommonCourtJudgment(CommonCourtJudgment commonCourtJudgment) {
        this.commonCourtJudgment = commonCourtJudgment;
    }
}
