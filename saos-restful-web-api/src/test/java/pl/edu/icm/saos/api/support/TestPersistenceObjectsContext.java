package pl.edu.icm.saos.api.support;

import pl.edu.icm.saos.persistence.model.*;

/**
 * @author pavtel
 */
public class TestPersistenceObjectsContext {

    private CommonCourt commonCourt;
    private CommonCourtJudgment commonCourtJudgment;

    private CommonCourtDivision firstDivision;
    private CommonCourtDivision secondDivision;

    private SupremeCourtChamberDivision scDivision;
    private SupremeCourtChamber scChamber;
    private SupremeCourtJudgmentForm scJudgmentForm;
    private SupremeCourtJudgment scJudgment;

    public int getJudgmentId(){
        return commonCourtJudgment.getId();
    }

    public CommonCourtJudgment getCcJudgment(){
        return commonCourtJudgment;
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

    public int getScChamberId(){
        return scChamber.getId();
    }

    public int getScJudgmentId(){
        return scJudgment.getId();
    }

    public CommonCourtDivision getFirstDivision() {
        return firstDivision;
    }

    public int getScDivisionId(){
        return scDivision.getId();
    }

    public SupremeCourtChamber getScChamber() {
        return scChamber;
    }

    public void setScChamber(SupremeCourtChamber scChamber) {
        this.scChamber = scChamber;
    }

    public SupremeCourtJudgment getScJudgment() {
        return scJudgment;
    }

    public SupremeCourtChamberDivision getScDivision() {
        return scDivision;
    }

    public void setScDivision(SupremeCourtChamberDivision scDivision) {
        this.scDivision = scDivision;
    }

    public SupremeCourtJudgmentForm getScJudgmentForm() {
        return scJudgmentForm;
    }

    public void setScJudgmentForm(SupremeCourtJudgmentForm scJudgmentForm) {
        this.scJudgmentForm = scJudgmentForm;
    }

    public void setScJudgment(SupremeCourtJudgment scJudgment) {
        this.scJudgment = scJudgment;
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
