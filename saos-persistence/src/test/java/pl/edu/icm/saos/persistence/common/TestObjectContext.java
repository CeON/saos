package pl.edu.icm.saos.persistence.common;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * @author pavtel
 */
public class TestObjectContext {

    private CommonCourtJudgment ccJudgment;
    private SupremeCourtJudgment scJudgment;
    private ConstitutionalTribunalJudgment ctJudgment;
    private NationalAppealChamberJudgment nacJudgment;

    //------------------------ GETTERS --------------------------

    public long getCcJudgmentId(){
        return ccJudgment.getId();
    }

    public long getCcCourtId(){
        return ccJudgment.getCourtDivision().getCourt().getId();
    }

    public long getCcCourtParentId(){
        return ccJudgment.getCourtDivision().getCourt().getParentCourt().getId();
    }

    public long getCcFirstDivisionId(){
        return getCcDivisionId(0);
    }

    public long getCcSecondDivisionId(){
        return getCcDivisionId(1);
    }


    public CommonCourtJudgment getCcJudgment() {
        return ccJudgment;
    }

    public long getScJudgmentId(){
        return scJudgment.getId();
    }

    public long getScFirstDivisionId(){
        return scJudgment.getScChamberDivision().getId();
    }

    public long getScChamberId(){
        return scJudgment.getScChamberDivision().getScChamber().getId();
    }

    public long getScFirstChamberId(){
        return scJudgment.getScChambers().get(0).getId();
    }

    public SupremeCourtJudgment getScJudgment() {
        return scJudgment;
    }

    public long getCtJudgmentId(){
        return ctJudgment.getId();
    }

    public ConstitutionalTribunalJudgment getCtJudgment() {
        return ctJudgment;
    }
    
    public long getNacJudgmentId() {
        return nacJudgment.getId();
    }
    
    public NationalAppealChamberJudgment getNacJudgment() {
        return nacJudgment;
    }

    //------------------------ SETTERS --------------------------
    void setCcJudgment(CommonCourtJudgment ccJudgment) {
        this.ccJudgment = ccJudgment;
    }

    void setScJudgment(SupremeCourtJudgment scJudgment) {
        this.scJudgment = scJudgment;
    }

    public void setCtJudgment(ConstitutionalTribunalJudgment ctJudgment) {
        this.ctJudgment = ctJudgment;
    }
    
    public void setNacJudgment(NationalAppealChamberJudgment nacJudgment) {
        this.nacJudgment = nacJudgment;
    }

    //------------------------ PRIVATE --------------------------
    
    private long getCcDivisionId(int divisionIndex){
        return ccJudgment.getCourtDivision().getCourt().getDivisions().get(divisionIndex).getId();
    }


}
