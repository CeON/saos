package pl.edu.icm.saos.persistence.common;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * @author pavtel
 */
public class TestObjectContext {

    private CommonCourtJudgment ccJudgment;
    private SupremeCourtJudgment scJudgment;

    //------------------------ GETTERS --------------------------

    public int getCcJudgmentId(){
        return ccJudgment.getId();
    }

    public int getCcCourtId(){
        return ccJudgment.getCourtDivision().getCourt().getId();
    }

    public int getCcCourtParentId(){
        return ccJudgment.getCourtDivision().getCourt().getParentCourt().getId();
    }

    public int getCcFirstDivisionId(){
        return getCcDivisionId(0);
    }

    public int getCcSecondDivisionId(){
        return getCcDivisionId(1);
    }


    public CommonCourtJudgment getCcJudgment() {
        return ccJudgment;
    }

    public int getScJudgmentId(){
        return scJudgment.getId();
    }

    public int getScFirstDivisionId(){
        return scJudgment.getScChamberDivision().getId();
    }

    public int getScChamberId(){
        return scJudgment.getScChamberDivision().getScChamber().getId();
    }

    public int getScFirstChamberId(){
        return scJudgment.getScChambers().get(0).getId();
    }

    public SupremeCourtJudgment getScJudgment() {
        return scJudgment;
    }

    //------------------------ SETTERS --------------------------
    public void setCcJudgment(CommonCourtJudgment ccJudgment) {
        this.ccJudgment = ccJudgment;
    }

    public void setScJudgment(SupremeCourtJudgment scJudgment) {
        this.scJudgment = scJudgment;
    }

    //------------------------ PRIVATE --------------------------
    private int getCcDivisionId(int divisionIndex){
        return ccJudgment.getCourtDivision().getCourt().getDivisions().get(divisionIndex).getId();
    }
}
