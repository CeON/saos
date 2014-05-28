package pl.edu.icm.saos.persistence.model;

import java.util.List;

/**
 * pl. Sędzia
 * 
 * @author Łukasz Dumiszewski
 */

public class Judge {
    
    public enum JudgeRole {
        
        /** pl. przewodniczacy składu sędziowskiego */
        PRESIDING_JUDGE, 
        
        /** pl. sędzia sprawozdawca */
        REPORTING_JUDGE,
        
        /** pl. autor uzasadnienia */
        REASONS_FOR_JUDGMENT_AUTHOR
    }
    
    private Judgment judgment;
    
    private String name;
    
    private List<JudgeRole> specialRoles;

    
    //------------------------ GETTERS --------------------------
    
    public List<JudgeRole> getSpecialRoles() {
        return specialRoles;
    }

    public Judgment getJudgment() {
        return judgment;
    }

    /** judge's name (in most cases full name) */
    public String getName() {
        return name;
    }


    
    //------------------------ SETTERS --------------------------
    
    public void setSpecialRoles(List<JudgeRole> specialRoles) {
        this.specialRoles = specialRoles;
    }

    public void setJudgment(Judgment judgment) {
        this.judgment = judgment;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
